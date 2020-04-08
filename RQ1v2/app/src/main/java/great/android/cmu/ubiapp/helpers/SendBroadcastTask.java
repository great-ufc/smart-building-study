package great.android.cmu.ubiapp.helpers;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class SendBroadcastTask extends AsyncTask<String, Void, Void> {

    private Context context;

    public SendBroadcastTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... ips) {
        ArrayList<String> devicesIPS = new ArrayList<>(Arrays.asList(ips));
        System.out.println(devicesIPS);
        Utils.sendBroadcast(devicesIPS);
        Long currentTime = System.currentTimeMillis();
        new UDPListenerTask(context, new Gson().toJson(devicesIPS)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentTime);
        return null;
    }
}
