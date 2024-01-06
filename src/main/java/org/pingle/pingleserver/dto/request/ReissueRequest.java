package org.pingle.pingleserver.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReissueRequest(@NotNull String refreshToken) {
}
