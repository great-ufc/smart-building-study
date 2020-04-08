package great.android.cmu.ubiapp.ui.home.listview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import great.android.cmu.ubiapp.helpers.Keywords;
import great.android.cmu.ubiapp.model.Device;

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData(List<Device> coapDevicesList) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> mySensors = new ArrayList<String>();
        List<String> myActuators = new ArrayList<String>();
        List<String> myResearchers = new ArrayList<String>();
        ArrayList<String> environments = new ArrayList<>();

        if(coapDevicesList != null) {
            for (int i = 0; i < coapDevicesList.size(); i++) {
                Device device = coapDevicesList.get(i);
                String env = device.getContext().get("env");
                String devIP = device.getIp();
                if (env != null && devIP != null) {
                    String devName = device.getResourceType().replaceAll("\"", "");
                    if (!environments.contains(env)) {
                        environments.add(env);
                    }
                    if (device.getType().equals("actuator")) {
                        myActuators.add(env + " - " + devName + " - " + devIP);
                    } else if(device.getType().equals("researcher")){

                        myResearchers.add(device.getContext().get("name") + " - " + env + " - " + devIP);
                    } else {
                        mySensors.add(env + " - " + devName + " - " + devIP);
                    }
                }
            }
        }

        Collections.sort(mySensors);
        Collections.sort(myActuators);
        Collections.sort(environments);

        //System.out.println(environments);
        //System.out.println(mySensors);

        expandableListDetail.clear();

        expandableListDetail.put(Keywords.LD_ENV, environments);
        expandableListDetail.put(Keywords.LD_ACTUATORS, myActuators);
        expandableListDetail.put(Keywords.LD_SENSORS, mySensors);
        expandableListDetail.put(Keywords.LD_RESEARCHERS, myResearchers);
        return expandableListDetail;
    }
}