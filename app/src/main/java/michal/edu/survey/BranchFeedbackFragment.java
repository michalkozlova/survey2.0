package michal.edu.survey;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
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
                        floats[finalI] = dataSource.getRatingForSeveralFeedbacks(feedbacks);
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
}
