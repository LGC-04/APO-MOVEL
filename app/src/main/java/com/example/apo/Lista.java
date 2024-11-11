package com.example.apo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Lista extends Fragment {

    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private List<WeatherData> weatherDataList;
    private WeatherViewModel weatherViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        weatherDataList = new ArrayList<>();


        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);


        weatherViewModel.getWoeid().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String novoWoeid) {
                if (novoWoeid != null) {
                    loadWeatherData(novoWoeid);
                }
            }
        });

        if (weatherViewModel.getWoeid().getValue() == null) {
            weatherViewModel.setWoeid("455827");
        }

        return view;
    }

    private void loadWeatherData(String woeid) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.hgbrasil.com/weather?woeid=" + woeid;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    parseWeatherData(responseBody);
                }
            }
        });
    }

    private void parseWeatherData(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject resultsObject = jsonObject.getAsJsonObject("results");


        String cityName = resultsObject.get("city").getAsString();


        JsonArray forecastArray = resultsObject.getAsJsonArray("forecast");

        weatherDataList.clear();

        for (int i = 0; i < forecastArray.size(); i++) {
            JsonObject forecastObject = forecastArray.get(i).getAsJsonObject();

            String date = forecastObject.get("date").getAsString();
            String temperature = forecastObject.get("max").getAsString() + "°C";
            String weatherDescription = forecastObject.get("description").getAsString();
            String humidity = forecastObject.get("humidity").getAsString() + "%";
            String cloudiness = forecastObject.get("cloudiness").getAsString() + "%";
            String rain = forecastObject.get("rain").getAsString() + " mm";
            String windSpeed = forecastObject.get("wind_speedy").getAsString();

            WeatherData weatherData = new WeatherData(cityName, date, temperature,
                    humidity, cloudiness, rain, windSpeed, weatherDescription);

            weatherDataList.add(weatherData);
        }

        getActivity().runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new WeatherAdapter(weatherDataList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        });
    }
}