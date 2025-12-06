package com.iskander.manager;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URL url;
    private String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient(URL url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
        register();
    }

    private String register(){
        try {
            URI uri = URI.create(url.toString() + "/register()");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                return response.body();
            }else{
                throw new RuntimeException("Ошибка регистрции " + response.statusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void put(String key,String json){
        try {
            URI uri = URI.create(url.toString() + "/save" + key + "?API_TOKEN=" + apiToken);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() < 200 || response.statusCode() >= 300){
                throw new RuntimeException("Ошибка сохранения. "+ response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public String load(String key){
            try {
                URI uri = URI.create(url.toString() + "/load/" + key + "?API_TOKEN=" + apiToken);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request,
                        HttpResponse.BodyHandlers.ofString());

                if(response.statusCode() == 200){
                    return response.body();
                }else if(response.statusCode() == 404){
                    return null;
                }else{
                    throw new RuntimeException("Ошиька загрузки. "+ response.statusCode());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
    public String getApiToken() {
        return apiToken;
    }
}
