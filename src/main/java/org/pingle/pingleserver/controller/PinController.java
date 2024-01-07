package org.pingle.pingleserver.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.reponse.PinResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.PinService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/teams/{teamId}/pins")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @GetMapping
    public ApiResponse<List<PinResponse>> getPinList (@PathVariable("teamId") Long teamId, @Nullable @RequestParam("category")MCategory category) {
        return ApiResponse.success(SuccessMessage.OK, pinService.getPinListFilterByCategory(teamId, category));

    }
}
