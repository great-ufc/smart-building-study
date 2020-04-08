package great.android.cmu.ubiapp.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import great.android.cmu.ubiapp.R;
import great.android.cmu.ubiapp.loccam.ContextKeys;
import great.android.cmu.ubiapp.loccam.ContextListener;
import great.android.cmu.ubiapp.loccam.ContextManager;
import great.android.cmu.ubiapp.model.Device;

/**
 * Profile activity
 * */
public class DashboardFragment extends Fragment {

    private Gson gson;
    private static Context dashContext;
    private LinkedHashMap<String, String> myContext;
    private DashboardViewModel dashboardViewModel;
    private static final String COAP_SERVER_URL = "coap://18.229.202.214:5683/devices";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        gson = new Gson();
        myContext = new LinkedHashMap<String, String>();

        // Task to update myContext for each 30 seconds
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 30000l);

        root.findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DashboardFragment", "Btn");
                checkAndSendCoapRegister();
            }
        });

        return root;
    }

    private void checkAndSendCoapRegister() {

        String name     = ((EditText) getActivity().findViewById(R.id.nameEditText)).getText().toString().trim();
        String profile  = ((Spinner) getActivity().findViewById(R.id.profileSpinner)).getSelectedItem().toString();
        String project  = ((Spinner) getActivity().findViewById(R.id.projectSpinner)).getSelectedItem().toString();
        String advisor  = ((EditText) getActivity().findViewById(R.id.advisorEditText)).getText().toString().trim();
        String env      = ((EditText) getActivity().findViewById(R.id.envEditText)).getText().toString().trim();
        String message  = ((EditText) getActivity().findViewById(R.id.messageEditText)).getText().toString().trim();

        if(name.isEmpty() || profile.isEmpty() || project.isEmpty() || advisor.isEmpty() || env.isEmpty() || message.isEmpty()){
            Toast.makeText(DashboardFragment.dashContext, "All fields are mandatory!", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("DashboardFragment", "Aqui");

        // Create device abstraction
        String ip = getLocalIpAddress();
        String uid = ip.replaceAll("\\.", "") + "_message".trim();
        Device researcher = new Device(uid, "researcher", "Message", MediaTypeRegistry.APPLICATION_JSON, ip);
        this.myContext.put("name", name);
        this.myContext.put("profile", profile);
        this.myContext.put("project", project);
        this.myContext.put("advisor", advisor);
        this.myContext.put("message", message);
        this.myContext.put("env", env);

        WifiManager manager = (WifiManager) DashboardFragment.dashContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String sSID = info.getSSID();
        if(sSID.length() > 0) myContext.put("network", sSID.replaceAll("\"", "").trim());

        researcher.setContext(this.myContext);

        NetworkConfig.createStandardWithoutFile();
        CoapClient client = new CoapClient(COAP_SERVER_URL);
        String json = gson.toJson(researcher);
        client.post(json, MediaTypeRegistry.APPLICATION_JSON);
        Toast.makeText(DashboardFragment.dashContext, "CoAP Request Sent!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashContext = context;
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * ########################################################################
     *        Get and Update Context using Google Awereness and LoCCAM
     * ########################################################################
     * */

    private class UpdateContextTask extends AsyncTask<Long, String, String> {
        @Override
        protected String doInBackground(Long... waitParams) {
            Looper.prepare();
            while(true){
                try {
                    Thread.sleep(waitParams[0]);
                    startSnapshots();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void startSnapshots(){
        SnapshotClient snapshot = Awareness.getSnapshotClient(DashboardFragment.dashContext);
        //System.out.println("Starting snapshots...");
        snapshot.getWeather().addOnSuccessListener(new OnSuccessListener<WeatherResponse>() {
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                myContext.put("temperature", String.format("%.2f", weatherResponse.getWeather().getTemperature(Weather.CELSIUS)));
                myContext.put("humidity", weatherResponse.getWeather().getHumidity() + "");
                myContext.put("feels", String.format("%.2f", weatherResponse.getWeather().getFeelsLikeTemperature(Weather.CELSIUS)));
            }
        });

        snapshot.getLocation().addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
            @Override
            public void onSuccess(LocationResponse locationResponse) {
                myContext.put("latitude", locationResponse.getLocation().getLatitude() + "");
                myContext.put("longitude", locationResponse.getLocation().getLongitude() + "");
            }
        });

        snapshot.getDetectedActivity().addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>() {
            @Override
            public void onSuccess(DetectedActivityResponse detectedActivityResponse) {
                String detectedActivity = null;

                switch (detectedActivityResponse.getActivityRecognitionResult().getMostProbableActivity().getType()){
                    case DetectedActivity.IN_VEHICLE: detectedActivity = "inVehicle"; break;
                    case DetectedActivity.ON_BICYCLE: detectedActivity = "onBicycle"; break;
                    case DetectedActivity.ON_FOOT:    detectedActivity = "onFoot";    break;
                    case DetectedActivity.STILL:      detectedActivity = "still";     break;
                    case DetectedActivity.TILTING:    detectedActivity = "tilting";   break;
                    case DetectedActivity.WALKING:    detectedActivity = "walking";   break;
                    case DetectedActivity.RUNNING:    detectedActivity = "running";   break;
                    default: detectedActivity = "unknown";
                }
                myContext.put("detectedActivity", detectedActivity);
            }
        });

        snapshot.getTimeIntervals().addOnSuccessListener(new OnSuccessListener<TimeIntervalsResponse>() {
            @Override
            public void onSuccess(TimeIntervalsResponse timeIntervalsResponse) {
                String interval = null;
                int[] intervals = timeIntervalsResponse.getTimeIntervals().getTimeIntervals();

                switch (intervals[0]){
                    case TimeFence.TIME_INTERVAL_WEEKDAY: interval = "weekday_"; break;
                    case TimeFence.TIME_INTERVAL_WEEKEND: interval = "weekend_"; break;
                    case TimeFence.TIME_INTERVAL_HOLIDAY: interval = "holiday_"; break;
                    default: interval = "unknown";
                }
                switch (intervals[1]){
                    case TimeFence.TIME_INTERVAL_MORNING:   interval += "morning";   break;
                    case TimeFence.TIME_INTERVAL_AFTERNOON: interval += "afternoon"; break;
                    case TimeFence.TIME_INTERVAL_EVENING:   interval += "evening";   break;
                    case TimeFence.TIME_INTERVAL_NIGHT:     interval += "night";     break;
                    default: interval = "unknown";
                }

                myContext.put("timeInterval", interval);
                Toast.makeText(DashboardFragment.dashContext, "Context updated!", Toast.LENGTH_LONG).show();
            }
        });
    }
}