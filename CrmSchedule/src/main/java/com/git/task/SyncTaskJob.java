package com.git.task;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.entity.Contract;
import com.git.entity.ContractDetail;
import com.git.entity.ContractProduct;
import com.git.entity.SyncLog;
import com.git.service.ContractDetailService;
import com.git.service.ContractProductService;
import com.git.service.ContractService;
import com.git.service.CrmApiClient;
import com.git.service.SyncLogService;
import com.git.util.CovertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CRM 数据同步定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncTaskJob {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int PAGE_SIZE = 100;
    private static final int DETAIL_BATCH_SIZE = 100;

    private final CrmApiClient crmApiClient;
    private final ContractService contractService;
    private final ContractDetailService contractDetailService;
    private final ContractProductService contractProductService;
    private final SyncLogService syncLogService;

    @Value("${crm.task.cron:0 0/10 * * * ?}")
    private String cronExpression;

    @Value("${crm.task.query-date-type:today}")
    private String queryDateType;

    @Value("${crm.task.custom-date:}")
    private String customDate;

    /**
     * 定时执行同步任务
     */
    @Scheduled(cron = "${crm.task.cron:0 0/10 * * * ?}")
    public void execute() {
        log.info("============= CRM Sync Task Start =============");
        LocalDateTime startTime = LocalDateTime.now();
        int totalCount = 0;
        int successCount = 0;
        int failCount = 0;
        String errorMsg = null;

        try {
            // 第一步：获取并保存合同列表（返回合同号列表）
            List<String> contractNumbers = syncContractList();
            totalCount = contractNumbers.size();
            log.info("======== 合同列表同步完成，总数：{} ========", totalCount);

            // 第二步：批量获取合同详情并保存到 contract_detail 表
            if (!contractNumbers.isEmpty()) {
                log.info("======== 开始同步合同详情，总数：{} ========", contractNumbers.size());
                syncContractDetails(contractNumbers);
                successCount = contractNumbers.size();
            }

            // 记录成功日志
            LocalDateTime endTime = LocalDateTime.now();
            log.info("======== 准备保存同步日志，totalCount: {}, successCount: {}, failCount: {} ========", totalCount, successCount, failCount);
            SyncLog syncLog = syncLogService.createSuccessLog(
                    "CRM_SYNC", LocalDate.now().format(DATE_FORMATTER), totalCount, successCount, failCount, startTime, endTime
            );
            syncLogService.saveLog(syncLog);
            log.info("======== 同步日志保存完成 ========");

        } catch (Exception e) {
            log.error("CRM 同步任务执行失败", e);
            errorMsg = e.getMessage();
            failCount = totalCount;
            syncLogService.saveLog(syncLogService.createFailLog(
                    "CRM_SYNC", LocalDate.now().format(DATE_FORMATTER), errorMsg, startTime, LocalDateTime.now()
            ));
        }

        log.info("============= CRM Sync Task Finish =============");
    }

    /**
     * 同步合同列表（查询最近 10 天数据）
     */
    private List<String> syncContractList() {
        List<String> contractIds = new ArrayList<>();
        int page = 1;
        int totalPage = 0;

        // 获取 10 天前的日期和今天
        String startDate = LocalDate.now().minusDays(10).format(DATE_FORMATTER);
        String endDate = LocalDate.now().format(DATE_FORMATTER);
        log.info("======== 查询最近 10 天合同数据，ht_date >= {} ========", startDate);

        JSONObject result = crmApiClient.getContractListByStartDate(page, startDate, PAGE_SIZE);

        if (result != null) {
            // API 返回的是 totalCount 而不是 total
            Integer total = result.getInteger("totalCount");
            if (total != null && total > 0) {
                totalPage = (total - 1) / PAGE_SIZE + 1;
            }
            log.info("======== API 返回总数据量：{}, 总页数：{} ========", total, totalPage);

            // 处理第一页数据
            JSONArray dataArray = result.getJSONArray("data");
            if (dataArray != null && !dataArray.isEmpty()) {
                // API 已返回 addtime 在最近 10 天的数据，直接保存
                List<Contract> contracts = CovertUtil.jsonToContractList(dataArray);
                contractService.batchSaveOrUpdate(contracts);
                extractContractIds(dataArray, contractIds);
                log.info("======== 已保存第 {} 页，{} 条记录 ========", page, contracts.size());
            }

            // 处理后续分页数据
            for (int i = 1; i < totalPage; i++) {
                page++;
                result = crmApiClient.getContractListByStartDate(page, startDate, PAGE_SIZE);
                if (result != null) {
                    dataArray = result.getJSONArray("data");
                    if (dataArray != null && !dataArray.isEmpty()) {
                        List<Contract> contracts = CovertUtil.jsonToContractList(dataArray);
                        contractService.batchSaveOrUpdate(contracts);
                        extractContractIds(dataArray, contractIds);
                        log.info("======== 已保存第 {} 页，{} 条记录 ========", page, contracts.size());
                    }
                }
            }
        }

        log.info("======== 合同列表同步完成，共获取 {} 个合同 ID（最近 10 天） ========", contractIds.size());
        return contractIds;
    }

    /**
     * 按日期范围过滤合同（使用 ht_date 字段，因为列表 API 不返回 addtime）
     */
    private List<Contract> filterByDateRange(List<Contract> contracts, String startDate, String endDate) {
        return contracts.stream()
            .filter(c -> {
                // 列表 API 返回的数据中没有 addtime，使用 ht_date
                String date = c.getHtDate();
                if (date == null || date.length() < 10) {
                    return false;
                }
                // 提取日期部分（去掉时间）
                date = date.substring(0, 10);
                return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
            })
            .collect(Collectors.toList());
    }

    /**
     * 同步合同详情（保存到 contract_detail 表）
     * 根据合同 ID 列表遍历查询每个合同的详情
     */
    private void syncContractDetails(List<String> contractIds) {
        List<ContractDetail> detailList = new ArrayList<>();
        List<ContractProduct> productList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;
        int totalProducts = 0;

        log.info("======== 开始同步合同详情，共 {} 个合同 ========", contractIds.size());

        for (String contractId : contractIds) {
            try {
                // 根据合同 ID 查询合同详情（返回 JSONObject）
                JSONObject detailResult = crmApiClient.getContractDetail(contractId);
                if (detailResult != null) {
                    log.debug("======== 合同详情 API 返回，ID: {} ========", contractId);

                    // 解析合同详情（从 data.detail 中提取）
                    ContractDetail detail = CovertUtil.jsonToContractDetail(detailResult);

                    if (detail != null && detail.getHtId() != null) {
                        log.debug("======== 合同详情解析成功，ID: {} ========", contractId);
                        detailList.add(detail);
                        successCount++;

                        // 解析产品明细（从 data.detail.child_mx 中提取）
                        List<ContractProduct> products = CovertUtil.jsonToContractProducts(detailResult, contractId);
                        if (!products.isEmpty()) {
                            productList.addAll(products);
                            totalProducts += products.size();
                            log.info("======== 合同 {} 包含 {} 个产品明细 ========", contractId, products.size());
                            // 记录每个产品的详细信息
                            for (int i = 0; i < products.size(); i++) {
                                ContractProduct p = products.get(i);
                                log.info("  产品 {}: productCode={}, productId={}, productName={}, quantity={}, totalPrice={}", 
                                    i+1, p.getProductCode(), p.getProductId(), p.getProductName(), p.getQuantity(), p.getTotalPrice());
                            }
                        } else {
                            log.info("======== 合同 {} 没有产品明细 ========", contractId);
                            // 记录原始 child_mx 数据
                            JSONObject detailData = detailResult.getJSONObject("detail");
                            if (detailData != null) {
                                JSONArray childMx = detailData.getJSONArray("child_mx");
                                log.info("  原始 child_mx: {}", childMx != null ? childMx.toJSONString() : "null");
                            }
                        }
                    } else {
                        failCount++;
                        log.warn("======== 合同详情解析失败，ID: {}, detail: {} ========", contractId, detail);
                    }
                } else {
                    failCount++;
                    log.warn("======== 获取合同详情失败，ID: {}, 结果为空 ========", contractId);
                }
            } catch (Exception e) {
                failCount++;
                log.error("======== 获取合同详情异常，ID: {} ========", contractId, e);
            }

            // 每批写入数据库
            if (detailList.size() >= DETAIL_BATCH_SIZE) {
                contractDetailService.batchSaveOrUpdate(detailList);
                log.info("======== 已批量保存 {} 条合同详情到 contract_detail 表 ========", detailList.size());
                detailList.clear();
            }
            if (productList.size() >= DETAIL_BATCH_SIZE * 5) {
                contractProductService.batchSaveOrUpdate(productList);
                log.info("======== 已批量保存 {} 条产品明细到 crm_contract_product 表 ========", productList.size());
                productList.clear();
            }
        }

        // 写入剩余数据
        if (!detailList.isEmpty()) {
            contractDetailService.batchSaveOrUpdate(detailList);
            log.info("======== 已批量保存 {} 条合同详情到 contract_detail 表 ========", detailList.size());
        }
        if (!productList.isEmpty()) {
            contractProductService.batchSaveOrUpdate(productList);
            log.info("======== 已批量保存 {} 条产品明细到 crm_contract_product 表 ========", productList.size());
        }

        log.info("======== 合同详情同步完成，成功：{}, 失败：{}, 产品明细总数：{} ========",
            successCount, failCount, totalProducts);
    }

    /**
     * 从列表数据中提取合同 ID
     */
    private void extractContractIds(JSONArray data, List<String> contractIds) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                JSONObject item = data.getJSONObject(i);
                // 从 item.detail.ht_id 提取
                JSONObject detail = item.getJSONObject("detail");
                if (detail != null) {
                    String htId = detail.getString("ht_id");
                    if (htId != null && !htId.isEmpty() && !contractIds.contains(htId)) {
                        contractIds.add(htId);
                    }
                }
            }
        }
    }

    /**
     * 获取查询日期
     */
    private String getQueryDate() {
        if ("all".equals(queryDateType)) {
            // 查询所有合同，不限制日期
            return null;
        } else if ("custom".equals(queryDateType) && customDate != null && !customDate.isEmpty()) {
            return customDate;
        } else if ("yesterday".equals(queryDateType)) {
            return LocalDate.now().minusDays(1).format(DATE_FORMATTER);
        } else {
            // 默认查询当天
            return LocalDate.now().format(DATE_FORMATTER);
        }
    }
}
