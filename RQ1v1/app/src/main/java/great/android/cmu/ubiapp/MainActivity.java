package great.android.cmu.ubiapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import great.android.cmu.ubiapp.context_manager.Async_Context_Manager;
import great.android.cmu.ubiapp.helpers.Keywords;
import great.android.cmu.ubiapp.model.Device;
import great.android.cmu.ubiapp.rules.flood_rules.Batt50Rule;
import great.android.cmu.ubiapp.rules.flood_rules.Hour12Rule;
import great.android.cmu.ubiapp.rules.flood_rules.Hour18Rule;
import great.android.cmu.ubiapp.rules.flood_rules.Temp14Rule;
import great.android.cmu.ubiapp.rules.flood_rules.Temp22Rule;
import great.android.cmu.ubiapp.rules.flood_rules.Temp30Rule;
import great.android.cmu.ubiapp.workflow.MainWorkflow;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Device> devices;
    private Date lastDeviceUpdade;

    public static SharedPreferences sharedPrefs;
    public static LocationManager locationManager;
    public static Location myLocation;

    private String[] myPermissions = {
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE
    };

    Batt50Rule batt50Rule;
    Hour12Rule hour12Rule;
    Hour18Rule hour18Rule;
    Temp14Rule temp14Rule;
    Temp22Rule temp22Rule;
    Temp30Rule temp30Rule;

    // Intent intent  = new Intent("Context_Manager");

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_settings)
                .build();//R.id.navigation_historic,
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        myLocation = MainActivity.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(myLocation == null){
            myLocation = MainActivity.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                myLocation = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        batt50Rule = new Batt50Rule(getApplicationContext());
        hour12Rule = new Hour12Rule(getApplicationContext());
        hour18Rule = new Hour18Rule(getApplicationContext());
        temp14Rule = new Temp14Rule(getApplicationContext());
        temp22Rule = new Temp22Rule(getApplicationContext());
        temp30Rule = new Temp30Rule(getApplicationContext());

        MainWorkflow.receive(batt50Rule);
        MainWorkflow.receive(hour12Rule);
        MainWorkflow.receive(hour18Rule);
        MainWorkflow.receive(temp14Rule);
        MainWorkflow.receive(temp22Rule);
        MainWorkflow.receive(temp30Rule);
        // startService(intent);

        Async_Context_Manager.startContextManager(getApplicationContext());
    }

    // Function to check and request permission.
    public void checkPermissions(){
        int code = 0;
        for(String permission : myPermissions){
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                //System.out.println("Requesting permission " + permission);
                ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, code);
            } else {
                //System.out.println("Permission " + permission + " already granted!");
                //Toast.makeText(MainActivity.this,"Permission " + permission + " already granted!", Toast.LENGTH_SHORT).show();
            }
            code++;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //System.out.println("Permission " + myPermissions[requestCode] + " granted!");
            //Toast.makeText(MainActivity.this, "Permission" + myPermissions[requestCode] + " granted!", Toast.LENGTH_SHORT).show();
        } else {
            //System.out.println("Permission " + myPermissions[requestCode] + " denied!");
            //Toast.makeText(MainActivity.this,"Permission" + myPermissions[requestCode] + " denied!", Toast.LENGTH_SHORT).show();
        }
    }

    public List<Device> getDevices(){
        return this.devices;
    }

    public void setDevices(ArrayList<Device> devices){
        this.devices = devices;
    }

    public Date getLastDeviceUpdade() {
        return lastDeviceUpdade;
    }

    public void setLastDeviceUpdade(Date lastDeviceUpdade) {
        this.lastDeviceUpdade = lastDeviceUpdade;
    }

    public static int getRadius(){
        return sharedPrefs.getInt(Keywords.RADIUS, 10);
    }
}
