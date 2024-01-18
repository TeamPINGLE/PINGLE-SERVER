package org.pingle.pingleserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.request.TeamRegisterRequest;
import org.pingle.pingleserver.dto.response.SelectedTeamResponse;
import org.pingle.pingleserver.dto.response.TeamRegisterResponse;
import org.pingle.pingleserver.dto.response.TeamSearchResultResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Team", description = "팀 관련 API")
public interface TeamApi {
    @Operation(summary = "팀 검색")
    ApiResponse<List<TeamSearchResultResponse>> searchTeams(@NotBlank @RequestParam String name);

    @Operation(summary = "팀 조회")
    ApiResponse<SelectedTeamResponse> getTeam(@PathVariable Long teamId);

    @Operation(summary = "팀 가입")
    ApiResponse<TeamRegisterResponse> registerTeam(
            @UserId Long userId,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamRegisterRequest request);

}
