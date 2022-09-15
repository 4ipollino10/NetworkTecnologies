package ru.fit.ccfit.gulyaev;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String addr = "FF0E:0:0:0:0:0:0:101";

        Context context = new Context();

        MessageReceiver messageReceiver = new MessageReceiver(addr, context);
        MessageSender messageSender = new MessageSender(addr);

        Thread receiverThread = new Thread(messageReceiver);
        Thread senderThread = new Thread(messageSender);

        senderThread.start();
        receiverThread.start();

        UsersChecker usersChecker = new UsersChecker(context);

        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(usersChecker, 0, 4000);

        String endCommand = "";
        while(!Objects.equals(endCommand, "e")){
            endCommand = scanner.next();
        }
        timer.cancel();

        messageReceiver.changeThreadMode();
        messageSender.changeThreadMode();

        receiverThread.join();
        senderThread.join();
    }
}
