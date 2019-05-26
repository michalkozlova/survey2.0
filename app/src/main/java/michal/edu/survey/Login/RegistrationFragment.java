package michal.edu.survey.Login;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import michal.edu.survey.Models.Store;
import michal.edu.survey.Models.User;
import michal.edu.survey.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private EditText etFirstName, etLastName, etEmail, etStoreName, etPassword, etConfirmPassword;
    private Spinner spinnerStoreType;
    private Button btnContinue;
//    private View bottomBar;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        setInitialView(v);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return v;
    }

    private void setInitialView(View v){
        etFirstName = v.findViewById(R.id.etFirstName);
        etLastName = v.findViewById(R.id.etLastName);
        etEmail = v.findViewById(R.id.etEmail);
        etStoreName = v.findViewById(R.id.etStoreName);
        etPassword = v.findViewById(R.id.etPassword);
        etConfirmPassword = v.findViewById(R.id.etConfirmPassword);
        spinnerStoreType = v.findViewById(R.id.spinnerStoreType);
        btnContinue = v.findViewById(R.id.btnContinue);
//        bottomBar = getActivity().getWindow().findViewById(R.id.navigation);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.store_type, R.layout.spinner_item_type);
        adapter.setDropDownViewResource(R.layout.spinner_item_type);
        spinnerStoreType.setAdapter(adapter);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffEA4C5F"));

//        bottomBar.setVisibility(View.GONE);
    }

    private String firstName() {
        return etFirstName.getText().toString();
    }
    private String lastName() {
        return etLastName.getText().toString();
    }
    private String email() {
        return etEmail.getText().toString();
    }
    private int storeType(){
        return spinnerStoreType.getSelectedItemPosition()-1;
    }
    private String storeName() {
        return etStoreName.getText().toString();
    }
    private String password() {
        return etPassword.getText().toString();
    }
    private String confirmedPassword() {
        return etConfirmPassword.getText().toString();
    }

    private boolean isFirstNameValid() {
        if (firstName().isEmpty()) {
            etFirstName.setError("Please put first name");
            return false;
        } else {
            return true;
        }
    }
    private boolean isLastNameValid() {
        if (lastName().isEmpty()) {
            etLastName.setError("Please put last name");
            return false;
        } else {
            return true;
        }
    }
    private boolean isEmailValid() {
        if (email().isEmpty()) {
            etEmail.setError("Please put email address");
            return false;
        }
        boolean isEmailValid = email().matches(Patterns.EMAIL_ADDRESS.pattern());
        if (!isEmailValid) {
            etEmail.setError("Invalid email address");
        }
        return isEmailValid;
    }
    private boolean isStoreNameValid(){
        if (storeName().isEmpty()) {
            etFirstName.setError("Please put store name");
            return false;
        } else {
            return true;
        }
    }
    private boolean isPasswordValid() {
        if (password().isEmpty()) {
            etPassword.setError("PLease put password");
            return false;
        }
        boolean isPasswordValid = password().length() >= 6;
        if (!isPasswordValid) {
            etPassword.setError("Password should have at least 6 characters");
        }
        return isPasswordValid;
    }
    private boolean isPasswordConfirmed() {
        if (confirmedPassword().isEmpty()) {
            etConfirmPassword.setError("Please confirm password");
            return false;
        } else if (!confirmedPassword().equals(password())) {
            etConfirmPassword.setError("Password is not the same");
            return false;
        } else {
            return true;
        }
    }

    ProgressDialog dialog;
    private void showProgress(boolean show) {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());

            dialog.setCancelable(true);
            dialog.setTitle("Please wait");
            dialog.setMessage("You will be registered soon!");
        }
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    private void register(){
        if (!isFirstNameValid() | !isLastNameValid() | !isEmailValid() | !isStoreNameValid() | !isPasswordValid() | !isPasswordConfirmed()) {
            return;
        }

        showProgress(true);

        Task<AuthResult> task = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email(), password());
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                User newUser = new User(firstName(), lastName(), email());
                Store newStore = new Store(storeName(), storeType(), userId);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                ref.child("Stores").child(userId).setValue(newStore);
                ref.child("Users").child(userId).setValue(newUser);

                showProgress(false);

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, QuestionnaireFragment.newInstance(storeType()))
                        .disallowAddToBackStack()
                        .commit();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showProgress(false);
                Snackbar.make(btnContinue, e.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

}
