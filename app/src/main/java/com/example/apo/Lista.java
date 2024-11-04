package com.example.apo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        weatherDataList = new ArrayList<>();

        // Exemplo de WOEID para São Paulo (BR)
        String woeid = "455827"; // Substitua pelo código desejado

        loadWeatherData(woeid); // Carregar dados da API

        return view;    }

    private void loadWeatherData(String woeid) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.hgbrasil.com/weather?woeid=" + woeid; // URL da API

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

        // Extraindo dados relevantes da resposta JSON
        String cityName = jsonObject.getAsJsonObject("results").get("city").getAsString();
        String temperature = jsonObject.getAsJsonObject("results").get("temp").getAsString();
        String weatherDescription = jsonObject.getAsJsonObject("results").get("description").getAsString();

        WeatherData weatherData = new WeatherData(cityName, temperature, weatherDescription);

        // Atualizando a lista e notificar o adaptador
        weatherDataList.add(weatherData);

        getActivity().runOnUiThread(() -> {
            adapter = new WeatherAdapter(weatherDataList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }
}