package michal.edu.survey.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import michal.edu.survey.MainActivity;
import michal.edu.survey.R;

public class LoginActivity extends AppCompatActivity {

    private int someInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkIfLoggedIn();
        System.out.println(someInt);
    }

    private void checkIfLoggedIn() {
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                boolean isUserLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
                //TODO: showProgress
                if (!isUserLoggedIn){
                    someInt = 1;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.loginContainer, new LoginFragment())
                            .commitAllowingStateLoss();
                }else {
                    if (someInt == 0) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
    }


    //TODO: do i need it here?
    ProgressDialog dialog;
    private void showProgress(boolean show){
        if (dialog == null) {
            dialog = new ProgressDialog(this);

            dialog.setCancelable(true);
            dialog.setTitle("Please wait");
            dialog.setMessage("Loading...");
        }
        if (show){
            dialog.show();
        }else {
            dialog.dismiss();
        }
    }
}
