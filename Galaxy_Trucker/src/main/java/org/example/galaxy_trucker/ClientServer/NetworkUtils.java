package org.example.galaxy_trucker.ClientServer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;


public class NetworkUtils {


    public static String getLocalIPAddress() {
        try {
            //qui prendo tutte le interfacce di quel bastardo
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netIf : Collections.list(interfaces)) {
                if (netIf.isLoopback() || !netIf.isUp()) continue;

                Enumeration<InetAddress> addresses = netIf.getInetAddresses();
                for (InetAddress addr : Collections.list(addresses)) {
                    //devo buttà tutto quello che non è ipv4 direi
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //se non trovi niente prega dio
        return "127.0.0.1";
    }

}
