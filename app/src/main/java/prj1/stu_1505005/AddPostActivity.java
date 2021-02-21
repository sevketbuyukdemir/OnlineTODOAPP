package prj1.stu_1505005;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import prj1.stu_1505005.AsyncTasks.AddPostAsyncTask;
import prj1.stu_1505005.Utils.GlobalVariables;
import prj1.stu_1505005.Utils.SharedPrefs;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    Button btn_turning_post_list_activity;
    Button btn_save_post;
    EditText edt_text_title;
    EditText edt_text_text;

    public void init(){
        btn_turning_post_list_activity = findViewById(R.id.btn_turning_post_list_activity);
        btn_save_post = findViewById(R.id.btn_save_post);
        edt_text_title = findViewById(R.id.edt_text_title);
        edt_text_text = findViewById(R.id.edt_text_text);
        btn_turning_post_list_activity.setOnClickListener(this);
        btn_save_post.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();
    }

    @Override
    public void onClick(View v) {
        int view_id = v.getId();
        if(view_id == R.id.btn_turning_post_list_activity){
            Intent intent = new Intent(context, PostListActivity.class);
            startActivity(intent);
        } else if(view_id == R.id.btn_save_post){
            String title = "";
            String text = "";
            try{
                title = edt_text_title.getText().toString();
                text = edt_text_text.getText().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(!title.equals("") && !text.equals("")) {
                SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
                String id = sharedPrefs.get("id");
                String shash = sharedPrefs.get("shash");
                new AddPostAsyncTask(context).execute("add_post",
                        id,
                        shash,
                        title,
                        text);
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.add_post_error_title);
                adb.setMessage(R.string.add_post_error_message);
                adb.setPositiveButton(R.string.positive_button, null);
                adb.create();
                adb.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.add_post_title);
        adb.setMessage(R.string.add_post_back_press_message);
        adb.setNegativeButton(R.string.negative_button, null);
        adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddPostActivity.super.onBackPressed();
            }
        });
        adb.create();
        adb.show();
    }
}