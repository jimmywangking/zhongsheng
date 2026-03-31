package com.git.service;

import com.git.entity.ContractProduct;
import com.git.mapper.ContractProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合同产品明细服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractProductService {

    private final ContractProductMapper contractProductMapper;

    /**
     * 批量保存或更新产品明细
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchSaveOrUpdate(List<ContractProduct> products) {
        int count = 0;
        for (ContractProduct product : products) {
            try {
                // 根据联合主键 (ht_id, product_id) 查询是否存在
                ContractProduct existing = contractProductMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ContractProduct>()
                        .eq(ContractProduct::getHtId, product.getHtId())
                        .eq(ContractProduct::getProductId, product.getProductId())
                );
                if (existing != null) {
                    // 更新其他字段
                    contractProductMapper.update(product,
                        new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<ContractProduct>()
                            .eq(ContractProduct::getHtId, product.getHtId())
                            .eq(ContractProduct::getProductId, product.getProductId())
                            .set(ContractProduct::getProductCode, product.getProductCode())
                            .set(ContractProduct::getProductName, product.getProductName())
                            .set(ContractProduct::getProductModel, product.getProductModel())
                            .set(ContractProduct::getProductSpec, product.getProductSpec())
                            .set(ContractProduct::getUnit, product.getUnit())
                            .set(ContractProduct::getQuantity, product.getQuantity())
                            .set(ContractProduct::getRetailPrice, product.getRetailPrice())
                            .set(ContractProduct::getUnitPrice, product.getUnitPrice())
                            .set(ContractProduct::getTotalPrice, product.getTotalPrice())
                            .set(ContractProduct::getRemark, product.getRemark())
                            .set(ContractProduct::getCostPrice, product.getCostPrice())
                            .set(ContractProduct::getCostTotal, product.getCostTotal())
                    );
                } else {
                    contractProductMapper.insert(product);
                }
                count++;
            } catch (Exception e) {
                log.error("保存产品明细失败，htId: {}, productId: {}",
                    product.getHtId(), product.getProductId(), e);
            }
        }
        return count;
    }
}
