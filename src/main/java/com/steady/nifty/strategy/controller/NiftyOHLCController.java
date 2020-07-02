package com.steady.nifty.strategy.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonView;
import com.steady.nifty.strategy.entity.NiftyOHLC;
import com.steady.nifty.strategy.entity.Views;

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
@Tag(name = "niftyohlc", description = "NIFTY's daily OHLC API")
@RequestMapping(value = "/niftyohlc")
public interface NiftyOHLCController {
        @Operation(summary = "Gets all OHLC per day for nifty", description = "Gets all OHLC details per day for nifty", tags = {
                        "niftyohlc" }, security = { @SecurityRequirement(name = "bearerToken") })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NiftyOHLC.class)))) })
        @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
        public Iterable<NiftyOHLC> getAll();

        @Operation(summary = "Calculaye OHLC for nifty", description = "Calculaye OHLC for nifty per day", tags = {
                        "niftyohlc" }, security = { @SecurityRequirement(name = "bearerToken") })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NiftyOHLC.class)))) })
        @GetMapping(value = "/calculateohlc", produces = MediaType.APPLICATION_JSON_VALUE)
        @JsonView(Views.Default.class)
        public boolean calculateOHLCDataFromNifty();

        @Operation(summary = "Calculaye expiry to expiry weekly OHLC for nifty", description = "Calculaye expiry to expiry weekly OHLC for nifty", tags = {
                "niftyohlc" }, security = { @SecurityRequirement(name = "bearerToken") })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NiftyOHLC.class)))) })
        @GetMapping(value = "/calculateohlc1w", produces = MediaType.APPLICATION_JSON_VALUE)
        @JsonView(Views.Default.class)
        public boolean calculateWeeklyOHLCDataFromNifty();

        @Operation(summary = "Calculaye expiry to expiry monthly OHLC for nifty", description = "Calculaye expiry to expiry monthly OHLC for nifty", tags = {
                "niftyohlc" }, security = { @SecurityRequirement(name = "bearerToken") })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NiftyOHLC.class)))) })
        @GetMapping(value = "/calculateohlc1m", produces = MediaType.APPLICATION_JSON_VALUE)
        @JsonView(Views.Default.class)
        public boolean calculate1MonthE2ExpiryOHLCDataFromNifty();

        @Operation(summary = "Calculaye expiry to expiry 2 months OHLC for nifty", description = "Calculaye expiry to expiry 2 months OHLC for nifty", tags = {
                "niftyohlc" }, security = { @SecurityRequirement(name = "bearerToken") })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NiftyOHLC.class)))) })
        @GetMapping(value = "/calculateohlc2mw", produces = MediaType.APPLICATION_JSON_VALUE)
        @JsonView(Views.Default.class)
        public boolean calculate2MonthE2ExpiryOHLCDataFromNifty();
}
