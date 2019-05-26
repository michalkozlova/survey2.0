package michal.edu.survey.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import michal.edu.survey.Models.Store;
import michal.edu.survey.R;

public class LoginActivity extends AppCompatActivity {

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
