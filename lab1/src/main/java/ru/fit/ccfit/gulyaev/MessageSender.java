package ru.fit.ccfit.gulyaev;

import java.io.IOException;
import java.net.*;

import static java.lang.Thread.sleep;

public class MessageSender implements Runnable {
    private static final int PORT = 2134;
    private final MulticastSocket sendSocket = new MulticastSocket(PORT);
    private final InetSocketAddress group;
    private boolean isRunning = true;
    private final NetworkInterface networkInterface;

    public MessageSender(String addr) throws IOException {

        InetAddress mcastaddr = InetAddress.getByName(addr);
        this.group = new InetSocketAddress(mcastaddr, PORT);
        this.networkInterface = NetworkInterface.getByName("em0");


        sendSocket.joinGroup(group, networkInterface);

    }

    @Override
    public void run() {
        String message = "hello brov";
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), this.group);
        while(this.isRunning){
            try{
                this.sendSocket.send(packet);
            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("Sender sent message to " + packet.getAddress());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            this.sendSocket.leaveGroup(this.group, this.networkInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendSocket.close();
    }

    public void changeThreadMode(){
        this.isRunning = false;
    }
}
