package com.steady.nifty.strategy.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.steady.nifty.strategy.payload.request.StrategyRequest;
import com.steady.nifty.strategy.payload.response.StrategyResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tag(name = "strategies", description = "NIFTY's daily OHLC API")
@RequestMapping(value = "/strategies")
public interface NiftyOptionsStrategyController {
        @Operation(summary = "Get strategy result for the different parameters", description = "Get strategy result for the different parameters", tags = {
                        "strategies" }, security = { @SecurityRequirement(name = "bearerToken") })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StrategyResponse.class)))) })
        @PostMapping(value = "/runStratagegy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public StrategyResponse runStratagegy(
                        @Valid @RequestBody StrategyRequest strategyRequest);
}
