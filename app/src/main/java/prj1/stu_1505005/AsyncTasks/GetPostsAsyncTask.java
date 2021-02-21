package prj1.stu_1505005.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import prj1.stu_1505005.PostListActivity;
import prj1.stu_1505005.Utils.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

import static prj1.stu_1505005.PostListActivity.postArrayList;
import static prj1.stu_1505005.PostListActivity.postsListViewAdapter;
import static prj1.stu_1505005.Utils.GlobalVariables.WEB_SERVICE_END_POINT;

public class GetPostsAsyncTask extends AsyncTask<String, Integer, JSONArray> {
    public static final String GET_POSTS_LOG_TAG = "GetPostsAsyncTask";
    JSONArray jsonArray;
    Context context;

    private ProgressDialog progressDialog;

    public GetPostsAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Get Posts");
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
    protected JSONArray doInBackground(String... strings) {
        Log.e(GET_POSTS_LOG_TAG, "DO IN BACKGROUND GET_POSTS");
        try {
            String posts_response = Jsoup.connect(WEB_SERVICE_END_POINT)
                    .ignoreContentType(true) // To Get JSON DATA
                    .data("op", strings[0])
                    .data("id", strings[1])
                    .data("shash", strings[2])
                    .post()
                    .text();

            jsonArray = new JSONArray(posts_response);
            Log.e(GET_POSTS_LOG_TAG,"Posts:");
            Log.e(GET_POSTS_LOG_TAG,jsonArray.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        Log.e(GET_POSTS_LOG_TAG, "ON POST EXECUTE GET_POSTS");
        try {
            if(jsonArray.length() > 0) {
                Log.e(GET_POSTS_LOG_TAG, "ON POST EXECUTE PUSH POSTS TO ARRAY_LIST");
                postArrayList = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String id = jsonObject.get("id").toString();
                        String post_date = jsonObject.get("post_date").toString();
                        String account_id = jsonObject.get("account_id").toString();
                        String title = jsonObject.get("title").toString();
                        String txt = jsonObject.get("txt").toString();
                        String shash = jsonObject.get("shash").toString();
                        Post post = new Post(id, post_date, account_id, title, txt, shash);
                        postArrayList.add(post);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                postsListViewAdapter.notifyDataSetChanged();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }
}