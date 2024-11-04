package com.example.apo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<WeatherData> weatherList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityName;
        public TextView temperature;
        public TextView weatherDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.tvCityName);
            temperature = itemView.findViewById(R.id.tvTemperature);
            weatherDescription = itemView.findViewById(R.id.tvWeatherDescription);
        }
    }

    public WeatherAdapter(List<WeatherData> weatherList) {
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherData currentWeather = weatherList.get(position);
        holder.cityName.setText(currentWeather.getCityName());
        holder.temperature.setText(currentWeather.getTemperature() + " °C");
        holder.weatherDescription.setText(currentWeather.getWeatherDescription());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
