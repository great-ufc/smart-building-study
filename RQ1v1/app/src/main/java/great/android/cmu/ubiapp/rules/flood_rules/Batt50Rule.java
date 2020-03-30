package great.android.cmu.ubiapp.rules.flood_rules;

import android.content.Context;

import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;
import great.android.cmu.ubiapp.adaptations.Batt50Adapt;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.rules.Filter;

public class Batt50Rule extends Filter {

    Batt50Adapt batt50Adapt;
    static int batteryLevel = 0;
    long timeOfRule;

    public Batt50Rule (Context context){
        batt50Adapt  = new Batt50Adapt(context);
    }

    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    public void setBatteryLevel(int batteryLevel){
        this.batteryLevel = batteryLevel;
    }

    @Override
    public boolean evaluate() {
        long timeOfRuleStart = new Date().getTime();

        if (batteryLevel <= 50){
            CalculateMetrics.setNumberOfRulesVerified();
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
        batt50Adapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;

        CalculateMetrics.setGeneralWatTimes(timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int random){
        setBatteryLevel(random);
    }
}
