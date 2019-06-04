package michal.edu.survey;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import michal.edu.survey.Listeners.FeedbackListener;
import michal.edu.survey.Listeners.StringResultListener;
import michal.edu.survey.Login.LoginActivity;
import michal.edu.survey.Models.Feedback;
import michal.edu.survey.Models.Section;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    private static long ONE_MINUTE = 60000;

    private DataSource dataSource = DataSource.getInstance();
    private Toolbar toolbar;
    private TextView tvStatFirstSection, tvStatSecondSection, tvStatThirdSection;
    private TextView tvNameFirstSection, tvNameSecondSection, tvNameThirdSection;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private BarChart chart;
    private Float[] floats = new Float[5];
    private Boolean[] listIsReady = {false, false, false, false, false};

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

        showProgress(true);

        toolbar = v.findViewById(R.id.toolbar);
        tvStatFirstSection = v.findViewById(R.id.tvStatFirstSection);
        tvStatSecondSection = v.findViewById(R.id.tvStatSecondSection);
        tvStatThirdSection = v.findViewById(R.id.tvStatThirdSection);
        tvNameFirstSection = v.findViewById(R.id.tvNameFirstSection);
        tvNameSecondSection = v.findViewById(R.id.tvNameSecondSection);
        tvNameThirdSection = v.findViewById(R.id.tvNameThirdSection);
        chart = v.findViewById(R.id.chart);


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

        dataSource.getAverageForSection(userID, 0, new StringResultListener() {
            @Override
            public void onResultListener(ArrayList<String> result) {
                tvStatFirstSection.setText(result.get(0));
            }
        });

        dataSource.getAverageForSection(userID, 1, new StringResultListener() {
            @Override
            public void onResultListener(ArrayList<String> result) {
                tvStatSecondSection.setText(result.get(0));
            }
        });

        dataSource.getAverageForSection(userID, 2, new StringResultListener() {
            @Override
            public void onResultListener(ArrayList<String> result) {
                tvStatThirdSection.setText(result.get(0));
            }
        });

        tvNameFirstSection.setText(Section.RESTAURANT_APPEARANCE_1);
        tvNameSecondSection.setText(Section.RESTAURANT_STAFF_2);
        tvNameThirdSection.setText(Section.RESTAURANT_FOOD_3);



        ArrayList<Long> dates = dataSource.getDatesFor5Months();

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            dataSource.queryForSpecificMonth(userID, dates.get(i), dates.get(i + 1) - ONE_MINUTE, new FeedbackListener() {
                @Override
                public void onFeedbackListener(ArrayList<Feedback> feedbacks) {
                    if (!feedbacks.isEmpty()) {
                        floats[finalI] = dataSource.getRatingForSeveralFeedbacks(feedbacks);
                    }else {
                        floats[finalI] = 0f;
                    }
                    listIsReady[finalI] = true;
                    setChart();
                }
            });
        }


        return v;
    }


    private void setChart(){
        if (listIsReady[0] && listIsReady[1] && listIsReady[2] && listIsReady[3] && listIsReady[4]){
            System.out.println("now we will make a chart");
            final List<BarEntry> entries = new ArrayList<>();

            for (int i = 0; i < floats.length; i++) {
                entries.add(new BarEntry(i, floats[i]));
            }

            BarDataSet dataSet = new BarDataSet(entries, "Label");
//            dataSet.setColors(new int[]{R.color.blue, R.color.pink, R.color.yellow, R.color.blue, R.color.pink});

            dataSet.setColor(R.color.blue);

            BarData data = new BarData(dataSet);
            data.setBarWidth(0.5f);


            chart.setData(data);
            chart.setFitBars(true);
            chart.invalidate();
        }

        showProgress(false);
    }

    ProgressDialog dialog;
    private void showProgress(boolean show){
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());

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

    private void queryForLast5Months(){
        final SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");
        final ArrayList<Feedback> feedbacks = new ArrayList<>();
        final ArrayList<String> mResults = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Feedbacks")
                .child(userID);

        ArrayList<Long> dates = dataSource.getDatesFor5Months();

        for (int i = 0; i < dates.size()-1; i++) {
            System.out.println(i);
            ref.orderByChild("timestamp").startAt(dates.get(i)).endAt(dates.get(i+1)-60000).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Feedback feedback = snapshot.getValue(Feedback.class);

                        String date = formatter.format(new Date(feedback.getTimestamp()));
                        System.out.println(date);

                        feedbacks.add(feedback);
                    }

                    if (feedbacks.isEmpty()){
                        System.out.println("no feedbacks that time");
                    } else {
                        float averageForMonth = dataSource.getRatingForSeveralFeedbacks(feedbacks);

                        String result = dataSource.showResult(averageForMonth);
                        System.out.println("result " + result);
                        mResults.add(result);

                        feedbacks.clear();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println(databaseError);
                }
            });
        }

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
