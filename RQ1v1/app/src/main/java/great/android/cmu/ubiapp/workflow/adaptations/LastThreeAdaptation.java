package great.android.cmu.ubiapp.workflow.adaptations;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import great.android.cmu.ubiapp.helpers.CalculateMetrics;
import great.android.cmu.ubiapp.helpers.Keywords;
import great.android.cmu.ubiapp.helpers.SendBroadcastTask;
import great.android.cmu.ubiapp.model.Device;
import task.Task2;

import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;

public class LastThreeAdaptation extends Task2 {

    private Context context;
    private List<Device> lastThree;
    private long timeOfStart, timeOfEnd;

    public LastThreeAdaptation(Context context){
        this.context = context;
    }

    public Context getContext(){
        return this.context;
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

        Toast.makeText(getContext(), "Sending message to devices with IP ["
                + this.lastThree.get(0).getIp() + ", "
                + this.lastThree.get(1).getIp() + ", "
                + this.lastThree.get(2).getIp() + "]", Toast.LENGTH_SHORT).show();
        new SendBroadcastTask(this.context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                this.lastThree.get(0).getIp(), this.lastThree.get(1).getIp(), this.lastThree.get(2).getIp());

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

    public void setLastThree(List<Device> devices){
        this.lastThree = devices;
    }
}
