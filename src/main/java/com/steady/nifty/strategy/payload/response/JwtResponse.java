package com.steady.nifty.strategy.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
	private String token;
	private final String type = "Bearer";
	private Long id;
	private String username;
	private List<String> roles;
}
