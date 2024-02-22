package org.pingle.pingleserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.reponse.MeetingResponse;
import org.pingle.pingleserver.dto.reponse.PinResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Pin", description = "핀 API")
public interface PinApi {

    @Operation(summary = "핀 목록 조회", description = "핀 목록을 조회한다.")
    ApiResponse<List<PinResponse>> getPins(Long teamId, MCategory category, String q);

    @Operation(summary = "핀에 속한 미팅 목록 조회", description = "핀에 속한 미팅 목록을 조회한다.")
    ApiResponse<List<MeetingResponse>> getMeetings(@UserId Long userId,
                                                   @PathVariable String teamId,
                                                   @PathVariable Long pinId,
                                                   @Nullable @RequestParam MCategory category);
}
