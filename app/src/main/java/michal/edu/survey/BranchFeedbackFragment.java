package michal.edu.survey;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import michal.edu.survey.Adapters.FeedbackAdapter;
import michal.edu.survey.Listeners.FeedbackListener;
import michal.edu.survey.Models.Branch;
import michal.edu.survey.Models.Feedback;

/**
 * A simple {@link Fragment} subclass.
 */
public class BranchFeedbackFragment extends Fragment {

    private static long ONE_MINUTE = 60000;

    private DataSource dataSource = DataSource.getInstance();
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView tvToolbar;
    private BarChart chart;
    private RecyclerView rvFeedbacks;
    private Float[] floats = new Float[5];
    private Boolean[] listIsReady = {false, false, false, false, false};


    public static BranchFeedbackFragment newInstance(Branch branch) {
        
        Bundle args = new Bundle();
        args.putSerializable("branch", branch);
        BranchFeedbackFragment fragment = new BranchFeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_feedbacks, container, false);

        tvToolbar = v.findViewById(R.id.tvToolbar);
        chart = v.findViewById(R.id.chart);
        rvFeedbacks = v.findViewById(R.id.rvFeedbacks);

        final Branch branch = (Branch) getArguments().getSerializable("branch");

        tvToolbar.setText(branch.getBranchName() + " STATISTICS");

        dataSource.getAllFeedbacks(userID, branch.getBranchName(), new FeedbackListener() {
            @Override
            public void onFeedbackListener(ArrayList<Feedback> feedbacks) {
                //TODO: not the right thing to do
                ArrayList<Feedback> revFeedbacks = new ArrayList<>();
                for (int i = feedbacks.size()-1; i >=0 ; i--) {
                    revFeedbacks.add(feedbacks.get(i));
                }

                FeedbackAdapter adapter = new FeedbackAdapter(revFeedbacks, getActivity());
                rvFeedbacks.setLayoutManager(new LinearLayoutManager(getContext()));
                rvFeedbacks.setAdapter(adapter);

                ArrayList<Long> dates = dataSource.getDatesFor5Months();

                for (int i = 0; i < 5; i++) {
                    final int finalI = i;
                    ArrayList<Feedback> oneMonthFeedbacks = new ArrayList<>();

                    for (Feedback feedback : feedbacks) {
                        if (feedback.getTimestamp() >= dates.get(i) && feedback.getTimestamp() < dates.get(i+1)){
                            oneMonthFeedbacks.add(feedback);
                        }
                    }

                    if (!oneMonthFeedbacks.isEmpty()){
                        floats[finalI] = dataSource.getRatingForSeveralFeedbacks(oneMonthFeedbacks);
                    }else {
                        floats[finalI] = 0f;
                    }
                    listIsReady[finalI] = true;
                    setChart();
                }
            }
        });



        return  v; 
    }


    private void setChart(){
        if (listIsReady[0] && listIsReady[1] && listIsReady[2] && listIsReady[3] && listIsReady[4]){
            System.out.println("now we will make a chart");
            final List<BarEntry> entries = new ArrayList<>();
            int[] colors = new int[5];

            for (int i = 0; i < floats.length; i++) {
                entries.add(new BarEntry(i, floats[i]));
                int color;
                if (floats[i] >= 4){
                    color = Color.argb(255, 29, 142, 9);
                    colors[i] = color;
                } else if (floats[i] < 4 && floats[i] > 3){
                    color = Color.argb(255, 252, 204, 0);
                    colors[i] = color;
                } else {
                    color = Color.argb(255, 255, 54, 88);
                    colors[i] = color;
                }
            }

            BarDataSet dataSet = new BarDataSet(entries, "Last 5 months");

            dataSet.setLabel("");

            dataSet.setColors(colors);

            BarData data = new BarData(dataSet);
            data.setBarWidth(0.5f);


            chart.setData(data);
            chart.setFitBars(true);
            chart.invalidate();
        }

        YAxis left = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true);

        left.setAxisMinimum(0f);
        left.setAxisMaximum(5f);
        left.setGranularity(1f);

        left.setTextColor(Color.RED);



        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(14f);


        long currentTimeMillis = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentTimeMillis);
        int mMonth = c.get(Calendar.MONTH);
        System.out.println("lastMonth: " + mMonth);

        xAxis.setValueFormatter(new MyXAxisFormatter(mMonth + 1));

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
}
