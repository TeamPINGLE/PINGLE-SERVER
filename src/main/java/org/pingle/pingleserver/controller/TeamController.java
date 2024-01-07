package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.response.TeamSearchResultResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    @GetMapping
    public ApiResponse<List<TeamSearchResultResponse>> searchTeams(@RequestParam String name){
        return ApiResponse.success(SuccessMessage.OK,teamService.searchTeams(name));
    }

}
