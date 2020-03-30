package great.android.cmu.ubiapp.external;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import great.android.cmu.ubiapp.helpers.Utils;

public class External_Processment {


   static Context received_context;

   static String range;




    public static void turnOnTheLights(Context context){
        received_context = context;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public static void turnOnTheLights(Context context, String range){
        received_context = context;
        range = range;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public static void turnOffTheLights(Context context){
        received_context = context;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public static void turnOffTheLights(Context context, String range){
        received_context = context;
        range = range;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public static void turnOnTheAir(Context context){
        received_context = context;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public static void turnOnTheAir(Context context,String range){
        received_context = context;
        range = range;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void turnOffTheAir(Context context){
        received_context = context;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public static void turnOffTheAir(Context context,String range){
        received_context = context;
        range = range;
        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }






    private static class UpdateContextTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Utils.sendBroadcast(new ArrayList<String>());
            Long currentTime = System.currentTimeMillis();
            new UDP_Listener().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentTime);
            return null;
        }
    }

    private static class UDP_Listener extends AsyncTask<Long, String, Long>{
        @Override
        protected Long doInBackground(Long... inputs) {
            int server_port = 6789;
            byte[] message = new byte[1024];
            try{
                DatagramPacket p = new DatagramPacket(message, message.length);
                DatagramSocket s = new DatagramSocket(server_port);
                s.receive(p);

                String clientMessage = new String(p.getData(), 0, p.getLength());
                String result = "IP " + p.getAddress().toString() + " sent: " + clientMessage;
                System.out.println(result);

                s.close();
                publishProgress(result);
            }catch(Exception e){
                Log.d("UDP_Listener","Error: " + e.toString());
            }
            return System.currentTimeMillis() - inputs[0];
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Toast.makeText(received_context, progress[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Long elapsedTime) {
            super.onPostExecute(elapsedTime);
            Toast.makeText(received_context, "elapsed time " + elapsedTime.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Elapsed Time: " + elapsedTime);
        }
    }

}
