package great.android.cmu.ubiapp.workflow.adaptations;

import android.content.Context;

import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;
import task.Task2;

import static android.content.Context.BATTERY_SERVICE;
import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;
import static great.android.cmu.ubiapp.helpers.Keywords.DISTANCE_SWITCH;

public class BatteryAdaptation extends Task2 {

    private Context context;
    private long timeOfStart, timeOfEnd;

    public BatteryAdaptation(Context context){
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }

    @Override
    public void recebeToken(Object o) {
    }

    @Override
    public Object retornaToken() {
        return null;
    }

    @Override
    public void executar() {
        timeOfStart = System.currentTimeMillis();
        sharedPrefs.edit().putBoolean(Keywords.COAP_SWITCH, true).commit();
        sharedPrefs.edit().putBoolean(Keywords.DISTANCE_SWITCH, true).commit();
        sharedPrefs.edit().putInt(Keywords.RADIUS, 10).commit();


        timeOfEnd = System.currentTimeMillis();

        CalculateMetrics.setTATimes(CalculateMetrics.calculateExecutionTime(timeOfStart, timeOfEnd));
    }

    @Override
    public Object retorno() {
        return null;
    }

    @Override
    public void evaluate(Object o) {
    }
}
