package michal.edu.survey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import michal.edu.survey.Login.LoginFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_list_branches:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, BranchesFragment.newInstance("WCnPCx747rNWF2bMEXqFWz8Caq63"))
                            .commit();
                    return true;
                case R.id.navigation_statistics:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, StatisticsFragment.newInstance("WCnPCx747rNWF2bMEXqFWz8Caq63"))
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.navigation_statistics);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null){
            System.out.println("null?!!");
        }else {
            System.out.println(currentUser.getUid());
        }

        checkIfLoggedIn();
    }

    private void checkIfLoggedIn(){
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                boolean isLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;

                if(!isLoggedIn){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
                }else {
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Stores").child(currentUserID()).child("questionnaire");
//                    final ArrayList<Section> mQuestionnaire = new ArrayList<>();
//                    ref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                Section value = snapshot.getValue(Section.class);
//                                mQuestionnaire.add(value);
//                            }
//
//                            if (mQuestionnaire.isEmpty()){
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.container, new QuestionnaireFragment())
//                                        .commit();
//                            } else {
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.container, new StatisticsFragment())
//                                        .commit();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, StatisticsFragment.newInstance(currentUserID())).commit();
                }
            }
        });
    }

    private String currentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
