package com.wipro.weatherforcast.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wipro.weatherforcast.R;
import com.wipro.weatherforcast.models.OpenWeather5DayModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class used as adapter for Recycler View.
 * @author jayeshhiray
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    Context context;
    private String BASE_URL_IMAGE = "http://openweathermap.org/img/wn/";
    HashMap<String, ArrayList<OpenWeather5DayModel.Main>> weatherList = new HashMap<>();
    List<OpenWeather5DayModel.List> mWeatherArrayList = new ArrayList();

    public RecyclerAdapter(Context context, OpenWeather5DayModel weatherList) {

        try {
            this.context = context;
            this.weatherList = weatherList.getMinMaxTemp();
            this.mWeatherArrayList = weatherList.getList();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Object[] keyset = weatherList.keySet().toArray();
        Arrays.sort(keyset);
        holder.tvWeatherDate.setText(keyset[position].toString());
        holder.tvTempMax.setText(String.valueOf(weatherList.get(String.valueOf(keyset[position])).get(weatherList.get(String.valueOf(keyset[position])).size() - 1).getTemp()) + "\u2103");
        holder.tvWeatherDesc.setText(mWeatherArrayList.get(position).getWeather().get(0).getDescription().toString());
        Glide.with(context).load(BASE_URL_IMAGE + mWeatherArrayList.get(position).getWeather().get(0).getIcon() + "@2x.png").into(holder.ivWeatherIcon);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_temp_max)
        TextView tvTempMax;
        @BindView(R.id.tv_weather_date)
        TextView tvWeatherDate;
        @BindView(R.id.tv_weather_desc)
        TextView tvWeatherDesc;
        @BindView(R.id.iv_weather_icon)
        ImageView ivWeatherIcon;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
