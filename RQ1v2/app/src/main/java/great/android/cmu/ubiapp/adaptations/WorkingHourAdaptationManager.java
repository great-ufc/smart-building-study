package great.android.cmu.ubiapp.adaptations;


import android.content.Context;
import android.os.AsyncTask;
import android.os.BatteryManager;

import java.util.Calendar;
import java.util.Date;

import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;

import static android.content.Context.BATTERY_SERVICE;
import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;

public class WorkingHourAdaptationManager extends AsyncTask<Context, Void, Void> {

    private long timeOfRule;

    private boolean evaluate(Context context){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        CalculateMetrics.setNumberOfRulesVerified();
        long timeOfRuleStart = new Date().getTime();

        if (hour < 8 || hour >= 18){
            execute();
            this.timeOfRule = new Date().getTime() - timeOfRuleStart;
            return true;
        } else{
            sharedPrefs.edit().putBoolean(Keywords.WORKING_HOUR, false).commit();
            this.timeOfRule = new Date().getTime() - timeOfRuleStart;
            return false;
        }
    }

    private void execute(){
        long timeOfAdaptStart = new Date().getTime();
        long timeOfStart = System.currentTimeMillis();

        sharedPrefs.edit().putBoolean(Keywords.WORKING_HOUR, true).commit();

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

