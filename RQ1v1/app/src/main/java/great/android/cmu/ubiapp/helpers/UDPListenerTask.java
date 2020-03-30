package great.android.cmu.ubiapp.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class UDPListenerTask extends AsyncTask<Long, String, Long> {

    private Context context;
    private String ips;
    private String clientMessage;

    public UDPListenerTask(Context context, String ips){
        this.context = context;
        this.ips = ips;
    }

    @Override
    protected Long doInBackground(Long... inputs) {
        int server_port = 6789;
        byte[] message = new byte[1024];
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(server_port);
            DatagramPacket packet = new DatagramPacket(message, message.length);
            socket.setSoTimeout(5000);
            socket.receive(packet);
            this.clientMessage = new String(packet.getData(), 0, packet.getLength());
        }catch (SocketTimeoutException e) {
            this.clientMessage = "Error";
        }catch(Exception e){
            Log.d("UDP_Listener","Error: " + e.toString());
        }finally {
            if(socket != null) socket.close();
        }
        return System.currentTimeMillis() - inputs[0];
    }

    @Override
    protected void onPostExecute(Long elapsedTime) {
        if(this.clientMessage != null && this.clientMessage.equals("CMU-2019: Acting completed successfully!")){
            super.onPostExecute(elapsedTime);
            Toast.makeText(context, "Elapsed time " + elapsedTime.toString(), Toast.LENGTH_LONG).show();
            //System.out.println("Elapsed Time: " + elapsedTime);
            new PostMetricsTask(context, 2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Activate light and vibration", ips, elapsedTime.toString());
        }else{
            Toast.makeText(context, "Try again!", Toast.LENGTH_LONG).show();
        }
    }
}
