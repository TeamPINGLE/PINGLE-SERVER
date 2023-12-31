package org.pingle.pingleserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.request.TeamRegisterRequest;
import org.pingle.pingleserver.dto.response.SelectedTeamResponse;
import org.pingle.pingleserver.dto.response.TeamRegistResponse;
import org.pingle.pingleserver.dto.response.TeamSearchResultResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ApiResponse<List<TeamSearchResultResponse>> searchTeams(@RequestParam String name){
        return ApiResponse.success(SuccessMessage.OK,teamService.searchTeams(name));
    }

    @GetMapping("/{teamId}")
    public ApiResponse<SelectedTeamResponse> getTeam(@PathVariable Long teamId){
        return ApiResponse.success(SuccessMessage.OK,teamService.getTeam(teamId));
    }

    @PostMapping("/{teamId}/register")
    public ApiResponse<TeamRegistResponse> registerTeam(
            @UserId Long userId,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamRegisterRequest request){
        return ApiResponse.success(SuccessMessage.OK, teamService.registTeam(userId, teamId, request));
    }
}
