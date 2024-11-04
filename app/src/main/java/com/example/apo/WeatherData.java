package com.example.apo;

public class WeatherData {
    private String cityName;
    private String temperature;
    private String weatherDescription;

    public WeatherData(String cityName, String temperature, String weatherDescription) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}
