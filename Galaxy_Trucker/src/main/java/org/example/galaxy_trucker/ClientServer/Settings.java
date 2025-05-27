package org.example.galaxy_trucker.ClientServer;

import java.net.InetAddress;

public class Settings {
    public static int TCP_PORT = 6969;
    public static int RMI_PORT = 1109;
    public static String SERVER_NAME = "127.0.0.1";

    public static void setIp(String ip) {
        if (!ip.equals("")){
            SERVER_NAME = ip;
        }
    }
}