package ru.nsu.ccfit.gulyaev.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import ru.nsu.ccfit.gulyaev.utils.LocationContext;

import java.io.IOException;

public class GetCordsByLocationService implements Runnable{
    private static final String apiKey = "0453f1cf-d1f0-40a4-b6a0-e4fb6fc39799";

    private final String location;
    private final LocationContext context;

    public GetCordsByLocationService(String location, LocationContext context){
        this.location = location;
        this.context = context;
    }

    private void makeRequest() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/geocode?q=" + this.location +
                        "&key=" + apiKey)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        JsonObject jsonObj = new Gson().fromJson(response.body().string(), JsonObject.class);
        JsonArray hits = jsonObj.getAsJsonArray("hits");

        int i = 1;

        for (JsonElement element : hits) {
            JsonObject object = (JsonObject)element;

            double lat = Double.parseDouble(String.valueOf(object.getAsJsonObject("point").get("lat")).replaceAll("\"",""));
            double lng = Double.parseDouble(String.valueOf(object.getAsJsonObject("point").get("lng")).replaceAll("\"",""));

            this.context.addLocation(lat, lng, i);

            String name = String.valueOf(object.get("name")).replaceAll("\"","");
            String country = String.valueOf(object.get("country")).replaceAll("\"","");
            String city = String.valueOf(object.get("city")).replaceAll("\"","");

            String location = name + " " + country + " " + city;

            System.out.println(location);
            System.out.println(lat + " " + lng + "\n");

            i++;

        }
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
