package ru.nsu.ccfit.gulyaev.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import ru.nsu.ccfit.gulyaev.utils.LocationContext;

import java.io.IOException;

public class GetPlacesByCordsService implements Runnable{
    private static final int RADIUS = 700;
    private static final String apiKey = "5ae2e3f221c38a28845f05b64e2e26237a7e7d529809cf323df46cb5";

    private final LocationContext context;

    public GetPlacesByCordsService(LocationContext context){
        this.context = context;
    }

    private void makeRequest() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.opentripmap.com/0.1/en/places/radius?radius=" + RADIUS +
                        "&lon=" + this.context.getMainPair().getSecond() +
                        "&lat=" + this.context.getMainPair().getFirst() +
                        "&apikey=" + apiKey)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        JsonObject jsonObj = new Gson().fromJson(response.body().string(), JsonObject.class);
        JsonArray features = jsonObj.getAsJsonArray("features");

        this.context.addLocationIds(features);
    }

    @Override
    public void run() {
        try {
            this.makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
