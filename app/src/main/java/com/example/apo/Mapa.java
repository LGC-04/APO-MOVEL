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

    // Declarar o ActivityResultLauncher
    private ActivityResultLauncher<Intent> qrCodeLauncher;

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

        // Restaura a localização se houver estado salvo
        if (savedInstanceState != null) {
            lat = savedInstanceState.getString("latitude", lat);
            lon = savedInstanceState.getString("longitude", lon);
            cidade = savedInstanceState.getString("cidade", cidade);
        }

        // Definindo a localização inicial do mapa
        updateMap(Double.parseDouble(lat), Double.parseDouble(lon));

        // Inicializa o ActivityResultLauncher
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

        // Configurar o FloatingActionButton
        view.findViewById(R.id.fab).setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0);  // Use a câmera padrão
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            qrCodeLauncher.launch(integrator.createScanIntent()); // Use o launcher para iniciar a atividade
        });

        return view;
    }

    private void updateMap(double latitude, double longitude) {
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        map.getController().setZoom(15);
        map.getController().setCenter(startPoint);

        // Remover todos os marcadores existentes (opcional)
        map.getOverlays().clear();

        // Adicionando um novo marcador
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setTitle(cidade); // Usar a variável cidade atualizada
        map.getOverlays().add(marker);

        // Atualiza o mapa para refletir as mudanças
        map.invalidate();
    }

    public void atualizarLocalizacao(String novaCidade, String novaLatitude, String novaLongitude) {
        this.cidade = novaCidade;
        this.lat = novaLatitude; // Atualiza a latitude
        this.lon = novaLongitude; // Atualiza a longitude

        updateMap(Double.parseDouble(lat), Double.parseDouble(lon)); // Atualiza o mapa com as novas coordenadas
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

        // Salvar o estado atual das variáveis de localização e cidade
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

            // Atualizando as variáveis na classe de mapa
            atualizarLocalizacao(cidade, latitude, longitude);

            // Usando o ViewModel para atualizar o WOEID
            WeatherViewModel weatherViewModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
            weatherViewModel.setWoeid(woeid); // Atualiza o WOEID no ViewModel

        } catch (JSONException e) {
            Toast.makeText(getContext(), "Erro ao processar o QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    }