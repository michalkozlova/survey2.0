package michal.edu.survey;


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

    public static long ONE_MINUTE = 60000;

    private DataSource dataSource = DataSource.getInstance();
    private Toolbar toolbar;
    private TextView tvStatFirstSection, tvStatSecondSection, tvStatThirdSection;
    private TextView tvNameFirstSection, tvNameSecondSection, tvNameThirdSection;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private BarChart chart;
    private Boolean monthsAgo5 = false;
    private Boolean monthsAgo4 = false;
    private Boolean monthsAgo3 = false;
    private Boolean monthsAgo2 = false;
    private Boolean monthsAgo = false;
    Float[] floats = new Float[5];
    private Float januryFL, februaryFL, marchFL, aprilFL, mayFL;

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



        ArrayList<Long> dates = getDatesFor5Months();

//        queryForLast5Months();

        ArrayList<Feedback> january = queryForSpecificMonth(dates.get(0), dates.get(1) - ONE_MINUTE, new FeedbackListener() {
            @Override
            public void onFeedbackListener(ArrayList<Feedback> feedbacks) {
                if (!feedbacks.isEmpty()) {
                    floats[0] = dataSource.getRatingForSeveralFeedbacks(feedbacks);
                }else {
                    floats[0] = 0f;
                }
                monthsAgo5 = true;
                setChart();
            }
        });


        ArrayList<Feedback> february = queryForSpecificMonth(dates.get(1), dates.get(2) - ONE_MINUTE, new FeedbackListener() {
            @Override
            public void onFeedbackListener(ArrayList<Feedback> feedbacks) {
                if (!feedbacks.isEmpty()) {
                    floats[1] = dataSource.getRatingForSeveralFeedbacks(feedbacks);
                }else {
                    floats[1] = 0f;
                }
                monthsAgo4 = true;
                setChart();
            }
        });


        ArrayList<Feedback> march = queryForSpecificMonth(dates.get(2), dates.get(3) - ONE_MINUTE, new FeedbackListener() {
            @Override
            public void onFeedbackListener(ArrayList<Feedback> feedbacks) {
                if (!feedbacks.isEmpty()) {
                    floats[2] = dataSource.getRatingForSeveralFeedbacks(feedbacks);
                }else {
                    floats[2] = 0f;
                }
                monthsAgo3 = true;
                setChart();
            }
        });


        ArrayList<Feedback> april = queryForSpecificMonth(dates.get(3), dates.get(4) - ONE_MINUTE, new FeedbackListener() {
            @Override
            public void onFeedbackListener(ArrayList<Feedback> feedbacks) {
                if (!feedbacks.isEmpty()) {
                    floats[3] = dataSource.getRatingForSeveralFeedbacks(feedbacks);
                }else {
                    floats[3] = 0f;
                }
                monthsAgo2 = true;
                setChart();
            }
        });


        ArrayList<Feedback> may = queryForSpecificMonth(dates.get(4), dates.get(5), new FeedbackListener() {
            @Override
            public void onFeedbackListener(ArrayList<Feedback> feedbacks) {

                if (!feedbacks.isEmpty()) {
                    floats[4] = dataSource.getRatingForSeveralFeedbacks(feedbacks);
                }

                monthsAgo = true;
                setChart();
            }
        });

