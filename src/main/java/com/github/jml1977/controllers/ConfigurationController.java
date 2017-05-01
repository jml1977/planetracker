package com.github.jml1977.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigurationController {
	
	@Value("${GOOGLE_MAPS_API_KEY}") // read from environment variable
	private String maps;
	
	@RequestMapping("/gmaps")
	public String gmaps() {
		return maps;
	}
}
