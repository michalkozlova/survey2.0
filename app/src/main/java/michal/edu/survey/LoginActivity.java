package michal.edu.survey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import michal.edu.survey.Login.QuestionnaireFragment;
import michal.edu.survey.Models.Question;
import michal.edu.survey.Models.Section;
import michal.edu.survey.Models.Store;

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
