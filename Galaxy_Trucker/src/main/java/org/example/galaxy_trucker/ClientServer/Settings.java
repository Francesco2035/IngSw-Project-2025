package org.example.galaxy_trucker.ClientServer;


/**
 * The Settings class encapsulates configuration settings for the application.
 * It includes information such as TCP and RMI ports, as well as the server name.
 * The class provides a utility method to update the server name.
 */
public class Settings {
    /**
     * Represents the default TCP port number used for network communication
     * within the application.
     *
     * This port is utilized for establishing TCP connections and should remain
     * consistent across systems to ensure proper communication between
     * components. The default value is set to 6969.
     */
    public static int TCP_PORT = 6969;
    /**
     * Specifies the port number used for RMI (Remote Method Invocation)
     * communication within the application.
     */
    public static int RMI_PORT = 1109;
    /**
     * The SERVER_NAME variable holds the default IP address of the server
     * that the application connects to. This value can be updated dynamically
     * during runtime using the provided method in the containing class.
     */
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