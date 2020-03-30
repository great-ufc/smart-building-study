package great.android.cmu.ubiapp.rules;

import evaluators.Assignment;
import evaluators.EvaluationException;
import evaluators.Filter;

public class LampRule extends Filter {
    @Override
    public boolean filter(Assignment assignment) throws EvaluationException {
        return false;
    }

    @Override
    public boolean evaluate() {
    //O WORKFLOW VAI CHAMAR O EVALUATE VAZIO
    //O EVALUATE VAZIO DEVE CHAMAR O OUTRO EVALUATE E INSTANCIA-LO. UMA IDEIA É PASSAR OS DADOS NECESSÁRIO PARA A CLASSA ATRAVÉS NA HORA DA INSTANCIAÇÃO DO OBJETO OU SETTERS

        return evaluate("sim", "não");
    }

    @Override
    public void execute() {
    }

    public boolean evaluate(String a, String b){

        return true;
    }
}
