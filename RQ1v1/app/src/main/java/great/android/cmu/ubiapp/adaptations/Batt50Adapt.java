package great.android.cmu.ubiapp.adaptations;

import android.content.Context;
import android.widget.Toast;

import great.android.cmu.ubiapp.external.External_Processment;
import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import task.Task2;

public class Batt50Adapt extends Task2 {

    Context received_context;

    long timeOfStart;
    long timeOfEnd;


    public Batt50Adapt (Context context){
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
        //Toast.makeText(received_context, "Nível de bateria baixo", Toast.LENGTH_LONG).show();
        for(int i = 0; i < 1000; i++){}
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
