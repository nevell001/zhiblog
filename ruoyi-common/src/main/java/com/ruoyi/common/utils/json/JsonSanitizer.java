package com.ruoyi.common.utils.json;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.html.EscapeUtil;

/**
 * 对 JSON 结构中的字符串值做 XSS 清理，保留原始 JSON 结构以避免破坏 JSON 格式。
 */
public class JsonSanitizer {

    public static String sanitize(String json) {
        if (StringUtils.isEmpty(json)) {
            return json;
        }
        String trimmed = json.trim();
        try {
            if (trimmed.startsWith("{")) {
                JSONObject obj = JSON.parseObject(json);
                sanitizeObject(obj);
                return obj.toString();
            } else if (trimmed.startsWith("[")) {
                JSONArray arr = JSON.parseArray(json);
                sanitizeArray(arr);
                return arr.toString();
            } else {
                // 非 JSON，退回到普通清理
                return EscapeUtil.clean(json);
            }
        } catch (Exception e) {
            // 解析或处理失败时，回退到原有清理策略以保证安全性
            return EscapeUtil.clean(json);
        }
    }

    private static void sanitizeObject(JSONObject obj) {
        for (String key : obj.keySet()) {
            Object val = obj.get(key);
            if (val instanceof String) {
                String s = (String) val;
                obj.put(key, EscapeUtil.clean(s).trim());
            } else if (val instanceof JSONObject) {
                sanitizeObject((JSONObject) val);
            } else if (val instanceof JSONArray) {
                sanitizeArray((JSONArray) val);
            }
        }
    }

    private static void sanitizeArray(JSONArray arr) {
        for (int i = 0; i < arr.size(); i++) {
            Object val = arr.get(i);
            if (val instanceof String) {
                arr.set(i, EscapeUtil.clean((String) val).trim());
            } else if (val instanceof JSONObject) {
                sanitizeObject((JSONObject) val);
            } else if (val instanceof JSONArray) {
                sanitizeArray((JSONArray) val);
            }
        }
    }
}
