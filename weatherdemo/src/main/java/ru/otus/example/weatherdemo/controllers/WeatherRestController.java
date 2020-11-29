package ru.otus.example.weatherdemo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.example.weatherdemo.models.Weather;
import ru.otus.example.weatherdemo.services.WeatherServiceAggregation;

import java.util.List;

@RestController
@Slf4j
public class WeatherRestController {
	private final WeatherServiceAggregation weatherService;
	private final WeatherServiceAggregation weatherServiceNull;

	public WeatherRestController(@Qualifier("weatherService") WeatherServiceAggregation weatherService,
			@Qualifier("weatherServiceNull") WeatherServiceAggregation weatherServiceNull) {
		this.weatherService = weatherService;
		this.weatherServiceNull = weatherServiceNull;
	}

	@GetMapping("api/weather")
	public List<Weather> getWeather() {
		log.info("get weather");
		return weatherService.getWeather();
	}

	@GetMapping("api/weatherNull")
	public List<Weather> getWeatherNull() {
		log.info("get weatherNull");
		return weatherServiceNull.getWeather();
	}

}
