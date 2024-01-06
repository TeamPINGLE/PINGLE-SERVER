package org.pingle.pingleserver.dto.request;

import lombok.NonNull;
import org.pingle.pingleserver.domain.enums.Provider;

public record LoginRequest(@NonNull Provider provider, String name) {
}
