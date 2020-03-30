package great.android.cmu.ubiapp.rules.flood_rules;

import android.content.Context;

import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;

import great.android.cmu.ubiapp.adaptations.Hour12Adapt;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.rules.Filter;

public class Hour12Rule extends Filter {

    int currentHourIn24Format = 0;
    Hour12Adapt hour12Adapt;
    long timeOfRule;

    public Hour12Rule (Context context){
        hour12Adapt = new Hour12Adapt(context);
    }

    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    public void setCurrentHourIn24Format(int currentHourIn24Format){
        this.currentHourIn24Format = currentHourIn24Format;
    }

    @Override
    public boolean evaluate() {
        long timeOfRuleStart = new Date().getTime();
        if (currentHourIn24Format==12){
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
        hour12Adapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;

        CalculateMetrics.setGeneralWatTimes(this.timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int random){
        setCurrentHourIn24Format(random);
    }
}
