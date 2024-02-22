package org.pingle.pingleserver.dto.reponse;

import org.pingle.pingleserver.domain.enums.MCategory;

public record PinResponse(Long id, Double x, Double y, MCategory category) {
}
