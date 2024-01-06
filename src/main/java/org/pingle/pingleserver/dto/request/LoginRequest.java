package org.pingle.pingleserver.dto.request;

import jakarta.validation.constraints.NotNull;
import org.pingle.pingleserver.domain.enums.Provider;

public record LoginRequest(@NotNull Provider provider, String name) {
}
