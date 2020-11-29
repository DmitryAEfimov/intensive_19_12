package ru.otus.example.weatherdemo.services;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.example.weatherdemo.models.Weather;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class YandexWeatherService implements WeatherService {
	@Value("${app.city-name}")
	private String cityName;

	@Override
	public List<Weather> getWeather() {
		try {
			log.info("Yandex performing request...");
			Thread.sleep(1000);
			Document doc = Jsoup.connect(String.format("https://yandex.ru/pogoda/%s", cityName)).get();
			Element tempValue = doc.selectFirst(".temp__value");
			log.info("Yandex request done.");
			return List.of(new Weather("YandexWeather", cityName, tempValue.text()));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return Collections.emptyList();
		}
	}
}
