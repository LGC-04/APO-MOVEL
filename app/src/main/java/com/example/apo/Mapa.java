package com.example.apo;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

public class Mapa extends Fragment {
    private MapView map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Carrega as configurações do osmdroid
        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));

        // Infla o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        map = view.findViewById(R.id.mapa);

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

        // Configurar o FloatingActionButton
        view.findViewById(R.id.fab).setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0);  // Use a câmera padrão
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();
                Toast.makeText(getContext(), "Scanned: " + scannedData, Toast.LENGTH_SHORT).show();
                // Aqui você pode processar o conteúdo do QR Code e mudar a localização do marcador
                // Exemplo: Atualizar coordenadas com base no conteúdo escaneado.
            } else {
                Toast.makeText(getContext(), "Scan cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}