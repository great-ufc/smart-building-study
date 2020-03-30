package great.android.cmu.ubiapp.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;

import great.android.cmu.ubiapp.CustomExpandableListAdapter;
import great.android.cmu.ubiapp.MainActivity;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;
import great.android.cmu.ubiapp.helpers.SendBroadcastTask;
import great.android.cmu.ubiapp.helpers.PostMetricsTask;
import great.android.cmu.ubiapp.model.Device;
import great.android.cmu.ubiapp.ExpandableListDataPump;
import great.android.cmu.ubiapp.R;

import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;


public class SavedTabsFragment extends Fragment {

    ProgressBar pgsBar;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, null);
        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        pgsBar = (ProgressBar) v.findViewById(R.id.progressBar);

        new UpdateListView().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        /* Snippet to collect metrics: wat, ta, rulesEvaluated */
        new PostMetricsTask(getContext(), 1)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        String.valueOf(CalculateMetrics.getCalculatedGeneralWat()),
                        String.valueOf(CalculateMetrics.getCalculatedTAs()),
                        String.valueOf(CalculateMetrics.getNumberOfRulesVerified()));

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                // Toast.makeText(getContext(), expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                // Toast.makeText(getContext(), expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(expandableListTitle.get(groupPosition).equals(Keywords.LD_ACTUATORS)){
                    String[] deviceData = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).toString().split(" - ");
                    Toast.makeText(getContext(), "Sending broadcast to device with this IP [" + deviceData[2] + "]", Toast.LENGTH_SHORT).show();
                    new SendBroadcastTask(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, deviceData[2]);
                }else if(expandableListTitle.get(groupPosition).equals(Keywords.LD_SENSORS)){
                    String[] deviceData = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).toString().split(" - ");
                    MainActivity main = (MainActivity) getParentFragment().getActivity();
                    Device specificDevice = null;
                    for(Device d : main.getDevices()){
                        if(d.getType().equals("sensor") && d.getIp().equals(deviceData[2]) && d.getResourceType().equals(deviceData[1])){
                            specificDevice = d; break;
                        }
                    }
                    //System.out.println(specificDevice);
                    Toast.makeText(getContext(), "Context: [" + specificDevice.getContext() + "]", Toast.LENGTH_LONG).show();
                }else if(expandableListTitle.get(groupPosition).equals(Keywords.LD_ENV)){
                    String envSelected = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).toString();
                    ArrayList<String> ips = new ArrayList<>();
                    MainActivity main = (MainActivity) getParentFragment().getActivity();
                    for(Device d : main.getDevices()){
                        if(d.getContext().get("env") != null && d.getContext().get("env").equals(envSelected) && !ips.contains(d.getIp())){
                            ips.add(d.getIp());
                        }
                    }
                    Toast.makeText(getContext(), "Sending broadcast to all devices in this environment [" + envSelected + "]", Toast.LENGTH_SHORT).show();
                    String[] ipsToBroadcast = new String[ips.size()];
                    for(int i = 0; i < ips.size(); i++){
                        ipsToBroadcast[i] = ips.get(i);
                    }
                    new SendBroadcastTask(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ipsToBroadcast);
                }
                return false;
            }
        });

        return v;
    }

    private class UpdateListView extends AsyncTask<Void, Void, Void> {

        private boolean done = false;

        @Override
        protected void onPostExecute(Void voids) {
            MainActivity main = (MainActivity) getParentFragment().getActivity();
            expandableListDetail = ExpandableListDataPump.getData(main.getDevices());
            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
            expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);
            pgsBar.setVisibility(View.INVISIBLE);
        }

        @Override
        @SuppressLint("MissingPermission")
        protected Void doInBackground(Void... voids) {
            boolean useCOAPCTX = sharedPrefs.contains(Keywords.COAP_SWITCH) && sharedPrefs.getBoolean(Keywords.COAP_SWITCH, false);
            boolean filterByDistance = sharedPrefs.contains(Keywords.DISTANCE_SWITCH) && sharedPrefs.getBoolean(Keywords.DISTANCE_SWITCH, false);
            String COAP_URI = "coap://18.229.202.214:5683/devices?";
            if(useCOAPCTX){
                WifiManager manager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                String sSID = info.getSSID();
                if(sSID.length() > 0 && !sSID.equals("<unknown ssid>")){
                    COAP_URI += ("network=" + sSID.replaceAll("\"", "").trim() + "&");
                }
                if(filterByDistance){
                    if(MainActivity.myLocation != null){
                        int radius = MainActivity.getRadius();
                        double longitude = MainActivity.myLocation.getLongitude();
                        double latitude = MainActivity.myLocation.getLatitude();
                        COAP_URI += ("proximity=" + radius + "," + latitude + "," + longitude);
                    }
                }
            }

            System.out.println(COAP_URI);

            /**
             * Requests examples:
             * - coap:// /.well-known/core
             * - coap://18.229.202.214:5683/devices?if=sensor
             * - coap://18.229.202.214:5683/devices?proximity=165,-3.7466212,-38.5769008
             * - coap://18.229.202.214:5683/devices?proximity=165,-3.7466212,-38.5769008&if=actuator
             * - coap://18.229.202.214:5683/devices?proximity=165,-3.7466212,-38.5769008&if=actuator&ct=10
             */
            MainActivity main = (MainActivity) getParentFragment().getActivity();
            boolean execute = false;
            if(main.getLastDeviceUpdade() == null){
                execute = true;
            }else{
                execute = ((new Date().getTime() - main.getLastDeviceUpdade().getTime())/60000) > 60 || sharedPrefs.getBoolean(Keywords.CHANGED, false);
            }

            int times = execute ? 0 : 3;
            long begin = new Date().getTime();
            while(!done){
                if(execute){
                    execute = false;
                    sharedPrefs.edit().putBoolean(Keywords.CHANGED, false).commit();
                    NetworkConfig standardConfig = NetworkConfig.createStandardWithoutFile();
                    standardConfig.setInt("MAX_RESOURCE_BODY_SIZE", 18192);

                    final CoapClient client = new CoapClient(COAP_URI);
                    client.get(new CoapHandler() {
                        @Override
                        public void onLoad(CoapResponse response) {
                            if(response != null){
                                //System.out.println(jsonArray);
                                ArrayList<Device> coapDevicesList = new ArrayList<Device>();
                                MainActivity main = (MainActivity) getParentFragment().getActivity();

                                String jsonArray = response.advanced().getPayloadString();
                                Type listType = new TypeToken<ArrayList<Device>>(){}.getType();
                                List<Device> devices = new Gson().fromJson(jsonArray, listType);

                                for (Device device : devices) {
                                    coapDevicesList.add(device);
                                }

                                done = true;
                                main.setLastDeviceUpdade(new Date());
                                main.setDevices(coapDevicesList);
                            }
                        }

                        @Override
                        public void onError() {
                            System.err.println("Exception on getDataFromCoapServer()");
                        }
                    });
                }
                /* waiting coap response until 5 seconds of timeout and three times */
                long end = new Date().getTime();
                if((end - begin) > 5000){
                    execute = true;
                    times++;
                }
                if(times == 3){
                    break;
                }
            }
            return null;
        }
    }
}