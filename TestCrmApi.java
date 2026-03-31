import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CRM API 接口测试 - 独立版本
 */
public class TestCrmApi {

    private static final String APP_ID = "41314";
    private static final String SECRET_ID = "s74gdLX96ULx";
    private static final String SECRET_KEY = "7ecd94df-fdfd-4dd5-9a38-9fa470ec5c33";
    private static final String API_URL = "https://crmapi.jzsoft.cn/apigetdata.aspx";
    private static final String DATATYPE = "150";

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 CRM API 接口测试 - 合同列表 + 合同详情             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            testListAndDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testListAndDetail() throws Exception {
        // ========== 测试 1: 合同列表 ==========
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📋 测试 1: 合同订单列表 API");
        System.out.println("════════════════════════════════════════════════════════════════");

        String contractId = getFirstContractId();
        System.out.println();
        System.out.println("✅ 获取到合同 ID: " + contractId);
        System.out.println();

        // ========== 测试 2: 合同详情 ==========
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📄 测试 2: 合同详情 API (使用上面获取的合同 ID)");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();

        if (contractId == null) {
            System.out.println("❌ 无法获取合同 ID，测试终止");
            return;
        }

        String type = "view";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("msgid", contractId);

        String postDataStr = JSON.toJSONString(postData);
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);

        String url = API_URL + "?version=v2&sign=" + sign +
                "&key=" + SECRET_ID +
                "&appid=" + APP_ID +
                "&type=" + type;

        System.out.println("【请求参数】");
        System.out.println("  msgid (合同 ID): " + contractId);
        System.out.println("  POST Data: " + postDataStr);
        System.out.println();

        System.out.println("【签名信息】");
        System.out.println("  签名源字符串：" + APP_ID + type + postDataStr);
        System.out.println("  签名值：" + sign);
        System.out.println();

        System.out.println("【请求 URL】");
        System.out.println("  " + url);
        System.out.println();

        System.out.println("【发送请求...】");
        JSONObject response = SignUtil.httpPost(url, postDataStr);

        System.out.println();
        System.out.println("【返回结果】");

        if (response != null) {
            if (response.containsKey("errcode")) {
                String errcode = response.getString("errcode");
                if (!"0".equals(errcode)) {
                    System.out.println("  ❌ API 调用失败!");
                    System.out.println("  错误码：" + errcode);
                    System.out.println("  错误信息：" + response.getString("errmsg"));
                    return;
                }
            }

            if (response.containsKey("result")) {
                JSONObject result = response.getJSONObject("result");

                System.out.println("  ✅ API 调用成功!");
                System.out.println();

                // 检查是否有 data 字段
                JSONObject detailData = result.getJSONObject("data");
                if (detailData == null) {
                    System.out.println("  ⚠️ 返回结果中没有 data 字段，直接使用 result");
                    detailData = result;
                }

                if (detailData != null && !detailData.isEmpty()) {
                    System.out.println("╔════════════════════════════════════════════════════════════════╗");
                    System.out.println("║       📄 合同详情 JSON (完整)                                  ║");
                    System.out.println("╚════════════════════════════════════════════════════════════════╝");
                    System.out.println();
                    System.out.println(detailData.toJSONString());
                    System.out.println();
                    System.out.println("════════════════════════════════════════════════════════════════");
                    System.out.println();

                    // 提取关键字段
                    System.out.println("【合同详情关键字段】");
                    System.out.println("  ht_id: " + detailData.getString("ht_id"));
                    System.out.println("  ht_number: " + detailData.getString("ht_number"));

                    JSONObject htTitle = detailData.getJSONObject("ht_title");
                    if (htTitle != null) {
                        System.out.println("  ht_title: " + htTitle.getString("value"));
                    }

                    JSONObject htSummoney = detailData.getJSONObject("ht_summoney");
                    if (htSummoney != null) {
                        System.out.println("  ht_summoney: " + htSummoney.get("value"));
                    }

                    JSONObject htCustomerid = detailData.getJSONObject("ht_customerid");
                    if (htCustomerid != null) {
                        System.out.println("  ht_customer: " + htCustomerid.getString("label"));
                    }

                    // 详情特有字段
                    JSONObject htRemark = detailData.getJSONObject("ht_remark");
                    System.out.println("  ht_remark: " + (htRemark != null ? htRemark.getString("value") : "null"));

                    JSONObject htAddress = detailData.getJSONObject("ht_address");
                    System.out.println("  ht_address: " + (htAddress != null ? htAddress.getString("value") : "null"));

                    System.out.println();
                    System.out.println("╔════════════════════════════════════════════════════════════════╗");
                    System.out.println("║          🎉 测试完成！                                        ║");
                    System.out.println("╚════════════════════════════════════════════════════════════════╝");
                } else {
                    System.out.println("  ❌ 详情数据为空");
                    System.out.println("  完整返回：" + response.toJSONString());
                }
            } else {
                System.out.println("  ❌ 返回结果中没有 result 字段");
                System.out.println("  完整返回：" + response.toJSONString());
            }
        } else {
            System.out.println("  ❌ HTTP 请求失败，返回 null");
        }
    }

    public static String getFirstContractId() throws Exception {
        System.out.println();
        System.out.println("【步骤 1: 获取合同列表，提取第一个合同 ID】");
        System.out.println();

        String type = "list_simple";

        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 10);

        String postDataStr = JSON.toJSONString(postData);
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);

        String url = API_URL + "?version=v2&sign=" + sign +
                "&key=" + SECRET_ID +
                "&appid=" + APP_ID +
                "&type=" + type;

        System.out.println("  请求 URL: " + url);
        System.out.println();

        JSONObject response = SignUtil.httpPost(url, postDataStr);

        if (response != null && response.containsKey("result")) {
            JSONObject result = response.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");

            if (data != null && data.size() > 0) {
                JSONObject firstItem = data.getJSONObject(0);
                String htId = firstItem.getString("ht_id");

                System.out.println("  ✅ 列表 API 调用成功!");
                System.out.println("  总记录数：" + result.getInteger("total"));
                System.out.println("  返回条数：" + data.size());
                System.out.println();

                System.out.println("【第一条数据 JSON】");
                System.out.println(firstItem.toJSONString());
                System.out.println();

                return htId;
            }
        }

        System.out.println("  ❌ 列表 API 调用失败");
        return null;
    }
}
