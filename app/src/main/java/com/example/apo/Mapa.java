package com.example.apo;

import android.os.Bundle;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.util.GeoPoint;

public class Mapa extends Fragment {
    private MapView map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Carrega as configurações do osmdroid
        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));

        // Infla o layout do fragmento
        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
        map = rootView.findViewById(R.id.mapa);

        // Configurações do mapa
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Definindo a localização inicial do mapa
        GeoPoint startPoint = new GeoPoint(-23.5505, -46.6333); // São Paulo
        map.getController().setZoom(15);
        map.getController().setCenter(startPoint);

        // Adicionando um marcador
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setTitle("São Paulo");
        map.getOverlays().add(marker);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Atualiza o mapa ao voltar para o fragmento
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Pausa o mapa ao sair do fragmento
        map.onPause();
    }
}