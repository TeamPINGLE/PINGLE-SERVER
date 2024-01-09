package org.pingle.pingleserver.utils;

import org.springframework.stereotype.Component;


@Component
public class CustomSearchUtil {
    public int calculateScoreIgnoreCase(String item, String searchTerm) {
        String uItem = item.toUpperCase();
        String uSearchTerm = searchTerm.toUpperCase();
        if (uItem.equals(uSearchTerm)) {
            return 1;
        } else if (uItem.startsWith(uSearchTerm)) {
            return 2;
        } else if (uItem.contains(uSearchTerm)) {
            return 3;
        }
        return 4;
    }
}
