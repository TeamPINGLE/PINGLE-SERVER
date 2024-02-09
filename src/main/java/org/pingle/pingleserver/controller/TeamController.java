package org.pingle.pingleserver.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.controller.swagger.TeamApi;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.reponse.TeamNameDuplicatedResponse;
import org.pingle.pingleserver.dto.request.TeamRegisterRequest;
import org.pingle.pingleserver.dto.response.SelectedTeamResponse;
import org.pingle.pingleserver.dto.response.TeamRegisterResponse;
import org.pingle.pingleserver.dto.response.TeamSearchResultResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/teams")
@RequiredArgsConstructor
public class TeamController implements TeamApi {

    private final TeamService teamService;

    @GetMapping
    public ApiResponse<List<TeamSearchResultResponse>> searchTeams(@NotBlank @RequestParam String name){
        return ApiResponse.success(SuccessMessage.OK,teamService.searchTeams(name));
    }

    @GetMapping("/{teamId}")
    public ApiResponse<SelectedTeamResponse> getTeam(@PathVariable Long teamId){
        return ApiResponse.success(SuccessMessage.OK,teamService.getTeam(teamId));
    }

    @PostMapping("/{teamId}/register")
    public ApiResponse<TeamRegisterResponse> registerTeam(
            @UserId Long userId,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamRegisterRequest request){
        return ApiResponse.success(SuccessMessage.OK, teamService.registerTeam(userId, teamId, request));
    }

    @GetMapping("/check-name")
    public ApiResponse<TeamNameDuplicatedResponse> checkTeamName(@NotBlank @RequestParam String name){
        return ApiResponse.success(SuccessMessage.OK, teamService.checkTeamName(name));
    }
}
