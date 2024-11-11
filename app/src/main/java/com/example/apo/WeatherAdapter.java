package com.example.apo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<WeatherData> weatherList;

    public WeatherAdapter(List<WeatherData> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherData weatherData = weatherList.get(position);

        if (position == 0) {
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.cityTextView.setText(weatherData.getCidade());
        } else {
            holder.cityTextView.setVisibility(View.GONE);
        }

        holder.dateTextView.setText(weatherData.getData());
        holder.tempTextView.setText(weatherData.getTemperatura());
        holder.descriptionTextView.setText(weatherData.getDescricao());

        holder.humidityTextView.setText("Umidade: " + weatherData.getHumidade());
        holder.cloudinessTextView.setText("Nuvens: " + weatherData.getCloudiness());
        holder.rainTextView.setText("Chuva: " + weatherData.getChuva());
        holder.windSpeedTextView.setText("Vento: " + weatherData.getVento());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityTextView;
        TextView dateTextView;
        TextView descriptionTextView;
        TextView tempTextView;
        TextView humidityTextView;
        TextView cloudinessTextView;
        TextView rainTextView;
        TextView windSpeedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.city_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            tempTextView = itemView.findViewById(R.id.temp_text_view);
            humidityTextView = itemView.findViewById(R.id.humidity_text_view);
            cloudinessTextView = itemView.findViewById(R.id.cloudiness_text_view);
            rainTextView = itemView.findViewById(R.id.rain_text_view);
            windSpeedTextView = itemView.findViewById(R.id.wind_speed_text_view);
        }
    }
}