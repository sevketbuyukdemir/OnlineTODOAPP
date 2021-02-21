package prj1.stu_1505005;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import prj1.stu_1505005.AsyncTasks.LoginAsyncTask;
import prj1.stu_1505005.Utils.GlobalVariables;
import prj1.stu_1505005.Utils.SharedPrefs;

import static prj1.stu_1505005.Utils.GlobalVariables.MY_STUDENT_HASH;

/**
 * My User Information:
 * id : 8
 * shash : 1505005
 * username : sevketbykdmr
 * password : sevket123
 * phone : 05415382609
 * country : Turkey
 * display name : Şevket Büyükdemir
 */
public class LoginActivity extends AppCompatActivity {
    Context context = this;
    EditText loginUserName;
    EditText loginPassword;
    Button loginButton;
    Button loginRegistrationButton;

    public void init() {
        GlobalVariables.login_activity_context = this;
        loginUserName = findViewById(R.id.loginUserName);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        loginRegistrationButton = findViewById(R.id.loginRegistrationButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        SharedPrefs sharedPrefs = new SharedPrefs(GlobalVariables.login_activity_context);
        if(sharedPrefs.isLoggedIn()) {
            Intent intent = new Intent(context, PostListActivity.class);
            context.startActivity(intent);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "";
                String password = "";
                try {
                    username = loginUserName.getText().toString();
                    password = loginPassword.getText().toString();
                    if(!username.equals("") &&
                            !password.equals("")){
                        new LoginAsyncTask(context).execute("login",
                                username,
                                password,
                                MY_STUDENT_HASH);
                    }
                } catch (Exception e) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    adb.setTitle(R.string.login_title);
                    adb.setMessage(R.string.login_fail);
                    adb.setPositiveButton(R.string.positive_button, null);
                    adb.create();
                    adb.show();
                }
            }
        });

        loginRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.login_title);
        adb.setMessage(R.string.login_back_press_message);
        adb.setNegativeButton(R.string.negative_button, null);
        adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);
            }
        });
        adb.create();
        adb.show();
    }
}