package com.wipro.weatherforcast.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.wipro.weatherforcast.interfaces.ForcastApi;
import com.wipro.weatherforcast.interfaces.ForcastCallbackListener;
import com.wipro.weatherforcast.models.OpenWeather5DayModel;
import com.wipro.weatherforcast.models.OpenWeatherModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wipro.weatherforcast.constants.ProjectConstants.BASE_URL_OPEN_WEATHER;


public class WeatherConditions {


    public static String error_msg = "Something went wrong";


    private static ForcastApi mForcastApi;

    /**
     * Fetch the current day weather data.
     * @auther jayeshhiray
     * @param city
     * @param appId
     * @param listener
     */
    public static void getOpenWeatherToday(String city, String appId, ForcastCallbackListener listener) {

        mForcastApi = ForcastApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(ForcastApi.class);
        Call<OpenWeatherModel> resForgotPasswordCall = mForcastApi.getOpenWeatherData(appId, city);
        resForgotPasswordCall.enqueue(new Callback<OpenWeatherModel>() {
            @Override
            public void onResponse(Call<OpenWeatherModel> call, Response<OpenWeatherModel> response) {
                if (response.body() != null) {
                    if (listener != null)
                        listener.getWeatherData(response.body(), true, "");
                    //Fetch upcomming 5days data
                    getOpenWeatherData5Days(city, appId, listener);

                }
            }

            @Override
            public void onFailure(Call<OpenWeatherModel> call, Throwable t) {
                if (listener != null)
                    listener.getWeatherData(null, false, t.getMessage());
            }
        });
    }

    /**
     * Fetch the upcoming 5 days weather data.
     *
     * @param city
     * @param appId
     * @param listener
     */
    public static void getOpenWeatherData5Days(String city, String appId, ForcastCallbackListener listener) {

        mForcastApi = ForcastApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(ForcastApi.class);
        Call<OpenWeather5DayModel> call = mForcastApi.getOpenWeatherData5days(appId, city);
        call.enqueue(new Callback<OpenWeather5DayModel>() {
            @Override
            public void onResponse(Call<OpenWeather5DayModel> call, Response<OpenWeather5DayModel> response) {
                if (response.body() != null) {
                    if (listener != null)
                        listener.getWeatherData(response.body(), true, "");
                }
            }

            @Override
            public void onFailure(Call<OpenWeather5DayModel> call, Throwable t) {
                if (listener != null)
                    listener.getWeatherData(null, false, t.getMessage());
            }
        });
    }

    /**
     * Hide keyboard once weathers data load.
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Get today day.
     *
     * @return date
     */
    public static String TodayDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return "Today, " + dayOfTheWeek;
    }

}

