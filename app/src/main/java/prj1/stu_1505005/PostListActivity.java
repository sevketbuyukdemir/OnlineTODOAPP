package prj1.stu_1505005;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.appbar.MaterialToolbar;
import prj1.stu_1505005.AsyncTasks.DeletePostAsyncTask;
import prj1.stu_1505005.AsyncTasks.GetPostsAsyncTask;
import prj1.stu_1505005.Utils.GlobalVariables;
import prj1.stu_1505005.Utils.Post;
import prj1.stu_1505005.Utils.SharedPrefs;
import prj1.stu_1505005.ViewAdapters.PostsListViewAdapter;

import java.util.ArrayList;

public class PostListActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    Context context = this;
    MaterialToolbar post_list_action_bar;
    ListView posts_list_view;
    public static PostsListViewAdapter postsListViewAdapter;
    public static ArrayList<Post> postArrayList;

    public void init(){
        post_list_action_bar = findViewById(R.id.post_list_action_bar);
        post_list_action_bar.setOnMenuItemClickListener(this);
        posts_list_view = findViewById(R.id.posts_list_view);
        postArrayList = new ArrayList<>();
        postsListViewAdapter = new PostsListViewAdapter(context);
        posts_list_view.setAdapter(postsListViewAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        init();

        SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
        String id = sharedPrefs.get("id");
        String shash = sharedPrefs.get("shash");
        new GetPostsAsyncTask(context).execute("list_posts", id, shash);

        posts_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int selectedIndex = position;
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.delete_post_alert_title);
                adb.setMessage(R.string.delete_post_alert_message);
                adb.setNegativeButton(R.string.negative_button, null);
                adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        new DeletePostAsyncTask(context).execute("del_post",
                                postArrayList.get(selectedIndex).getAccount_id(),
                                postArrayList.get(selectedIndex).getStudent_hash(),
                                postArrayList.get(selectedIndex).getId());
                        dialog.dismiss();
                    }
                });
                adb.create();
                adb.show();
                return true;
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int item_id = item.getItemId();
        if(item_id == R.id.account_information_menu) {
            Intent account_information_post = new Intent(context, AccountInformationActivity.class);
            startActivity(account_information_post);
            return true;
        } else if(item_id == R.id.add_post_menu) {
            Intent intent_add_post = new Intent(context, AddPostActivity.class);
            startActivity(intent_add_post);
            return true;
        } else if(item_id == R.id.log_out_menu) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle(R.string.post_list_screen_toolbar_title);
            adb.setMessage(R.string.post_list_back_press_message);
            adb.setNegativeButton(R.string.negative_button, null);
            adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
                    sharedPrefs.clearStoredData();
                    Intent intent_login = new Intent(context, LoginActivity.class);
                    startActivity(intent_login);
                }
            });
            adb.create();
            adb.show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.post_list_screen_toolbar_title);
        adb.setMessage(R.string.post_list_back_press_message);
        adb.setNegativeButton(R.string.negative_button, null);
        adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
                sharedPrefs.clearStoredData();
                Intent intent_login = new Intent(context, LoginActivity.class);
                startActivity(intent_login);
            }
        });
        adb.create();
        adb.show();
    }
}