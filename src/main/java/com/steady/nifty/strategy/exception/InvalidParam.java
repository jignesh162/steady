package com.steady.nifty.strategy.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidParam {
    @Schema(description = "Service name", example = "EpicServiceImpl")
    private String object;
    @Schema(description = "Field name.", example = "epicId")
    private String field;
    @Schema(description = "Value that was rejected in validation.", example = "12345678901")
    private Object rejectedValue;
    @Schema(description = "Validation error message.", example = "size must be between 0 and 10")
    private String message;
}
