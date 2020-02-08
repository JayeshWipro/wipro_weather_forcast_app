package com.wipro.weatherforcast.interfaces;


public interface ForcastCallbackListener<T> {
    <Y> void getWeatherData(Y weatherModel, Boolean success, String errorMsg);
}
