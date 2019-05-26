package michal.edu.survey;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import michal.edu.survey.Login.LoginActivity;
import michal.edu.survey.Login.LoginFragment;
import michal.edu.survey.Models.Feedback;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private Toolbar toolbar;
    private TextView tvStatFirstSection, tvStatSecondSection, tvStatThirdSection;
    private TextView tvNameFirstSection, tvNameSecondSection, tvNameThirdSection;

    public static StatisticsFragment newInstance(String userID) {

        Bundle args = new Bundle();
        args.putSerializable("userID", userID);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_statistics, container, false);

        toolbar = v.findViewById(R.id.toolbar);
        tvStatFirstSection = v.findViewById(R.id.tvStatFirstSection);
        tvStatSecondSection = v.findViewById(R.id.tvStatSecondSection);
        tvStatThirdSection = v.findViewById(R.id.tvStatThirdSection);
        tvNameFirstSection = v.findViewById(R.id.tvNameFirstSection);
        tvNameSecondSection = v.findViewById(R.id.tvNameSecondSection);
        tvNameThirdSection = v.findViewById(R.id.tvNameThirdSection);


        toolbar.inflateMenu(R.menu.main_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_log_out:
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        System.out.println("log out");
                        return true;
                    default:
                        return false;
                }
            }
        });

        String userID = (String) getArguments().getSerializable("userID");

//        queryTest(userID);

        dataSource.getTotalAverage(userID);


        dataSource.getAverageForSection(userID, 0, tvStatFirstSection, tvNameFirstSection);
        dataSource.getAverageForSection(userID, 1, tvStatSecondSection, tvNameSecondSection);
        dataSource.getAverageForSection(userID, 2, tvStatThirdSection, tvNameThirdSection);

        return v;
    }


    private void queryTest(String userID){
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Feedbacks")
                .child(userID);

//        ref.orderByChild("timestamp").startAt(1558471564000L).endAt(1558471680000L).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Feedback feedback = snapshot.getValue(Feedback.class);
//
//                    if (feedback.getCity().equals("Efrat")){
//                        System.out.println(feedback.getComment() + ", City: " + feedback.getCity());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ref.orderByChild("city").equalTo("city").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Feedback feedback = child.getValue(Feedback.class);
                    System.out.println(feedback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
