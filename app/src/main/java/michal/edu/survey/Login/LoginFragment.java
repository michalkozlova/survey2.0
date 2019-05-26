package michal.edu.survey.Login;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import michal.edu.survey.Models.Store;
import michal.edu.survey.R;
import michal.edu.survey.StatisticsFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegistration;
//    private View bottomBar;
    private FragmentManager manager;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        setInitialView(v);

        manager = getFragmentManager();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistration();
            }
        });

        return v;
    }

    private void setInitialView(View v){
        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);
        btnLogin = v.findViewById(R.id.btnContinue);
        btnRegistration = v.findViewById(R.id.btnRegistration);
//        bottomBar = getActivity().getWindow().findViewById(R.id.navigation);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffEA4C5F"));

//        bottomBar.setVisibility(View.GONE);
    }

    private void goToRegistration() {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new RegistrationFragment())
                .addToBackStack("")
                .commit();
    }

    private void login() {
        if (!isEmailValid() | !isPasswordValid()){
            return;
        }

        showProgress(true);

        Task<AuthResult> task = FirebaseAuth.getInstance().signInWithEmailAndPassword(email(), password());
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                showProgress(false);
//                bottomBar.setVisibility(View.VISIBLE);

                manager
                        .beginTransaction()
                        .replace(R.id.container, StatisticsFragment.newInstance(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        .disallowAddToBackStack()
                        .commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showError(e);
            }
        });
    }

    private void showError(@NonNull Exception e) {
        showProgress(false);
        Snackbar.make(btnLogin, e.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

//    private void proceedToStatistics() {
//        showProgress(false);
//        bottomBar.setVisibility(View.VISIBLE);
//
//        getActivity()
//                .getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, new StatisticsFragment())
//                .disallowAddToBackStack()
//                .commit();
//    }

    String email(){return etEmail.getText().toString();}
    String password(){return etPassword.getText().toString();}

    ProgressDialog dialog;
    private void showProgress(boolean show){
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());

            dialog.setCancelable(true);
            dialog.setTitle("Please wait");
            dialog.setMessage("Logging you in...");
        }
        if (show){
            dialog.show();
        }else {
            dialog.dismiss();
        }
    }

    private boolean isEmailValid(){
        if (email().isEmpty()){
            etEmail.setError("Please put your email");
            return false;
        }
        boolean isEmailValid = email().matches(Patterns.EMAIL_ADDRESS.pattern());
        if(!isEmailValid){
            etEmail.setError("Invalid email address");
        }
        return isEmailValid;
    }

    private boolean isPasswordValid(){
        if (password().isEmpty()){
            etPassword.setError("Please put your password");
            return false;
        }
        boolean isPasswordValid = password().length() >= 6;
        if (!isPasswordValid){
            etPassword.setError("Password must contain at least 6 characters");
        }
        return isPasswordValid;
    }

    public static class LoginActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.loginContainer, QuestionnaireFragment.newInstance(Store.STORE_RESTAURANT))
                    .commit();
        }
    }
}
