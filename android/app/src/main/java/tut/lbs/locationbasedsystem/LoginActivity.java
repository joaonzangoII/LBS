package tut.lbs.locationbasedsystem;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AutoCompleteTextView;

import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.requests.AuthRequest;
import tut.lbs.locationbasedsystem.utils.Constant;

public class LoginActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        final Session session = new Session(this);
        final AppCompatButton btnLogin = (AppCompatButton) findViewById(R.id.btn_sign_in);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ((AutoCompleteTextView) findViewById(R.id.email)).getText().toString();
                final String password = ((AutoCompleteTextView) findViewById(R.id.password)).getText().toString();
                AuthRequest.login(session, LoginActivity.this, email, password, requestHandler);
            }
        });

        final AppCompatButton btnRegister = (AppCompatButton) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(RegisterActivity.class);
            }
        });

        final AppCompatButton btnForgotPassword = (AppCompatButton)
                findViewById(R.id.btn_forgot_password);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goToActivity(RecoverPasswordActivity.class);
            }
        });
    }

    private void viewMain(final boolean isLoggedIn) {
        if (isLoggedIn) {
            goToActivity(MapsActivity.class, true);
        }
    }

    final Handler requestHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            final Bundle data = message.getData();
            final boolean isLoggedIn = data.getBoolean(Constant.KEY_IS_LOGGED_IN);
            final boolean success = data.getBoolean(Constant.KEY_SUCCESS);
            if (success) {
                viewMain(isLoggedIn);
            }
            return false;
        }
    });

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
