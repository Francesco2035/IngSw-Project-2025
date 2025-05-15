package org.example.galaxy_trucker.Controller.ClientServer;

import java.net.InetAddress;

public class Settings {
    public static int TCP_PORT = 6969;
    public static int RMI_PORT = 1109;
    public static String SERVER_NAME = "192.168.1.48";

//    static {
//        try {
//            InetAddress localHost = InetAddress.getLocalHost();
//            SERVER_NAME = localHost.getHostAddress();
//        } catch (java.net.UnknownHostException e) {
             // fallback
//            System.err.println("Errore indirizzo ip; uso 127.0.0.1.");
//        }
//    }

    public static void setIp(String ip) {
        SERVER_NAME = ip;
    }
}