package great.android.cmu.ubiapp.workflow.adaptations;

import android.content.Context;

import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;
import task.Task2;

import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;

public class WorkingHourAdaptation extends Task2 {

    private Context context;
    private long timeOfStart, timeOfEnd;

    public WorkingHourAdaptation(Context context){
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
        sharedPrefs.edit().putBoolean(Keywords.WORKING_HOUR, true).commit();
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
