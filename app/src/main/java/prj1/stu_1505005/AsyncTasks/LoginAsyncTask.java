package prj1.stu_1505005.AsyncTasks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import prj1.stu_1505005.PostListActivity;
import prj1.stu_1505005.R;
import prj1.stu_1505005.Utils.GlobalVariables;
import prj1.stu_1505005.Utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import static prj1.stu_1505005.Utils.GlobalVariables.LAST_MESSAGE_FROM_SERVER;
import static prj1.stu_1505005.Utils.GlobalVariables.WEB_SERVICE_END_POINT;

public class LoginAsyncTask extends AsyncTask<String, Integer, JSONObject> {
    public static final String LOGIN_LOG_TAG = "LoginAsyncTask";
    JSONObject jsonObject;
    int success = 0;
    Context context;
    private ProgressDialog progressDialog;
    private String pw_val;

    public LoginAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Process continue...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        Log.e(LOGIN_LOG_TAG, "DO IN BACKGROUND LOGIN");
        pw_val = strings[2];
        try {
            String login_response = Jsoup.connect(WEB_SERVICE_END_POINT)
                    .ignoreContentType(true) // To Get JSON DATA
                    .data("op", strings[0])
                    .data("un", strings[1])
                    .data("pw", strings[2])
                    .data("shash", strings[3])
                    .post()
                    .text();

            jsonObject = new JSONObject(login_response);
            Log.e(LOGIN_LOG_TAG,"Login information:");
            Log.e(LOGIN_LOG_TAG,jsonObject.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.e(LOGIN_LOG_TAG, "ON POST EXECUTE LOGIN");
        try{
            try {
                LAST_MESSAGE_FROM_SERVER = jsonObject.get("msg").toString();
                success = (int) jsonObject.get("r");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(success == 1) {
                SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
                sharedPrefs.setLogin();

                String id_val, dn_val, un_val, shash_val, phone_val, country_val;
                try {
                    JSONObject userJSONObject = (JSONObject) jsonObject.get("user");
                    id_val = userJSONObject.get("id").toString();
                    dn_val = userJSONObject.get("display_name").toString();
                    un_val = userJSONObject.get("un").toString();
                    shash_val = jsonObject.get("student_hash").toString();
                    phone_val = userJSONObject.get("phone").toString();
                    country_val = userJSONObject.get("country").toString();
                    sharedPrefs.save("id", id_val);
                    sharedPrefs.save("dn", dn_val);
                    sharedPrefs.save("un", un_val);
                    sharedPrefs.save("pw", pw_val);
                    sharedPrefs.save("shash", shash_val);
                    sharedPrefs.save("phone", phone_val);
                    sharedPrefs.save("country", country_val);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(context, PostListActivity.class);
                context.startActivity(intent);
            } else if(success == 0) {
                Toast.makeText(context, R.string.login_fail, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.login_title);
                adb.setMessage(LAST_MESSAGE_FROM_SERVER);
                adb.setPositiveButton(R.string.positive_button, null);
                adb.create();
                adb.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }


}
