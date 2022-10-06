package ru.nsu.ccfit.gulyaev.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import ru.nsu.ccfit.gulyaev.utils.LocationContext;
import ru.nsu.ccfit.gulyaev.utils.Pair;

import java.io.IOException;
import java.util.Scanner;

public class GetWeatherByCordsService implements Runnable{
    private final LocationContext context;

    private static final String apiKey = "ae40c5a9da0f6f93ff2d77758cc2e347";

    public GetWeatherByCordsService(LocationContext context){
        this.context = context;
    }


    private void getWeather(JsonObject jsonObj){
        JsonObject weather = (JsonObject) jsonObj.getAsJsonArray("weather").get(0);
        JsonObject main = (JsonObject) jsonObj.get("main");

        String description = String.valueOf(weather.get("description")).replaceAll("\"","");
        String temp = String.valueOf(main.get("temp")).replaceAll("\"","");
        String feelsLike = String.valueOf(main.get("feels_like")).replaceAll("\"","");

        System.out.println("Description: " + description + "\nTemp: " + temp + "\nFeels like: " + feelsLike + "\n");
    }

    private void makeRequest(Pair pair) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?lat=" + pair.getFirst() +
                        "&lon=" + pair.getSecond() +
                        "&appid=" + apiKey +
                        "&units=metric")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        JsonObject jsonObj = new Gson().fromJson(response.body().string(), JsonObject.class);

        this.getWeather(jsonObj);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while(true){
            int index = Integer.parseInt(scanner.next());

            boolean isCorrectIndex = this.context.setMainPair(index);
            if(isCorrectIndex){
                break;
            }
            System.out.println("Invalid location id");
        }
        try {
            this.makeRequest(this.context.getMainPair());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
