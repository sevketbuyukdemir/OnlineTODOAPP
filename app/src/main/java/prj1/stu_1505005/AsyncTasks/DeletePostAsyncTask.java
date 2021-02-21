package prj1.stu_1505005.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import prj1.stu_1505005.Utils.GlobalVariables;
import prj1.stu_1505005.Utils.SharedPrefs;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import static prj1.stu_1505005.Utils.GlobalVariables.WEB_SERVICE_END_POINT;

public class DeletePostAsyncTask  extends AsyncTask<String, Integer, JSONObject> {
    public static final String DELETE_POST_LOG_TAG = "DeletePostAsyncTask";
    JSONObject jsonObject;
    Context context;

    private ProgressDialog progressDialog;

    public DeletePostAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Delete Post");
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
        Log.e(DELETE_POST_LOG_TAG, "DO IN BACKGROUND DELETE_POST");
        try {
            String delete_post_response = Jsoup.connect(WEB_SERVICE_END_POINT)
                    .ignoreContentType(true) // To Get JSON DATA
                    .data("op", strings[0])
                    .data("account_id", strings[1])
                    .data("shash", strings[2])
                    .data("post_id", strings[3])
                    .post()
                    .text();

            jsonObject = new JSONObject(delete_post_response);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.e(DELETE_POST_LOG_TAG, "ON POST EXECUTE DELETE_POST");
        progressDialog.dismiss();
        SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
        String id = sharedPrefs.get("id");
        String shash = sharedPrefs.get("shash");
        new GetPostsAsyncTask(context).execute("list_posts", id, shash);
    }
}
