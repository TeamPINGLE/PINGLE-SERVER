package org.pingle.pingleserver.dto.request;

import org.pingle.pingleserver.domain.enums.TKeyword;

public record TeamCreationRequest (
        String name,
        String email,
        TKeyword keyword
) {
}
