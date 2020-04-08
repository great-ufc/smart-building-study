package great.android.cmu.ubiapp.context_manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import great.android.cmu.ubiapp.helpers.Utils;
import great.android.cmu.ubiapp.workflow.MainWorkflow;

public class Async_Context_Manager {

    static Context received_context;

    public static void startContextManager(Context context){
        received_context = context;
        new StartContextManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static class StartContextManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            new ChangeContext().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return null;
        }
    }

    private static int generateRandom(int up, int down){
        return ((int)(Math.random()* up+down));
    }

    private static class ChangeContext extends AsyncTask<Long, String, Long>{
        public int count = 0;
        public boolean active = true;
        @Override
        protected Long doInBackground(Long... inputs) {
            while(active && count <= 20){
                MainWorkflow.observeContexts(generateRandom(24,0));
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }
            return (inputs.length > 0) ? System.currentTimeMillis() - inputs[0] : 0;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            //Toast.makeText(received_context, progress[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Long elapsedTime) {
            super.onPostExecute(elapsedTime);
            //Toast.makeText(received_context, "elapsed time " + elapsedTime.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Elapsed Time: " + elapsedTime);
        }
    }
}
