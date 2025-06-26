package org.example.galaxy_trucker.ClientServer;


/**
 * The Settings class encapsulates configuration settings for the application.
 * It includes information such as TCP and RMI ports, as well as the server name.
 * The class provides a utility method to update the server name.
 */
public class Settings {
    public static int TCP_PORT = 6969;
    public static int RMI_PORT = 1109;
    public static String SERVER_NAME = "127.0.0.1";

    /**
     * Updates the server name to the specified IP address if the provided
     * IP address is not an empty string.
     *
     * @param ip the new IP address to set as the server name. If the provided
     *           string is empty, the server name will not be updated.
     */
    public static void setIp(String ip) {
        if (!ip.equals("")){
            SERVER_NAME = ip;
        }
    }
}