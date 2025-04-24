package com.ovenAI.Tusk.components;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ExtractAnswer {
    public String extractAnswerText(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidatesList = getListOfMaps(response.get("candidates"));
            if (candidatesList.isEmpty()) return "No candidates found";

            Map<String, Object> content = getMap(candidatesList.get(0).get("content"));
            List<Map<String, Object>> parts = getListOfMaps(content.get("parts"));
            if (parts.isEmpty()) return "No parts found";

            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "Could not extract answer from response";
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getListOfMaps(Object obj) {
        if (obj instanceof List<?> list) {
            if (!list.isEmpty() && list.get(0) instanceof Map<?, ?>) {
                return (List<Map<String, Object>>) list;
            }
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(Object obj) {
        if (obj instanceof Map<?, ?>) {
            return (Map<String, Object>) obj;
        }
        return Collections.emptyMap();
    }
}
