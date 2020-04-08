package great.android.cmu.ubiapp.helpers;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import great.android.cmu.ubiapp.model.DeviceAction;
import great.android.cmu.ubiapp.model.DeviceActionMessage;


public class MyUtils {

    public final static int UDP_OUT_PORT = 4445;
    public final static int UDP_IN_PORT = 54809;

    public static void sendBroadcast(List<String> ips){
        Gson gson = new Gson();
        DeviceAction vibrate = new DeviceAction("vibrate", 2000);
        DeviceAction light = new DeviceAction("light", 2000);
        List<DeviceAction> actions = new ArrayList<DeviceAction>();
        actions.add(vibrate); actions.add(light);

        DeviceActionMessage deviceActionMessage = new DeviceActionMessage("CMU-2019", actions, ips);

        try {
            broadcast(gson.toJson(deviceActionMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String broadcastMessage) throws IOException {
        DatagramSocket socket = null;

        socket = new DatagramSocket(UDP_IN_PORT);
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), UDP_OUT_PORT);
        socket.send(packet);
        socket.close();
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, long1 Start point lat2, long2 End point
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double long1, double long2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(long2 - long1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000; // convert to meters
    }
}
