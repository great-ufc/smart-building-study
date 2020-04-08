package great.android.cmu.ubiapp.adaptations;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import great.android.cmu.ubiapp.MainActivity;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;
import great.android.cmu.ubiapp.helpers.MyUtils;
import great.android.cmu.ubiapp.helpers.SendBroadcastTask;
import great.android.cmu.ubiapp.model.Device;

import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;

public class LastThreeAdaptationManager extends AsyncTask<Context, Void, Void> {

    private ArrayList<Device> lastThree;
    private long timeOfRule;

    private boolean evaluate(Context context){
        CalculateMetrics.setNumberOfRulesVerified();
        long timeOfRuleStart = new Date().getTime();
        if(sharedPrefs.getBoolean(Keywords.WORKING_HOUR, false)){
            ArrayList<Device> list = new ArrayList<>();
            if(MainActivity.devices != null){
                for(Device d : MainActivity.devices){
                    if(d.getType().equals("researcher")){
                        list.add(d);
                    }
                }
                if(list.size() == 3){
                    double latitudeGREat = -3.747350;
                    double longitudeGREat = -38.574450;
                    for(Device d : list){
                        String vLat = d.getContext().get("latitude");
                        String vLong = d.getContext().get("longitude");
                        if(vLat != null && !vLat.isEmpty() && vLong != null && !vLong.isEmpty()){
                            double latitude = Double.valueOf(vLat);
                            double longitude = Double.valueOf(vLong);
                            if(MyUtils.distance(latitudeGREat, latitude, longitudeGREat, longitude) > 30){
                                return false;
                            }
                        }
                    }
                    lastThree = list;
                    execute();
                    this.timeOfRule = new Date().getTime() - timeOfRuleStart;
                    return true;
                }
            }
        }

        this.timeOfRule = new Date().getTime() - timeOfRuleStart;
        return false;
    }

    private void execute(Context context){
        long timeOfAdaptStart = new Date().getTime();
        long timeOfStart = System.currentTimeMillis();

        Toast.makeText(context, "Sending message to devices with IP ["
                + this.lastThree.get(0).getIp() + ", "
                + this.lastThree.get(1).getIp() + ", "
                + this.lastThree.get(2).getIp() + "]", Toast.LENGTH_SHORT).show();
        new SendBroadcastTask(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                this.lastThree.get(0).getIp(), this.lastThree.get(1).getIp(), this.lastThree.get(2).getIp());

        long timeOfEnd = System.currentTimeMillis();

        CalculateMetrics.setTATimes(CalculateMetrics.calculateExecutionTime(timeOfStart, timeOfEnd));
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;

        CalculateMetrics.setGeneralWatTimes(timeOfRule, timeOfAdapt);
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        Context context = contexts[0];
        if(evaluate(context)){
            execute(context);
        }
        return null;
    }
}

