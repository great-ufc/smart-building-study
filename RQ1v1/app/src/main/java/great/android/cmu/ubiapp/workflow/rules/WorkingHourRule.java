package great.android.cmu.ubiapp.workflow.rules;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;
import great.android.cmu.ubiapp.workflow.adaptations.WorkingHourAdaptation;

import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;

public class WorkingHourRule extends Filter {

    private WorkingHourAdaptation whAdapt;
    private long timeOfRule;

    public WorkingHourRule(Context context){
        whAdapt = new WorkingHourAdaptation(context);
    }

    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    @Override
    public boolean evaluate() {
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

    @Override
    public void execute() {
        long timeOfAdaptStart = new Date().getTime();
        whAdapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;

        CalculateMetrics.setGeneralWatTimes(timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int value){
    }
}
