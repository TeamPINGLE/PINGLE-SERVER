package org.pingle.pingleserver.dto.reponse;

import lombok.Builder;
import org.pingle.pingleserver.domain.enums.TKeyword;

@Builder
public record KeywordResponse (String name, String value){

    public static KeywordResponse of(TKeyword keyword){
        return KeywordResponse.builder()
                .name(keyword.toString())
                .value(keyword.getValue())
                .build();
    }
}
