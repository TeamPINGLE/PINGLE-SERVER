package org.pingle.pingleserver.controller.swagger;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.reponse.LocationResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "외부 API", description = "외부(네이버) API")
public interface OpenApi {
    @Operation(summary = "지역 정보 검색", description = "네이버 API를 이용해 지역 정보를 조회한다.")
    ApiResponse<List<LocationResponse>> getLocationInfo(@RequestParam(name = "search") String location);
}
