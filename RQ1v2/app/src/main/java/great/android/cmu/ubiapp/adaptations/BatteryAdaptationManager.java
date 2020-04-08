package great.android.cmu.ubiapp.adaptations;

import android.content.Context;
import android.os.AsyncTask;
import android.os.BatteryManager;

import java.util.Date;

import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;

import static android.content.Context.BATTERY_SERVICE;
import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;

public class BatteryAdaptationManager extends AsyncTask<Context, Void, Void> {

    private long timeOfRule;

    private boolean evaluate(Context context){
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        int batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        CalculateMetrics.setNumberOfRulesVerified();

        long timeOfRuleStart = new Date().getTime();

        if (batteryLevel <= 50){
            execute();
            this.timeOfRule = new Date().getTime() - timeOfRuleStart;
            return true;
        } else{
            this.timeOfRule = new Date().getTime() - timeOfRuleStart;
            return false;
        }
    }

    private void execute(){
        long timeOfAdaptStart = new Date().getTime();
        long timeOfStart = System.currentTimeMillis();

        sharedPrefs.edit().putBoolean(Keywords.COAP_SWITCH, true).commit();
        sharedPrefs.edit().putBoolean(Keywords.DISTANCE_SWITCH, true).commit();
        sharedPrefs.edit().putInt(Keywords.RADIUS, 10).commit();

        long timeOfEnd = System.currentTimeMillis();
        CalculateMetrics.setTATimes(CalculateMetrics.calculateExecutionTime(timeOfStart, timeOfEnd));
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;
        CalculateMetrics.setGeneralWatTimes(timeOfRule, timeOfAdapt);
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        Context context = contexts[0];
        if(evaluate(context)){
            execute();
        }
        return null;
    }
}
