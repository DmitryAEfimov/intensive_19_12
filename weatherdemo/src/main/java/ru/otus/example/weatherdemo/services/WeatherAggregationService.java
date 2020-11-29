package ru.otus.example.weatherdemo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.example.weatherdemo.models.Weather;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service("weatherService")
@Slf4j
public class WeatherAggregationService implements WeatherServiceAggregation {
	private final List<WeatherService> weatherServices;
	private final WeatherCache weatherCache;

	public WeatherAggregationService(List<WeatherService> weatherServices, WeatherCache weatherCache) {
		this.weatherServices = weatherServices;
		this.weatherCache = weatherCache;
	}

	@Override
	public List<Weather> getWeather() {
		return weatherCache.getValue().orElseGet(() -> {
			var weather = doRequest();
			weatherCache.putValue(weather);
			return weather;
		});
	}

	private List<Weather> doRequest() {
		CompletableFuture[] weatherFutures = new CompletableFuture[weatherServices.size()];

		int idx = 0;
		for (WeatherService weatherService : weatherServices) {
			weatherFutures[idx++] = CompletableFuture.supplyAsync(weatherService::getWeather);
		}

		try {
			CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(weatherFutures);
			return (List<Weather>) combinedFuture.get(30, TimeUnit.SECONDS);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return Collections.emptyList();
		}
	}
}
