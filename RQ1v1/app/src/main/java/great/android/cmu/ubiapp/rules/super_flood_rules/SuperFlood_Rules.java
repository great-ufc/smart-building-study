package great.android.cmu.ubiapp.rules.super_flood_rules;

import java.util.HashMap;
import java.util.Map;

import evaluators.Assignment;
import evaluators.EvaluationException;
import evaluators.Filter;

public class SuperFlood_Rules  extends Filter {


    static Map<String,String> generatedKeyValueRules = new HashMap<String,String>();

    public SuperFlood_Rules(){
        generateRule();
    }

    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    @Override
    public boolean evaluate() {
        return false;
    }

    @Override
    public void execute() {

    }

    private static void generateRule(){

        generatedKeyValueRules.put( "Temperature", "5" );


    }
}
