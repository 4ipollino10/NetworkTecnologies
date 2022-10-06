package ru.nsu.ccfit.gulyaev.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import ru.nsu.ccfit.gulyaev.utils.LocationContext;

import java.io.IOException;
import java.util.List;


public class GetPlaceDescriptionByIDService implements Runnable {

    private final LocationContext context;

    private static final String apiKey = "5ae2e3f221c38a28845f05b64e2e26237a7e7d529809cf323df46cb5";

    public GetPlaceDescriptionByIDService(LocationContext context){
        this.context = context;
    }

    public void makeRequest() throws IOException {
        List<String> ids = this.context.getIdsList();

        for(String id : ids){
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.opentripmap.com/0.1/en/places/xid/" + id +
                            "?apikey=" + apiKey )
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            JsonObject jsonObj = new Gson().fromJson(response.body().string(), JsonObject.class);

            String name = String.valueOf(jsonObj.get("name")).replaceAll("\"","");
            String rate = String.valueOf(jsonObj.get("rate")).replaceAll("\"","");
            String otm = String.valueOf(jsonObj.get("otm")).replaceAll("\"","");

            String locationDescription = "Name: " + name + "\nRate: " + rate + "\nOtm: " + otm + "\n";

            System.out.println(locationDescription);

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
