package com.git.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.entity.ContractDetail;
import com.git.mapper.ContractDetailMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合同详情服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractDetailService {

    private final ContractDetailMapper contractDetailMapper;

    /**
     * 批量保存或更新合同详情
     *
     * @param details 合同详情列表
     * @return 保存成功的数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchSaveOrUpdate(List<ContractDetail> details) {
        int count = 0;
        for (ContractDetail detail : details) {
            try {
                // 先检查是否已存在
                ContractDetail existing = contractDetailMapper.selectOne(
                        new LambdaQueryWrapper<ContractDetail>().eq(ContractDetail::getHtId, detail.getHtId())
                );
                if (existing != null) {
                    detail.setId(existing.getId());
                    contractDetailMapper.updateById(detail);
                } else {
                    contractDetailMapper.insert(detail);
                }
                count++;
            } catch (Exception e) {
                log.error("保存合同详情失败，htId: {}", detail.getHtId(), e);
            }
        }
        return count;
    }

    /**
     * 根据合同 ID 查询合同详情
     */
    public ContractDetail getByHtId(String htId) {
        return contractDetailMapper.selectOne(
                new LambdaQueryWrapper<ContractDetail>().eq(ContractDetail::getHtId, htId)
        );
    }

    /**
     * 查询所有合同详情
     */
    public List<ContractDetail> listAll() {
        return contractDetailMapper.selectList(null);
    }

    /**
     * 分页查询合同详情
     */
    public List<ContractDetail> listByPage(int page, int size) {
        LambdaQueryWrapper<ContractDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ContractDetail::getSyncTime);
        int offset = (page - 1) * size;
        return contractDetailMapper.selectList(wrapper);
    }
}
