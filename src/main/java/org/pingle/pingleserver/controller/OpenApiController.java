package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.controller.swagger.OpenApi;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.reponse.LocationResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.utils.NaverUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class OpenApiController implements OpenApi {

    private final NaverUtil naverUtil;

    @GetMapping("/location")
    public ApiResponse<List<LocationResponse>> getLocationInfo(@RequestParam(name = "search") String location) {
        return ApiResponse.success(SuccessMessage.OK, naverUtil.getLocationInfo(location));
    }
}
