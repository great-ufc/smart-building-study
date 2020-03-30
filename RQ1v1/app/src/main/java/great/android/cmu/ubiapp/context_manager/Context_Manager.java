package great.android.cmu.ubiapp.context_manager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import great.android.cmu.ubiapp.workflow.MainWorkflow;
import workflow.Workflow;

public class Context_Manager extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Worker worker = new Worker(startId);
        worker.run();
        return (super.onStartCommand(intent, flags, startId));
    }

    class Worker extends Thread{
        public int count = 0;
        public int startId;
        public boolean active = true;

        public Worker(int startId){

            this.startId = startId;
        }


        public void run(){
            while(active && count < 10){
                changeContext();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                count++;
            }
            stopSelf(startId);
        }

        private void changeContext(){
            //MainWorkflow.observeContexts(generateRandom(30, 0), generateRandom(24, 0), generateRandom(100,0));
            MainWorkflow.observeContexts(generateRandom(24,0));
        }


        private int generateRandom(int up, int down){
            return ((int)(Math.random()* up+down));
        }


    }

}

