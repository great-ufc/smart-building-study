package great.android.cmu.ubiapp.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;



public class PostMetricsTask extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private String URL;

    private String[] keys;

    public PostMetricsTask(Context context, int type){
        this.context = context;

        if(type == 1){
            URL = "https://docs.google.com/forms/d/e/1FAIpQLSe9kYO3qQRmt-EgNCk5tHOv_p80Ta9cpfF0pHxZODWtdIotPg/formResponse";
            this.keys = new String[]{"entry.1011940443", "entry.527634828", "entry.1685283592"};
        }else if(type == 2){
            URL = "https://docs.google.com/forms/d/e/1FAIpQLSfbi5MDgaLaZGZcugUf7C5X3DKJVF5mx_sZqksF20wNmNybZQ/formResponse";
            this.keys = new String[]{"entry.1760472624", "entry.834188860", "entry.2078865570"};
        }
    }

    @Override
    protected Boolean doInBackground(String... metrics) {
        Boolean result = true;
        String postBody = "";

        try {
            //all values must be URL encoded to make sure that special characters like & | ",etc. do not cause problems
            for(int i = 0; i < metrics.length; i++){
                postBody += this.keys[i] + "=" + URLEncoder.encode(metrics[i],"UTF-8") + "&";
            }
        } catch (UnsupportedEncodingException ex) {
            result = false;
        }

        try{
            //Create OkHttpClient for sending request
            OkHttpClient client = new OkHttpClient();
            //Create the request body with the help of Media Type
            RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
            Request request = new Request.Builder().url(URL).post(body).build();
            //Send the request
            Response response = client.newCall(request).execute();
        }catch (IOException exception){
            result = false;
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result){
        //Toast.makeText(this.context, "Result: " + result, Toast.LENGTH_LONG).show();
    }
}
