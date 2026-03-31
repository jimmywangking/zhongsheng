package com.git.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.entity.Contract;
import com.git.mapper.ContractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合同服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractMapper contractMapper;

    /**
     * 批量保存或更新合同列表
     *
     * @param contracts 合同列表
     * @return 保存成功的数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchSaveOrUpdate(List<Contract> contracts) {
        int count = 0;
        for (Contract contract : contracts) {
            try {
                // 先检查是否已存在
                Contract existing = contractMapper.selectOne(
                        new LambdaQueryWrapper<Contract>().eq(Contract::getHtId, contract.getHtId())
                );
                if (existing != null) {
                    contract.setId(existing.getId());
                    contractMapper.updateById(contract);
                } else {
                    contractMapper.insert(contract);
                }
                count++;
            } catch (Exception e) {
                log.error("保存合同失败，htId: {}", contract.getHtId(), e);
            }
        }
        return count;
    }

    /**
     * 根据合同号查询合同
     */
    public Contract getByHtNumber(String htNumber) {
        return contractMapper.selectOne(
                new LambdaQueryWrapper<Contract>().eq(Contract::getHtNumber, htNumber)
        );
    }

    /**
     * 根据合同 ID 查询合同
     */
    public Contract getByHtId(String htId) {
        return contractMapper.selectOne(
                new LambdaQueryWrapper<Contract>().eq(Contract::getHtId, htId)
        );
    }

    /**
     * 查询所有合同
     */
    public List<Contract> listAll() {
        return contractMapper.selectList(null);
    }

    /**
     * 分页查询合同
     */
    public List<Contract> listByPage(int page, int size) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Contract::getSyncTime);
        int offset = (page - 1) * size;
        return contractMapper.selectList(wrapper);
    }
}
