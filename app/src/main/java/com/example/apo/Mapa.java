package com.example.apo;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

public class Mapa extends Fragment {
    private MapView map;
    private String lat = "-23.5505";
    private String lon = "-46.6333";
    private String cidade = "São Paulo";

    private ActivityResultLauncher<Intent> qrCodeLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));

        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        map = view.findViewById(R.id.mapa);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        if (savedInstanceState != null) {
            lat = savedInstanceState.getString("latitude", lat);
            lon = savedInstanceState.getString("longitude", lon);
            cidade = savedInstanceState.getString("cidade", cidade);
        }

        updateMap(Double.parseDouble(lat), Double.parseDouble(lon));

        qrCodeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), data);
                        if (intentResult != null && intentResult.getContents() != null) {
                            String scannedData = intentResult.getContents();
                            Toast.makeText(getContext(), "Scanned: " + scannedData, Toast.LENGTH_SHORT).show();
                            processScannedData(scannedData);
                        } else {
                            Toast.makeText(getContext(), "Scan cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        view.findViewById(R.id.fab).setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0);  // Use a câmera padrão
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            qrCodeLauncher.launch(integrator.createScanIntent());
        });

        return view;
    }

    private void updateMap(double latitude, double longitude) {
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        map.getController().setZoom(15);
        map.getController().setCenter(startPoint);

        map.getOverlays().clear();

        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setTitle(cidade);
        map.getOverlays().add(marker);

        map.invalidate();
    }

    public void atualizarLocalizacao(String novaCidade, String novaLatitude, String novaLongitude) {
        this.cidade = novaCidade;
        this.lat = novaLatitude;
        this.lon = novaLongitude;

        updateMap(Double.parseDouble(lat), Double.parseDouble(lon));
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("latitude", lat);
        outState.putString("longitude", lon);
        outState.putString("cidade", cidade);
    }

    private void processScannedData(String scannedData) {
        try {
            JSONObject jsonObject = new JSONObject(scannedData);
            String cidade = jsonObject.getString("cidade");
            String latitude = jsonObject.getString("latitude");
            String longitude = jsonObject.getString("longitude");
            String woeid = jsonObject.getString("woeid");

            atualizarLocalizacao(cidade, latitude, longitude);

            WeatherViewModel weatherViewModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
            weatherViewModel.setWoeid(woeid);

        } catch (JSONException e) {
            Toast.makeText(getContext(), "Erro ao processar o QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    }