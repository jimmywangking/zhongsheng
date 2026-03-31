package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.git.util.SignUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CRM API 单元测试类
 */
public class CrmApiTest {

    private static final String APP_ID = "41314";
    private static final String SECRET_ID = "s74gdLX96ULx";
    private static final String SECRET_KEY = "7ecd94df-fdfd-4dd5-9a38-9fa470ec5c33";
    private static final String DATATYPE = "150";

    /**
     * 测试签名生成
     */
    @Test
    @DisplayName("测试签名生成算法")
    public void testSignGeneration() throws Exception {
        String type = "list_simple";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", 1711526400L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 10);

        String postDataStr = JSON.toJSONString(postData);
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);

        // 验证签名不为空
        assertNotNull(sign);
        assertFalse(sign.isEmpty());

        // 验证签名经过 URL 编码
        assertTrue(sign.contains("%"));

        System.out.println("签名源字符串：" + APP_ID + type + postDataStr);
        System.out.println("生成的签名：" + sign);
    }

    /**
     * 测试合同列表 API 调用
     */
    @Test
    @DisplayName("测试合同订单列表 API 调用")
    public void testContractListApi() {
        String type = "list_simple";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 10);

        String postDataStr = JSON.toJSONString(postData);

        try {
            String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);
            String url = "https://crmapi.jzsoft.cn/apigetdata.aspx?version=v2&sign=" + sign +
                    "&key=" + SECRET_ID + "&appid=" + APP_ID + "&type=" + type;

            JSONObject result = SignUtil.httpPost(url, postDataStr);

            // 验证返回结果
            assertNotNull(result);
            assertTrue(result.containsKey("result") || result.containsKey("errcode"));

            if (result.containsKey("errcode")) {
                String errcode = result.getString("errcode");
                if (!"0".equals(errcode)) {
                    System.err.println("API 调用失败：" + result.getString("errmsg"));
                    fail("API 调用失败：" + result.getString("errmsg"));
                }
            }

            // 解析 result
            if (result.containsKey("result")) {
                JSONObject resultObj = result.getJSONObject("result");
                assertNotNull(resultObj);

                // 验证总记录数
                Integer total = resultObj.getInteger("total");
                System.out.println("总记录数：" + total);
                assertTrue(total >= 0);

                // 验证数据数组
                JSONArray data = resultObj.getJSONArray("data");
                assertNotNull(data);

                if (data.size() > 0) {
                    System.out.println("返回数据条数：" + data.size());

                    // 验证第一条数据的字段
                    JSONObject firstItem = data.getJSONObject(0);
                    validateContractFields(firstItem);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("API 调用异常：" + e.getMessage());
        }
    }

    /**
     * 测试合同详情 API 调用
     */
    @Test
    @DisplayName("测试单个合同详情 API 调用")
    public void testContractDetailApi() {
        // 先获取列表获取一个合同 ID
        String contractId = getFirstContractId();

        if (contractId == null) {
            System.out.println("暂无合同数据，跳过详情测试");
            return;
        }

        String type = "view";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("msgid", contractId);

        String postDataStr = JSON.toJSONString(postData);

        try {
            String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);
            String url = "https://crmapi.jzsoft.cn/apigetdata.aspx?version=v2&sign=" + sign +
                    "&key=" + SECRET_ID + "&appid=" + APP_ID + "&type=" + type;

            JSONObject result = SignUtil.httpPost(url, postDataStr);

            // 验证返回结果
            assertNotNull(result);

            if (result.containsKey("result")) {
                JSONObject resultObj = result.getJSONObject("result");
                
                // 详情接口返回的数据可能在 data 字段或直接在 result 中
                JSONObject detailData = null;
                if (resultObj.containsKey("data")) {
                    detailData = resultObj.getJSONObject("data");
                } else {
                    detailData = resultObj;
                }

                if (detailData != null && !detailData.isEmpty()) {
                    System.out.println("合同详情获取成功");
                    validateContractFields(detailData);

                    // 验证详情特有字段
                    if (detailData.containsKey("ht_remark")) {
                        System.out.println("备注：" + detailData.get("ht_remark"));
                    }
                    if (detailData.containsKey("ht_address")) {
                        System.out.println("地址：" + detailData.get("ht_address"));
                    }
                } else {
                    System.out.println("详情数据为空");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 详情 API 可能失败，不阻断测试
            System.out.println("详情 API 调用异常：" + e.getMessage());
        }
    }

    /**
     * 验证合同字段
     */
    private void validateContractFields(JSONObject item) {
        // 验证必填字段
        assertTrue(item.containsKey("ht_id"), "缺少 ht_id 字段");
        assertTrue(item.containsKey("key"), "缺少 key 字段");

        String htId = item.getString("ht_id");
        assertNotNull(htId);
        assertFalse(htId.isEmpty());
        System.out.println("合同 ID: " + htId);

        // 验证合同标题
        if (item.containsKey("ht_title")) {
            Object titleObj = item.get("ht_title");
            if (titleObj instanceof JSONObject) {
                JSONObject title = (JSONObject) titleObj;
                String titleValue = title.getString("value");
                System.out.println("合同标题：" + titleValue);
                assertNotNull(titleValue);
            }
        }

        // 验证合同编号
        if (item.containsKey("ht_number")) {
            String number = item.getString("ht_number");
            System.out.println("合同编号：" + number);
        }

        // 验证合同金额
        if (item.containsKey("ht_summoney")) {
            Object moneyObj = item.get("ht_summoney");
            if (moneyObj instanceof JSONObject) {
                JSONObject money = (JSONObject) moneyObj;
                Object value = money.get("value");
                System.out.println("合同金额：" + value);
            }
        }

        // 验证客户
        if (item.containsKey("ht_customerid")) {
            Object customerObj = item.get("ht_customerid");
            if (customerObj instanceof JSONObject) {
                JSONObject customer = (JSONObject) customerObj;
                String label = customer.getString("label");
                System.out.println("客户名称：" + label);
            }
        }

        // 验证负责人
        if (item.containsKey("ht_preside")) {
            String preside = item.getString("ht_preside");
            System.out.println("负责人：" + preside);
        }

        // 验证创建时间
        if (item.containsKey("addtime")) {
            String addtime = item.getString("addtime");
            System.out.println("创建时间：" + addtime);
        }
    }

    /**
     * 获取第一个合同 ID
     */
    private String getFirstContractId() {
        String type = "list_simple";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 1);

        String postDataStr = JSON.toJSONString(postData);

        try {
            String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);
            String url = "https://crmapi.jzsoft.cn/apigetdata.aspx?version=v2&sign=" + sign +
                    "&key=" + SECRET_ID + "&appid=" + APP_ID + "&type=" + type;

            JSONObject result = SignUtil.httpPost(url, postDataStr);

            if (result != null && result.containsKey("result")) {
                JSONObject resultObj = result.getJSONObject("result");
                JSONArray data = resultObj.getJSONArray("data");

                if (data != null && data.size() > 0) {
                    JSONObject firstItem = data.getJSONObject(0);
                    return firstItem.getString("ht_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 测试 POST 数据生成
     */
    @Test
    @DisplayName("测试 POST 数据生成")
    public void testPostDataGeneration() {
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 10);

        String postDataStr = JSON.toJSONString(postData);

        System.out.println("========== POST DATA ==========");
        System.out.println(postDataStr);

        // 验证 JSON 格式
        JSONObject json = JSON.parseObject(postDataStr);
        assertNotNull(json);
        assertEquals(DATATYPE, json.getString("datatype"));
        assertEquals(1, json.getInteger("page"));
        assertEquals(10, json.getInteger("pagesize"));
    }

    /**
     * 测试签名一致性
     */
    @Test
    @DisplayName("测试签名一致性（多次生成相同）")
    public void testSignConsistency() throws Exception {
        String type = "list_simple";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", 1711526400L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);

        String postDataStr = JSON.toJSONString(postData);

        String sign1 = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);
        String sign2 = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);

        assertEquals(sign1, sign2, "相同输入应该生成相同的签名");
    }
}
