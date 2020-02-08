package com.wipro.weatherforcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wipro.weatherforcast.adapters.AutoCompleteAdapter;
import com.wipro.weatherforcast.adapters.RecyclerAdapter;
import com.wipro.weatherforcast.interfaces.ForcastCallbackListener;
import com.wipro.weatherforcast.models.LocationSearchModel;
import com.wipro.weatherforcast.models.OpenWeather5DayModel;
import com.wipro.weatherforcast.models.OpenWeatherModel;
import com.wipro.weatherforcast.utils.WeatherConditions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author jayeshhiray
 * This main class which is call from AndroidManifest.xml
 */

public class MainActivity extends AppCompatActivity implements ForcastCallbackListener {

    // Initialised the all xlm components
    @BindView(R.id.tv_city_country)
    TextView tvCountry;
    @BindView(R.id.iv_weather_icon)
    ImageView ivWeatherIcon;
    @BindView(R.id.week_weather_data)
    RecyclerView rvWeekWeather;
    @BindView(R.id.et_city_name)
    AutoCompleteTextView etCityName;
    @BindView(R.id.btn_search)
    Button btnSearchWeather;
    @BindView(R.id.tv_today_day)
    TextView tvToday;
    @BindView(R.id.btn_get_5_days_weather)
    TextView btn_five_days;
    @BindView(R.id.tv_temp)
    TextView tvWeatherTemp;

    //Key used to fetch the weather data
    String OPEN_WEATHER_APP_ID = "b317aca2e83ad16e219ff2283ca837d5";
    LocationSearchModel mLocationSearchModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialised ButterKnife-Bind Android views and callbacks to fields and methods
        ButterKnife.bind(this);
        etCityName.setThreshold(2);
        etCityName.setAdapter(new AutoCompleteAdapter(MainActivity.this, OPEN_WEATHER_APP_ID));
        rvWeekWeather.setLayoutManager(new LinearLayoutManager(this));
        btnSearchWeather.setOnClickListener(view -> WeatherConditions.getOpenWeatherToday(etCityName.getText().toString(), OPEN_WEATHER_APP_ID, MainActivity.this));
        etCityName.setOnItemClickListener((adapterView, view, i, l) -> {

            mLocationSearchModel = (LocationSearchModel) adapterView.getAdapter().getItem(i);

            etCityName.setText(mLocationSearchModel.getLocalizedName());
        });


    }

    /**
     * Fetch weather data from using open weather apis.
     * @param weatherModel
     * @param success
     * @param errorMsg
     */
    @Override
    public void getWeatherData(Object weatherModel, Boolean success, String errorMsg) {
        if (success) {

            if (weatherModel instanceof OpenWeatherModel) {

                OpenWeatherModel openWeatherModel = (OpenWeatherModel) weatherModel;
                tvToday.setText(WeatherConditions.TodayDay());
                tvCountry.setText(openWeatherModel.getName() + ", " + openWeatherModel.getSys().getCountry());
                tvWeatherTemp.setText(String.valueOf(openWeatherModel.getMain().getTemp()) + "\u2103");
                Glide.with(MainActivity.this)
                        .load("http://openweathermap.org/img/wn/" + openWeatherModel.getWeather().get(0).getIcon() + "@2x.png")
                        .into(ivWeatherIcon);
                btn_five_days.setVisibility(View.VISIBLE);
                tvCountry.setVisibility(View.VISIBLE);
                WeatherConditions.hideKeyboard(MainActivity.this);

            } else if (weatherModel instanceof OpenWeather5DayModel) {

                OpenWeather5DayModel weatherBean = (OpenWeather5DayModel) weatherModel;

                rvWeekWeather.setAdapter(new RecyclerAdapter(MainActivity.this, weatherBean));//.getMinMaxTemp(),weatherBean.getList());


            }
        } else {
            Toast.makeText(this, WeatherConditions.error_msg, Toast.LENGTH_LONG).show();
        }
    }
}
