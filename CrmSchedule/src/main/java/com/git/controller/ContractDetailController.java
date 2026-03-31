package com.git.controller;

import com.git.entity.ContractDetail;
import com.git.service.ContractDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同详情查询接口
 */
@RestController
@RequestMapping("/api/contract-details")
@RequiredArgsConstructor
public class ContractDetailController {

    private final ContractDetailService contractDetailService;

    /**
     * 查询所有合同详情
     */
    @GetMapping
    public List<ContractDetail> listAll() {
        return contractDetailService.listAll();
    }

    /**
     * 分页查询合同详情
     */
    @GetMapping("/page")
    public Map<String, Object> listByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ContractDetail> list = contractDetailService.listByPage(page, size);
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    /**
     * 根据合同 ID 查询详情
     */
    @GetMapping("/{htId}")
    public ContractDetail getByHtId(@PathVariable String htId) {
        return contractDetailService.getByHtId(htId);
    }
}
