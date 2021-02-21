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

public class AddPostAsyncTask extends AsyncTask<String, Integer, JSONObject> {
    public static final String ADD_POST_LOG_TAG = "AddPostAsyncTask";
    JSONObject jsonObject;
    int success = 0;
    Context context;

    private ProgressDialog progressDialog;

    public AddPostAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Add Post");
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
        Log.e(ADD_POST_LOG_TAG, "DO IN BACKGROUND ADD_POST");
        try {
            String add_post_response = Jsoup.connect(WEB_SERVICE_END_POINT)
                    .ignoreContentType(true) // To Get JSON DATA
                    .data("op", strings[0])
                    .data("id", strings[1])
                    .data("shash", strings[2])
                    .data("title", strings[3])
                    .data("txt", strings[4])
                    .post()
                    .text();

            jsonObject = new JSONObject(add_post_response);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.e(ADD_POST_LOG_TAG, "ON POST EXECUTE ADD_POST");
        try {
            if(((int)jsonObject.get("r")) == 1){
                String msg = jsonObject.get("msg").toString();
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.add_post_title);
                adb.setMessage(msg);
                adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
                        String id = sharedPrefs.get("id");
                        String shash = sharedPrefs.get("shash");
                        new GetPostsAsyncTask(context).execute("list_posts", id, shash);
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
