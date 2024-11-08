package com.example.apo;

public class WeatherData {
    private String cidade;
    private String data;
    private String temperatura;
    private String humidade;
    private String cloudiness;
    private String chuva;
    private String vento;
    private String descricao;

    // Construtor
    public WeatherData(String cidade, String data, String temperatura, String humidade,
                       String cloudiness, String chuva, String vento, String descricao) {
        this.cidade = cidade;
        this.data = data;
        this.temperatura = temperatura;
        this.humidade = humidade;
        this.cloudiness = cloudiness;
        this.chuva = chuva;
        this.vento = vento;
        this.descricao = descricao;
    }

    // Getters
    public String getCidade() { return cidade; }
    public String getData() { return data; }
    public String getTemperatura() { return temperatura; }
    public String getHumidade() { return humidade; }
    public String getCloudiness() { return cloudiness; }
    public String getChuva() { return chuva; }
    public String getVento() { return vento; }
    public String getDescricao() { return descricao; }
}
