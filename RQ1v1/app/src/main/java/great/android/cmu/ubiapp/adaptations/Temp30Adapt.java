package great.android.cmu.ubiapp.adaptations;

import android.content.Context;

import great.android.cmu.ubiapp.external.External_Processment;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import task.Task2;

public class Temp30Adapt extends Task2 {

    Context received_context;
    long timeOfStart;
    long timeOfEnd;

    public Temp30Adapt (Context context){
        received_context = context;
    }
    @Override
    public void recebeToken(Object o) {

    }

    @Override
    public Object retornaToken() {
        return null;
    }

    @Override
    public void executar() {
        timeOfStart = System.currentTimeMillis();
        External_Processment.turnOnTheAir(received_context);
        timeOfEnd = System.currentTimeMillis();
        CalculateMetrics.setTATimes(CalculateMetrics.calculateExecutionTime(timeOfStart, timeOfEnd));
    }

    @Override
    public Object retorno() {
        return null;
    }

    @Override
    public void evaluate(Object o) {

    }
}
