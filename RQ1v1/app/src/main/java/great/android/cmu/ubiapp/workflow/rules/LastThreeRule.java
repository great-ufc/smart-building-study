package great.android.cmu.ubiapp.workflow.rules;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import evaluators.Assignment;
import evaluators.EvaluationException;
import great.android.cmu.ubiapp.MainActivity;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;
import great.android.cmu.ubiapp.helpers.Utils;
import great.android.cmu.ubiapp.model.Device;
import great.android.cmu.ubiapp.workflow.adaptations.LastThreeAdaptation;

import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;

public class LastThreeRule extends Filter {

    private LastThreeAdaptation lastThreeAdapt;
    private long timeOfRule;

    public LastThreeRule(Context context){
        lastThreeAdapt = new LastThreeAdaptation(context);
    }

    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    @Override
    public boolean evaluate() {
        CalculateMetrics.setNumberOfRulesVerified();
        long timeOfRuleStart = new Date().getTime();
        if(sharedPrefs.getBoolean(Keywords.WORKING_HOUR, false)){
            ArrayList<Device> list = new ArrayList<>();
            if(MainActivity.devices != null){
                for(Device d : MainActivity.devices){
                    if(d.getType().equals("researcher")){
                        list.add(d);
                    }
                }
                if(list.size() == 3){
                    double latitudeGREat = -3.747350;
                    double longitudeGREat = -38.574450;
                    for(Device d : list){
                        String vLat = d.getContext().get("latitude");
                        String vLong = d.getContext().get("longitude");
                        if(vLat != null && !vLat.isEmpty() && vLong != null && !vLong.isEmpty()){
                            double latitude = Double.valueOf(vLat);
                            double longitude = Double.valueOf(vLong);
                            if(Utils.distance(latitudeGREat, latitude, longitudeGREat, longitude) > 30){
                                return false;
                            }
                        }
                    }
                    lastThreeAdapt.setLastThree(list);
                    execute();
                    this.timeOfRule = new Date().getTime() - timeOfRuleStart;
                    return true;
                }
            }
        }

        this.timeOfRule = new Date().getTime() - timeOfRuleStart;
        return false;
    }

    @Override
    public void execute() {
        long timeOfAdaptStart = new Date().getTime();
        lastThreeAdapt.executar();
        long timeOfAdapt = new Date().getTime() - timeOfAdaptStart;

        CalculateMetrics.setGeneralWatTimes(timeOfRule, timeOfAdapt);
    }

    @Override
    public void setContext(int value){
    }
}
