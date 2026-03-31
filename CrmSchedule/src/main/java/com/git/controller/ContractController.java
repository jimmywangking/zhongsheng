package com.git.controller;

import com.git.entity.Contract;
import com.git.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同查询接口
 */
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    /**
     * 查询所有合同
     */
    @GetMapping
    public List<Contract> listAll() {
        return contractService.listAll();
    }

    /**
     * 分页查询合同
     */
    @GetMapping("/page")
    public Map<String, Object> listByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Contract> list = contractService.listByPage(page, size);
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    /**
     * 根据合同 ID 查询
     */
    @GetMapping("/{htId}")
    public Contract getByHtId(@PathVariable String htId) {
        return contractService.getByHtId(htId);
    }
}
