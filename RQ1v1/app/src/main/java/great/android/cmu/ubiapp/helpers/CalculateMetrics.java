package great.android.cmu.ubiapp.helpers;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


public class CalculateMetrics {

    static ArrayList<Long> TaTimes = new ArrayList();
    static ArrayList<Long> GeneralWatTimes = new ArrayList();
    static int numberOfRulesVerified = 0;

    public static void calculateSingleWat(String adaptationName, Long WorkingTime, Long AdaptivityTime){
        System.out.println("Name of Adaptation: " + adaptationName + " the WAT calculus = " + (WorkingTime/AdaptivityTime));
        Log.d("WAT", "Name of Adaptation: " + adaptationName + " the WAT calculus = " + (WorkingTime/AdaptivityTime));
    }

    public static void calculateSingleWat(Context context,String adaptationName, Long WorkingTime, Long AdaptivityTime){
        Toast.makeText(context,"Name of Adaptation: " + adaptationName + " the WAT calculus = " + (WorkingTime/AdaptivityTime), Toast.LENGTH_LONG).show();
        System.out.println("Name of Adaptation: " + adaptationName + " the WAT calculus = " + (WorkingTime/AdaptivityTime));
        Log.d("WAT", "Name of Adaptation: " + adaptationName + " the WAT calculus = " + (WorkingTime/AdaptivityTime));
    }


    public static void calculateGeneralWat(){
        Long GeneralWATTimeVariable = null;
        for(int i = 0; i < GeneralWatTimes.size(); i++){
            GeneralWATTimeVariable += GeneralWatTimes.get(i);
        }

        System.out.println("General Wat Calculated: " + GeneralWATTimeVariable);
        Log.d("GENERAL WAT", "General Wat Calculated: " + GeneralWATTimeVariable);
    }

    public static Long getCalculatedGeneralWat(){
        Long GeneralWATTimeVariable = 0L;

        if (!GeneralWatTimes.isEmpty()) {
            for (int i = 0; i < GeneralWatTimes.size(); i++) {
                GeneralWATTimeVariable += GeneralWatTimes.get(i);

            }
            Log.d("GENERAL WAT", "General Wat Calculated: " + GeneralWATTimeVariable);
            return GeneralWATTimeVariable;
        } else{
            return 1L;
        }
    }

    public static void calculateGeneralWat(Context context){

        Long GeneralWATTimeVariable = null;
        for(int i = 0; i < GeneralWatTimes.size(); i++){
            GeneralWATTimeVariable += GeneralWatTimes.get(i);
        }

        Toast.makeText(context,"General Wat Calculated: " + GeneralWATTimeVariable, Toast.LENGTH_LONG).show();
        System.out.println("General Wat Calculated: " + GeneralWATTimeVariable );
        Log.d("GENERAL WAT", "General Wat Calculated: " + GeneralWATTimeVariable );

    }

    public static void setGeneralWatTimes(long WorkingTime, long AdaptivityTime){
        Long auxWorkingTime = new Long(WorkingTime);
        Long auxAdaotivityTime = new Long(AdaptivityTime);

        System.out.println("[WAT: " + WorkingTime + ", TA: " + AdaptivityTime + "]");

        if(auxWorkingTime.equals(0)){
            System.out.println("aux1 igual a 0");
            GeneralWatTimes.add(0L);
        }
        if (auxAdaotivityTime.equals(0)) {
            System.out.println("aux2 igual a 0");
            GeneralWatTimes.add(0L);
        }else{
            System.out.println("ELSE - [WAT: " + WorkingTime + ", TA: " + AdaptivityTime + "]");
            if(AdaptivityTime != 0){
                GeneralWatTimes.add(WorkingTime / AdaptivityTime);
            }
        }
    }

    public static void setTATimes(Long taTimeOnThisMoment){
        TaTimes.add(taTimeOnThisMoment);
    }

    public static void calculateTA(){
        Long GeneralTAVariable = null;
        for(int i = 0; i < TaTimes.size(); i++){
            GeneralTAVariable +=TaTimes.get(i);
        }

        System.out.println("General TA Calculated: " + GeneralTAVariable );
        Log.d("GENERAL TA", "General TA Calculated: " + GeneralTAVariable );
    }

    public static Long getCalculatedTAs(){
        Long GeneralTAVariable = 0L;

        if(!TaTimes.isEmpty()){
            for(int i = 0; i < TaTimes.size(); i++){
                GeneralTAVariable += TaTimes.get(i);
            }
            Log.d("GET GENERAL TA", "General TA Calculated: " + GeneralTAVariable );
            return GeneralTAVariable;
        } else{
            return Long.valueOf(1);
        }
    }


    public static void calculateTA(Context context){
        Long GeneralTAVariable = null;
        for(int i = 0; i < TaTimes.size(); i++){
            GeneralTAVariable += GeneralWatTimes.get(i);
        }

        Toast.makeText(context,"General TA Calculated: " + GeneralTAVariable, Toast.LENGTH_LONG).show();
        System.out.println("General TA Calculated: " + GeneralTAVariable);
        Log.d("GENERAL TA", "General TA Calculated: " + GeneralTAVariable);
    }

    public static void printAllTA(){
        for(int i = 0; i < TaTimes.size(); i++) {
            System.out.println(TaTimes.get(i));
            Log.d("PRINT ALL TA", "ALL TA LIST: " + TaTimes.get(i));
        }
    }

    public static void setNumberOfRulesVerified(){
        numberOfRulesVerified++;
    }

    public static int getNumberOfRulesVerified(){
        return numberOfRulesVerified;
    }

//    public static void calculateExecutionTime(long timeOfStart, long timeOfEnd){
//        System.out.println("Start: " + timeOfStart);
//        System.out.println("End: " + timeOfEnd);
//        System.out.println("The Adaptation has end in this time :" + (timeOfEnd -timeOfStart));
//    }

    public static long calculateExecutionTime(long timeOfStart, long timeOfEnd){
        System.out.println("Start: " + timeOfStart);
        System.out.println("End: " + timeOfEnd);
        System.out.println("The Adaptation has end in this time :" + (timeOfEnd - timeOfStart));
        return (timeOfEnd - timeOfStart);
    }
}
