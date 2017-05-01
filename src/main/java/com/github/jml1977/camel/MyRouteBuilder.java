package com.github.jml1977.camel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.redis.RedisConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyRouteBuilder extends RouteBuilder {
	
	@Value("${inputDataSource}")
	private String inputDataSource;

	@Override
	public void configure() throws Exception {

		Processor geoSetter = new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				final Message in = exchange.getIn();

				Object body = in.getBody();
				if (body instanceof List) {
					List contents = (List) ((List) body).get(0);
					Map<String, Object> geoData = new HashMap<>();
					geoData.put("lat", contents.get(14));
					geoData.put("lng", contents.get(15));

					in.setHeader("CamelRedis.Key", contents.get(4));
					in.setHeader("CamelRedis.Values", geoData);

				}
			}
		};

		// @formatter:off
		from(inputDataSource).to("direct:start");
		
		from("direct:start")
			.unmarshal().csv()
			.choice()
				.when(simple("${body.get(0).get(0)} == 'AIR'")).to("direct:AIR")
				.when(simple("${body.get(0).get(0)} == 'MSG'")).to("direct:MSG")
				.otherwise().setBody(simple("Not relevant!")).to("direct:other")
			.end();
		
		from("direct:AIR").stop();
		
		from("direct:MSG")
			.choice()
				.when(simple("${body.get(0).get(1)} == '3'")).to("direct:msgPOS")
				.otherwise().stop()
			.end();
		

		from("direct:msgPOS")
			.setHeader(RedisConstants.COMMAND, constant("HMSET"))
			.process(geoSetter)
			.to("spring-redis://localhost:6379?redisTemplate=#stringRedisTemplate")
			.setHeader(RedisConstants.COMMAND, constant("EXPIRE"))
			.setHeader("CamelRedis.Key", simple("${body.get(0).get(4)}"))
			.setHeader(RedisConstants.TIMEOUT, constant(5))
			.to("spring-redis://localhost:6379?redisTemplate=#stringRedisTemplate")
			.stop();
				
		from("direct:other").stop();
		
		// @formatter:on
	}
}
