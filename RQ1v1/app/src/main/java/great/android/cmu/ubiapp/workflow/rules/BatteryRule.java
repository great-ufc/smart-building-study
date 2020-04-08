package great.android.cmu.ubiapp.workflow.rules;

import android.content.Context;
import android.os.BatteryManager;

import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.workflow.adaptations.BatteryAdaptation;

import static android.content.Context.BATTERY_SERVICE;

public class BatteryRule extends Filter {

    private BatteryAdaptation batteryAdapt;
    private long timeOfRule;

    public BatteryRule(Context context){
        batteryAdapt = new BatteryAdaptation(context);
    }

    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    @Override
    public boolean evaluate() {
        BatteryManager bm = (BatteryManager) this.batteryAdapt.getContext().getSystemService(BATTERY_SERVICE);
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

    @Override
    public void execute() {
        long timeOfAdaptStart = new Date().getTime();
        batteryAdapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;

        CalculateMetrics.setGeneralWatTimes(timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int value){
    }
}
