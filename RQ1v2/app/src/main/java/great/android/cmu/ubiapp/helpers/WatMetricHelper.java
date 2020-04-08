package great.android.cmu.ubiapp.helpers;

import java.util.HashMap;
import java.util.Map;

public class WatMetricHelper {

    private static Long WorkingTime;
    private static Long AdaptivityTime;
    private static String FilterName;
    String AdaptName;

    public static Map<String, String> WatMap =  new HashMap<String, String>();


    public WatMetricHelper(String FilterName){
    }


    public static Long getAdaptivityTime() {
        return AdaptivityTime;
    }

    public static void setAdaptivityTime(Long adaptivityTime) {
        AdaptivityTime = adaptivityTime;
    }

    public static Long getWorkingTime() {
        return WorkingTime;
    }

    public static void setWorkingTime(Long workingTime) {
        WorkingTime = workingTime;
    }

    public static String getFilterName() {
        return FilterName;
    }

    public static void setFilterName(String filterName) {
        FilterName = filterName;
    }

    public static void setWatMap(String adaptName, long adaptivityTime, long workingTime){
        //TimeMap.put(adaptivityTime, workingTime);
       //myMap.put(adaptName, new Map<adaptivityTime, workingTime>);

        WatMap.put(adaptName, String.valueOf(adaptivityTime+";"+workingTime) );
    }


}
