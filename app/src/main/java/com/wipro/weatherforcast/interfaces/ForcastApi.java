package com.wipro.weatherforcast.interfaces;

import java.util.List;

import com.wipro.weatherforcast.models.LocationSearchModel;
import com.wipro.weatherforcast.models.OpenWeatherModel;
import com.wipro.weatherforcast.models.OpenWeather5DayModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ForcastApi {
    @GET("weather?type=accurate&units=metric")
    Call<OpenWeatherModel> getOpenWeatherData(@Query("appid") String appId, @Query("q") String query);

    @GET("forecast?type=accurate&units=metric")
    Call<OpenWeather5DayModel> getOpenWeatherData5days(@Query("appid") String appId, @Query("q") String query);

    @GET("locations/v1/cities/autocomplete")
    Call<List<LocationSearchModel>> getOpenWeatherCities(@Query("apikey") String appId, @Query("q") String query);
}
