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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import michal.edu.survey.Listeners.FloatResultListener;
import michal.edu.survey.Login.LoginActivity;
import michal.edu.survey.Models.Feedback;
import michal.edu.survey.Models.Section;


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

        dataSource.getAverageForSection(userID, 0, new FloatResultListener() {
            @Override
            public void onResultListener(ArrayList<String> result) {
                tvStatFirstSection.setText(result.get(0));
            }
        });

        dataSource.getAverageForSection(userID, 1, new FloatResultListener() {
            @Override
            public void onResultListener(ArrayList<String> result) {
                tvStatSecondSection.setText(result.get(0));
            }
        });

        dataSource.getAverageForSection(userID, 2, new FloatResultListener() {
            @Override
            public void onResultListener(ArrayList<String> result) {
                tvStatThirdSection.setText(result.get(0));
            }
        });

        tvNameFirstSection.setText(Section.RESTAURANT_APPEARANCE_1);
        tvNameSecondSection.setText(Section.RESTAURANT_STAFF_2);
        tvNameThirdSection.setText(Section.RESTAURANT_FOOD_3);



        chartTest();

        return v;
    }


    private void chartTest() {
        ArrayList<Long> allDatesReverse = new ArrayList<>();
        ArrayList<Long> allDatesCorrect = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");

        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("currentTimeMillis: " + currentTimeMillis);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentTimeMillis);
        c.set(Calendar.MILLISECOND, 0);


        int mDay = c.get(Calendar.DATE);
        int mMonth = c.get(Calendar.MONTH);
        int mYear = c.get(Calendar.YEAR);
        System.out.println("mDay: " + mDay + " mMonth: " + mMonth + " mYear: " + mYear);

        c.set(mYear, mMonth, 1, 0, 0, 0);
        long firstDayOfMonth = c.getTimeInMillis();
        c.setTimeInMillis(firstDayOfMonth);
        System.out.println("firstDayOfMonth: " + firstDayOfMonth);

        String fullDateAndTime = formatter.format(new Date(firstDayOfMonth));
        System.out.println("Full date and Time: " + fullDateAndTime);

        for (int i = 0; i < 5; i++) {
            c.set(Calendar.MONTH, mMonth-i);
            long monthAgo = c.getTimeInMillis();
            allDatesReverse.add(monthAgo);

            System.out.println(monthAgo);
            String monthAgoFull = formatter.format(new Date(monthAgo));
            System.out.println(i + " months ago date: " + monthAgoFull);

            long lastMinuteOfPreviousMonth = monthAgo - 60000;
        }

        for (int i = allDatesReverse.size() - 1; i >= 0; i--){
            allDatesCorrect.add(allDatesReverse.get(i));
        }

        allDatesCorrect.add(currentTimeMillis);

        for (int i = 0; i < allDatesCorrect.size() - 1; i++) {
            String date = formatter.format(new Date(allDatesCorrect.get(i)));
            System.out.println(date);
        }


//            c.add(Calendar.MINUTE, -1);
//            long lastMinuteOfMonth = c.getTimeInMillis();
//        System.out.println(lastMinuteOfMonth);
//            String lastMinute = formatter.format(new Date(lastMinuteOfMonth));
//            System.out.println("last Minute: " + lastMinute);
//
//        System.out.println(monthAgo-lastMinuteOfMonth);




//        c.add(Calendar.MONTH, -5);
//        long timeMillis5MonthsAgo = c.getTimeInMillis();
//        System.out.println("timeMillis5MonthsAgo: " + timeMillis5MonthsAgo);
//        Calendar c1 = Calendar.getInstance();
//        c1.setTimeInMillis(timeMillis5MonthsAgo);
//        int newMonth = c1.get(Calendar.MONTH);
//        int newYear = c1.get(Calendar.YEAR);
//        System.out.println("newMonth: " + newMonth);
//        System.out.println("newYear: " + newYear);

//        c.setTime(date);

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
