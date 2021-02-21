package prj1.stu_1505005.AsyncTasks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import prj1.stu_1505005.LoginActivity;
import prj1.stu_1505005.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import static prj1.stu_1505005.Utils.GlobalVariables.*;

public class RegistrationAsyncTask extends AsyncTask<String, Integer, JSONObject> {
    public static final String REGISTRATION_LOG_TAG = "RegistrationAsyncTask";
    JSONObject jsonObject;
    int success = 0;
    Context context;

    private ProgressDialog progressDialog;

    public RegistrationAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Registration");
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
        Log.e(REGISTRATION_LOG_TAG, "DO IN BACKGROUND REGISTRATION");
        try {
            String registered_response = Jsoup.connect(WEB_SERVICE_END_POINT)
                    .ignoreContentType(true) // To Get JSON DATA
                    .data("op", strings[0])
                    .data("shash", strings[1])
                    .data("un", strings[2])
                    .data("pw", strings[3])
                    .data("phone", strings[4])
                    .data("country", strings[5])
                    .data("dn", strings[6])
                    .post()
                    .text();

            jsonObject = new JSONObject(registered_response);
            Log.e(REGISTRATION_LOG_TAG,"User information's:");
            Log.e(REGISTRATION_LOG_TAG,jsonObject.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.e(REGISTRATION_LOG_TAG, "ON POST EXECUTE REGISTRATION");
        try {
            try {
                LAST_MESSAGE_FROM_SERVER = jsonObject.get("msg").toString();
                success = (int) jsonObject.get("r");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle(R.string.registration_title);
            adb.setMessage(LAST_MESSAGE_FROM_SERVER);
            adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(success == 0) {
                        Toast.makeText(context, R.string.registration_fail, Toast.LENGTH_SHORT).show();
                    } else if(success == 1) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
            adb.create();
            adb.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }
}