//        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                setChart();
//            }
//        }, 0, 1, TimeUnit.SECONDS);



        return v;
    }


    private void setChart(){
//        if (!monthsAgo | !monthsAgo2 | !monthsAgo3 | !monthsAgo4 | monthsAgo5){
//            System.out.println("not yet: monthsAgo=" + monthsAgo + "; monthsAgo2=" + monthsAgo2 + "; " +
//                    "monthsAgo3=" + monthsAgo3 + "; monthsAgo4=" + monthsAgo4 + "; monthsAgo5=" + monthsAgo5);
//            return;
//        }

        if (monthsAgo && monthsAgo2 && monthsAgo3 && monthsAgo4 && monthsAgo5){
            System.out.println("now we will make a chart");
            final List<BarEntry> entries = new ArrayList<>();

//            entries.add(new BarEntry(1f, januryFL));
//            entries.add(new BarEntry(2f, februaryFL));
//            entries.add(new BarEntry(3f, marchFL));
//            entries.add(new BarEntry(4f, aprilFL));
//            entries.add(new BarEntry(5f, mayFL));

            for (int i = 0; i < floats.length; i++) {
                entries.add(new BarEntry(i, floats[i]));
            }

            BarDataSet dataSet = new BarDataSet(entries, "Label");
            dataSet.setColor(Color.MAGENTA);

            BarData data = new BarData(dataSet);
            data.setBarWidth(0.5f);


            chart.setData(data);
            chart.setFitBars(true);
            chart.invalidate();
        }


    }

    private ArrayList<Long> getDatesFor5Months() {
        ArrayList<Long> allDatesReverse = new ArrayList<>();
        ArrayList<Long> allDatesCorrect = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");

        long currentTimeMillis = System.currentTimeMillis();
//        System.out.println("currentTimeMillis: " + currentTimeMillis);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentTimeMillis);
        c.set(Calendar.MILLISECOND, 0);


        int mDay = c.get(Calendar.DATE);
        int mMonth = c.get(Calendar.MONTH);
        int mYear = c.get(Calendar.YEAR);
//        System.out.println("mDay: " + mDay + " mMonth: " + mMonth + " mYear: " + mYear);

        c.set(mYear, mMonth, 1, 0, 0, 0);
        long firstDayOfMonth = c.getTimeInMillis();
        c.setTimeInMillis(firstDayOfMonth);
//        System.out.println("firstDayOfMonth: " + firstDayOfMonth);

        String fullDateAndTime = formatter.format(new Date(firstDayOfMonth));
//        System.out.println("Full date and Time: " + fullDateAndTime);

        for (int i = 0; i < 5; i++) {
            c.set(Calendar.MONTH, mMonth-i);
            long monthAgo = c.getTimeInMillis();
            allDatesReverse.add(monthAgo);

//            System.out.println(monthAgo);
            String monthAgoFull = formatter.format(new Date(monthAgo));
            System.out.println(i + " months ago date: " + monthAgoFull);

            long lastMinuteOfPreviousMonth = monthAgo - 60000;
        }

        for (int i = allDatesReverse.size() - 1; i >= 0; i--){
            allDatesCorrect.add(allDatesReverse.get(i));
        }

        allDatesCorrect.add(currentTimeMillis);


        System.out.println(allDatesCorrect);

        for (int i = 0; i < allDatesCorrect.size(); i++) {
            String date = formatter.format(new Date(allDatesCorrect.get(i)));
            System.out.println(date);
        }

    return allDatesCorrect;


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

    private ArrayList<Feedback> queryForSpecificMonth(long firstDayOfMonth, long lastDayOfMonth, final FeedbackListener callback){
        final ArrayList<Feedback> mResult = new ArrayList<>();
        final SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");
        final String firstDate = formatter.format(new Date(firstDayOfMonth));
        final String lastDate = formatter.format(new Date(lastDayOfMonth));

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Feedbacks")
                .child(userID);

        ref.orderByChild("timestamp").startAt(firstDayOfMonth).endAt(lastDayOfMonth).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Feedback feedback = snapshot.getValue(Feedback.class);
                    mResult.add(feedback);
                }

                if (mResult.isEmpty()){
                    System.out.println(firstDate + " - " + lastDate);
                    System.out.println("no feedbacks that time");
                    callback.onFeedbackListener(mResult);
                }else {
                    System.out.println(firstDate + " - " + lastDate);
                    callback.onFeedbackListener(mResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });

        return mResult;
    }

    private void queryForLast5Months(){
        final SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");
        final ArrayList<Feedback> feedbacks = new ArrayList<>();
        final ArrayList<String> mResults = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Feedbacks")
                .child(userID);

        ArrayList<Long> dates = getDatesFor5Months();

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
