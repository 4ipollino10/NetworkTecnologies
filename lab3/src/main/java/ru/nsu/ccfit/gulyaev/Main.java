package ru.nsu.ccfit.gulyaev;

import ru.nsu.ccfit.gulyaev.service.GetCordsByLocationService;
import ru.nsu.ccfit.gulyaev.service.GetPlaceDescriptionByIDService;
import ru.nsu.ccfit.gulyaev.service.GetPlacesByCordsService;
import ru.nsu.ccfit.gulyaev.service.GetWeatherByCordsService;
import ru.nsu.ccfit.gulyaev.utils.LocationContext;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your location here:");

        LocationContext context = new LocationContext();

        String location = scanner.next();

        CompletableFuture<Void> locationRequest = CompletableFuture
                .runAsync(new GetCordsByLocationService(location, context))
                .thenRun(new GetWeatherByCordsService(context))
                .thenRun(new GetPlacesByCordsService(context))
                .thenRun(new GetPlaceDescriptionByIDService(context));


        locationRequest.get();

        context.clearUp();
    }
}
