package great.android.cmu.ubiapp.rules.flood_rules;

import android.content.Context;

import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;

import great.android.cmu.ubiapp.adaptations.Temp30Adapt;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.rules.Filter;

public class Temp30Rule extends Filter {

    float detectedTemperature;
    Temp30Adapt temp30Adapt;
    long timeOfRule;

    public Temp30Rule(Context context){
        temp30Adapt = new Temp30Adapt(context);
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
        if (detectedTemperature >= 30.0){
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
        temp30Adapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;
        CalculateMetrics.setGeneralWatTimes(this.timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int random){
        setDetectedTemperature((float)random);
    }
}

