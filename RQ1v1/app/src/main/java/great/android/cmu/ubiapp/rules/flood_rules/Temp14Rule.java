package great.android.cmu.ubiapp.rules.flood_rules;

import android.content.Context;

import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;

import great.android.cmu.ubiapp.adaptations.Temp14Adapt;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.rules.Filter;

public class Temp14Rule extends Filter {

    Temp14Adapt temp14Adapt;
    long timeOfRule;

    public Temp14Rule (Context context){
        temp14Adapt = new Temp14Adapt(context);
    }

    float detectedTemperature;
    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    public void setDetectedTemperature(float detectedTemperature){
        this.detectedTemperature = detectedTemperature;
    }

    @Override
    public boolean evaluate() {
        long timeOfRuleStart = new Date().getTime();
        if (detectedTemperature <= 14.0){
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
        temp14Adapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;
        CalculateMetrics.setGeneralWatTimes(this.timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int random){
        setDetectedTemperature((float)random);
    }
}
