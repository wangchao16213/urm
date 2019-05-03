package cn.com.eship.utils;

import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.enums.BehaviorFieldType;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilderHelper {
    public static Map<String, Object> getFilterItem(BehaviorField behaviorField) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", behaviorField.getFullFieldName());
        item.put("label", behaviorField.getFieldLable());
        switch (behaviorField.getFieldType()) {
            case BehaviorFieldType
                    .TEXT: {
                item.put("type", "string");
                break;
            }
            case BehaviorFieldType
                    .NUMBER: {
                item.put("type", "integer");
                break;

            }
            case BehaviorFieldType
                    .BOOLEAN: {
                item.put("type", "integer");
                item.put("input", "radio");
                Map<Integer, String> values = new HashMap<>();
                values.put(1, "是");
                values.put(2, "否");
                item.put("values", values);
                break;

            }
            case BehaviorFieldType
                    .DATE: {
                item.put("type", "string");
                break;

            }
        }
        return item;

    }
}
