package ru.otus.example.weatherdemo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.example.weatherdemo.models.Weather;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WeatherCache implements Cache<List<Weather>> {
	private List<Weather> weatherCached;

	public WeatherCache() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(this::resetCache, 0, 10, TimeUnit.SECONDS);
	}

	private synchronized void resetCache() {
		log.info("resetCache");
		weatherCached = null;
	}

	@Override
	public synchronized void putValue(List<Weather> value) {
		this.weatherCached = value;

	}

	@Override
	public synchronized Optional<List<Weather>> getValue() {
		return Optional.ofNullable(weatherCached);
	}

	@Override
	public synchronized List<Weather> getValueNull() {
		return weatherCached;
	}
}


