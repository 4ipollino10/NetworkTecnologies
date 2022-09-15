package ru.fit.ccfit.gulyaev;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class MessageReceiver implements Runnable{

    private final Context context;

    private static final int PORT = 2134;
    private final MulticastSocket receiveSocket = new MulticastSocket(PORT);
    private final byte[] buff = new byte[256];
    private boolean isRunnable = true;
    private final InetSocketAddress group;
    private final NetworkInterface networkInterface;
    public MessageReceiver(String addr, Context context) throws IOException {
        this.context = context;
        InetAddress mcastaddr = InetAddress.getByName(addr);
        this.group = new InetSocketAddress(mcastaddr, PORT);
        this.networkInterface = NetworkInterface.getByName("em0");


        receiveSocket.joinGroup(group, networkInterface);
    }

    @Override
    public void run() {
        DatagramPacket recv = new DatagramPacket(buff, buff.length);
        while(this.isRunnable){
            try {
                this.receiveSocket.receive(recv);
            } catch (IOException e) {
                e.printStackTrace();
            }
            context.setUser(recv.getAddress().toString());
            System.out.println("receiver receive message from " + recv.getAddress() + " " + recv.getPort());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            this.receiveSocket.leaveGroup(this.group, this.networkInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeThreadMode(){
        this.isRunnable = false;
    }
}
