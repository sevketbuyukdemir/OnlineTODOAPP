package prj1.stu_1505005;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import prj1.stu_1505005.AsyncTasks.RegistrationAsyncTask;

import static prj1.stu_1505005.Utils.GlobalVariables.MY_STUDENT_HASH;

public class RegistrationActivity extends AppCompatActivity {
    Context context = this;
    EditText enterUsernameEditText;
    EditText enterUserPasswordEditText;
    EditText enterUserDisplayNameEditText;
    EditText enterUserPhoneEditText;
    EditText enterUserCountryEditText;
    Button registrationButton;

    public void init(){
        enterUsernameEditText = findViewById(R.id.enterUsernameEditText);
        enterUserPasswordEditText = findViewById(R.id.enterUserPasswordEditText);
        enterUserDisplayNameEditText = findViewById(R.id.enterUserDisplayNameEditText);
        enterUserPhoneEditText = findViewById(R.id.enterUserPhoneEditText);
        enterUserCountryEditText = findViewById(R.id.enterUserCountryEditText);
        registrationButton = findViewById(R.id.registrationButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username ="";
                String password ="";
                String display_name ="";
                String phone ="";
                String country ="";
                try {
                    username = enterUsernameEditText.getText().toString();
                    password = enterUserPasswordEditText.getText().toString();
                    display_name = enterUserDisplayNameEditText.getText().toString();
                    phone = enterUserPhoneEditText.getText().toString();
                    country = enterUserCountryEditText.getText().toString();

                    if(!username.equals("") &&
                            !password.equals("") &&
                            !display_name.equals("") &&
                            !phone.equals("") &&
                            !country.equals("")){
                        new RegistrationAsyncTask(context).execute("register",
                                MY_STUDENT_HASH,
                                username,
                                password,
                                phone,
                                country,
                                display_name);
                    }
                } catch (Exception e) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    adb.setTitle(R.string.registration_title);
                    adb.setMessage(R.string.registration_input_exception_message);
                    adb.setPositiveButton(R.string.positive_button, null);
                    adb.create();
                    adb.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.registration_title);
        adb.setMessage(R.string.registration_back_press_message);
        adb.setNegativeButton(R.string.negative_button, null);
        adb.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegistrationActivity.super.onBackPressed();
            }
        });
        adb.create();
        adb.show();
    }
}