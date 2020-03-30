package great.android.cmu.ubiapp.rules;

import evaluators.Assignment;
import evaluators.EvaluationException;

public class Filter extends evaluators.Filter {
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

    public void setContext(int random){}

}
