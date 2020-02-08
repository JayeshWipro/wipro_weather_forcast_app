package com.wipro.weatherforcast.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.wipro.weatherforcast.utils.ForcastApiService;
import com.wipro.weatherforcast.R;
import com.wipro.weatherforcast.interfaces.ForcastApi;
import com.wipro.weatherforcast.models.LocationSearchModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author jayeshhiray
 * Class used for the as a AutoComplete Adapter.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

    private List<LocationSearchModel> mResultList = new ArrayList<>();
    private Context mContext;
    private String OPEN_WEATHER_APP_ID;
    private String BASE_URL_OPEN_WEATHER = "http://api.openweathermap.org/data/2.5/";

    public AutoCompleteAdapter(Context mContext, String appId) {
        this.mContext = mContext;
        OPEN_WEATHER_APP_ID = appId;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Object getItem(int i) {
        return mResultList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view = inflater.inflate(R.layout.row_suggestions, viewGroup, false);
            }
        }

        if (view != null) {
            ((TextView) view.findViewById(R.id.tv_city_suggestion)).setText(mResultList.get(i).getLocalizedName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    ForcastApi weatherApi = ForcastApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(ForcastApi.class);
                    Call<List<LocationSearchModel>> call = weatherApi.getOpenWeatherCities(OPEN_WEATHER_APP_ID, charSequence.toString());
                    call.enqueue(new Callback<List<LocationSearchModel>>() {
                        @Override
                        public void onResponse(Call<List<LocationSearchModel>> call, Response<List<LocationSearchModel>> response) {
                            if (response.body() != null) {
                                if (!response.body().isEmpty()) {
                                    mResultList.clear();
                                    mResultList.addAll(response.body());
                                    filterResults.values = mResultList;
                                    filterResults.count = mResultList.size();
                                    publishResults(charSequence, filterResults);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<LocationSearchModel>> call, Throwable t) {
                            mResultList.clear();
                            publishResults(charSequence, filterResults);

                        }
                    });
                }
                filterResults.values = mResultList;
                filterResults.count = mResultList.size();
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();
                } else
                    notifyDataSetInvalidated();
            }
        };
        return filter;
    }
}
