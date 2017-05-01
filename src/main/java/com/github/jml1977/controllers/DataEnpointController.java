package com.github.jml1977.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataEnpointController {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public String data() {
		StringBuilder cheapJson = new StringBuilder();
		Set<String> keys = stringRedisTemplate.keys("??????");
		Collection valueKeys = Arrays.asList("lat", "lng");
		boolean first = true;
		for (String key : keys) {
			List values = stringRedisTemplate.opsForHash().multiGet(key, valueKeys);
			String thisLine = String.format("{\"key\":\"%s\", \"lat\":\"%s\", \"lng\":\"%s\"}%n", key, values.get(0),
					values.get(1));
			if (first) {
				first = false;
			} else {
				cheapJson.append(",");
			}
			cheapJson.append(thisLine);
		}
		return "{\"data\": [" + cheapJson.toString() + "] }";
	}

}
