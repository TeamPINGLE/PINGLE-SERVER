package org.pingle.pingleserver.dto.request;

import org.pingle.pingleserver.domain.enums.Provider;

public record LoginRequest(Provider provider) {
}
