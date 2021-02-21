package prj1.stu_1505005.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import prj1.stu_1505005.PostListActivity;
import prj1.stu_1505005.R;
import prj1.stu_1505005.Utils.GlobalVariables;
import prj1.stu_1505005.Utils.SharedPrefs;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import static prj1.stu_1505005.Utils.GlobalVariables.WEB_SERVICE_END_POINT;

public class ChangePasswordAsyncTask extends AsyncTask<String, Integer, JSONObject> {
    public static final String CHANGE_PASSWORD_LOG_TAG = "ChangePasswordAsyncTask";
    JSONObject jsonObject;
    Context context;

    private ProgressDialog progressDialog;

    String npw;

    public ChangePasswordAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Change Password");
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
        Log.e(CHANGE_PASSWORD_LOG_TAG, "DO IN BACKGROUND CHANGE_PASSWORD");
        npw = strings[3];
        try {
            String change_password_response = Jsoup.connect(WEB_SERVICE_END_POINT)
                    .ignoreContentType(true) // To Get JSON DATA
                    .data("op", strings[0])
                    .data("id", strings[1])
                    .data("opw", strings[2])
                    .data("npw", strings[3])
                    .data("shash", strings[4])
                    .post()
                    .text();

            jsonObject = new JSONObject(change_password_response);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.e(CHANGE_PASSWORD_LOG_TAG, "ON POST EXECUTE CHANGE_PASSWORD");
        try {
            if(((int)jsonObject.get("r")) == 1){
                SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
                sharedPrefs.save("pw", npw);

                String msg = jsonObject.get("msg").toString();
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.change_password_title);
                adb.setMessage(msg);
                adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, PostListActivity.class);
                        context.startActivity(intent);
                    }
                });
                adb.create();
                adb.show();
            } else if(((int)jsonObject.get("r")) == 0) {
                String msg = jsonObject.get("msg").toString();
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.change_password_title);
                adb.setMessage(msg);
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
