package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.controller.swagger.PinApi;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.reponse.MeetingResponse;
import org.pingle.pingleserver.dto.reponse.PinResponse;
import org.pingle.pingleserver.dto.response.RankingResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.PinService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/teams/{teamId}/pins")
@RequiredArgsConstructor
public class PinController implements PinApi {

    private final PinService pinService;

    @GetMapping
    public ApiResponse<List<PinResponse>> getPins (@PathVariable Long teamId,
                                                   @RequestParam(required = false)MCategory category,
                                                   @RequestParam(required = false)String q) {
        return ApiResponse.success(SuccessMessage.OK, pinService.getPins(teamId, category, q));
    }

    @GetMapping("/{pinId}/meetings")
    public ApiResponse<List<MeetingResponse>> getMeetings(@UserId Long userId,
                                                          @PathVariable String teamId,
                                                          @PathVariable Long pinId,
                                                          @RequestParam(required = false) MCategory category) {
        return ApiResponse.success(SuccessMessage.OK, pinService.getMeetings(pinId, userId, category));
    }

    @GetMapping("/ranking")
    public ApiResponse<RankingResponse> getRankings(@PathVariable Long teamId) {
        return ApiResponse.success(SuccessMessage.OK, pinService.getRankings(teamId));

    }
}
