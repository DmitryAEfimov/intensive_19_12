package ru.otus.example.weatherdemo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.example.weatherdemo.models.Weather;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("weatherServiceNull")
@Slf4j
public class WeatherAggregationServiceNull implements WeatherServiceAggregation {
	private final List<WeatherService> weatherServices;
	private final WeatherCache weatherCache;
	private final BlockingQueue<List<Weather>> weatherQueue;
	private final ExecutorService executor;

	public WeatherAggregationServiceNull(List<WeatherService> weatherServices, WeatherCache weatherCache) {
		this.weatherServices = weatherServices;
		this.weatherCache = weatherCache;
		weatherQueue = new ArrayBlockingQueue<>(weatherServices.size());
		executor = Executors.newFixedThreadPool(weatherServices.size());
	}

	@Override
	public List<Weather> getWeather() {
		try {
			List<Weather> weatherList = weatherCache.getValueNull();
			if (weatherList == null) {
				var weather = doRequest();
				weatherCache.putValue(weather);
			}
			return weatherList;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return Collections.emptyList();
		}

	}

	private List<Weather> doRequest() throws InterruptedException {
		weatherQueue.clear();
		for (WeatherService weatherService : weatherServices) {
			executor.submit(() -> {
				var result = weatherQueue.offer(weatherService.getWeather());
				log.debug("weatherQueue.offer result:{}", result);
			});
		}
		return weatherQueue.take();
	}

	@PreDestroy
	public void destroy() {
		executor.shutdownNow();
	}
}
