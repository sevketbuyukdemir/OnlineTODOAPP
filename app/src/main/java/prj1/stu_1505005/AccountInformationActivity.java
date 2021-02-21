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
import android.widget.TextView;

import prj1.stu_1505005.AsyncTasks.ChangePasswordAsyncTask;
import prj1.stu_1505005.Utils.GlobalVariables;
import prj1.stu_1505005.Utils.SharedPrefs;

public class AccountInformationActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    TextView tv_user_id;
    TextView tv_user_dn;
    TextView tv_user_un;
    TextView tv_user_student_hash;
    TextView tv_user_phone;
    TextView tv_user_country;
    TextView tv_user_pw;
    Button turn_posts;
    Button btn_change_password;
    EditText edt_user_change_pw;

    public void init() {
        tv_user_id = findViewById(R.id.tv_user_id);
        tv_user_dn = findViewById(R.id.tv_user_dn);
        tv_user_un = findViewById(R.id.tv_user_un);
        tv_user_student_hash = findViewById(R.id.tv_user_student_hash);
        tv_user_phone = findViewById(R.id.tv_user_phone);
        tv_user_country = findViewById(R.id.tv_user_country);
        tv_user_pw = findViewById(R.id.tv_user_pw);
        turn_posts = findViewById(R.id.btn_turning_post_list_activity_account_information);
        btn_change_password = findViewById(R.id.btn_change_password);
        turn_posts.setOnClickListener(this);
        btn_change_password.setOnClickListener(this);
        edt_user_change_pw = findViewById(R.id.edt_user_change_pw);

        SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
        String id = sharedPrefs.get("id");
        String dn = sharedPrefs.get("dn");
        String un = sharedPrefs.get("un");
        String shash = sharedPrefs.get("shash");
        String phone = sharedPrefs.get("phone");
        String country = sharedPrefs.get("country");
        String pw = sharedPrefs.get("pw");

        tv_user_id.setText(id);
        tv_user_dn.setText(dn);
        tv_user_un.setText(un);
        tv_user_student_hash.setText(shash);
        tv_user_phone.setText(phone);
        tv_user_country.setText(country);
        tv_user_pw.setText(pw);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        init();
    }

    @Override
    public void onClick(View v) {
        int view_id = v.getId();
        if(view_id == R.id.btn_turning_post_list_activity_account_information) {
            Intent intent = new Intent(context, PostListActivity.class);
            startActivity(intent);
        } else if(view_id == R.id.btn_change_password) {
            String new_password = "";
            try {
                new_password = edt_user_change_pw.getText().toString();
            } catch (Exception e) {
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.change_password_error_title);
                adb.setMessage(R.string.change_password_error_message);
                adb.setPositiveButton(R.string.positive_button, null);
                adb.create();
                adb.show();
            }
            if(!new_password.equals("")){
                SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
                String id = sharedPrefs.get("id");
                String opw = sharedPrefs.get("pw");
                String shash = sharedPrefs.get("shash");
                new ChangePasswordAsyncTask(context).execute("change_password",
                        id,
                        opw,
                        new_password,
                        shash);
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(R.string.change_password_error_title);
                adb.setMessage(R.string.change_password_error_message);
                adb.setPositiveButton(R.string.positive_button, null);
                adb.create();
                adb.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.change_password_title);
        adb.setMessage(R.string.change_password_back_press_message);
        adb.setNegativeButton(R.string.negative_button, null);
        adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccountInformationActivity.super.onBackPressed();
            }
        });
        adb.create();
        adb.show();
    }
}