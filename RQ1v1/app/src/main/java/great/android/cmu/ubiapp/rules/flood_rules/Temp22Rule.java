package great.android.cmu.ubiapp.rules.flood_rules;

import android.content.Context;

import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;
import great.android.cmu.ubiapp.adaptations.Temp22Adapt;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.rules.Filter;

public class Temp22Rule extends Filter {

    float detectedTemperature;
    Temp22Adapt temp22Adapt;
    long timeOfRule;

    public Temp22Rule (Context context){
        temp22Adapt = new Temp22Adapt(context);
    }

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
        if (detectedTemperature == 22.0){
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
        temp22Adapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;
        CalculateMetrics.setGeneralWatTimes(this.timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int random){
        setDetectedTemperature((float)random);
    }
}
