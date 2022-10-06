package ru.nsu.ccfit.gulyaev.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationContext {
    private final HashMap<Integer, Pair> locationMap;

    private final List<String> idsList;

    private Pair mainPair;

    public LocationContext(){
        this.idsList = new ArrayList<>();
        this.locationMap = new HashMap<>();
        for(int i = 1; i < 6; ++i){
            locationMap.put(i, null);
        }
    }

    public void addLocationIds(JsonArray features){
        for (JsonElement element : features) {

            JsonObject object = (JsonObject)element;
            JsonObject properties = (JsonObject) object.get("properties");

            String xid = String.valueOf(properties.get("xid")).replaceAll("\"","");
            this.idsList.add(xid);
        }
    }

    public void addLocation(double lat, double lng, int index){
        locationMap.put(index, new Pair(lat, lng));
    }

    public boolean setMainPair(int key){
        if(this.locationMap.containsKey(key)){
            mainPair = this.locationMap.get(key);
            locationMap.clear();
            return true;
        }
        return false;
    }

    public Pair getMainPair(){
        return this.mainPair;
    }

    public List<String> getIdsList(){
        return this.idsList;
    }

    public void clearUp(){
        this.idsList.clear();
    }
}
