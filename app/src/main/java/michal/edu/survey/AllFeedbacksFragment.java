package michal.edu.survey;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import michal.edu.survey.Listeners.FeedbackListener;
import michal.edu.survey.Models.Feedback;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllFeedbacksFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();

    public static AllFeedbacksFragment newInstance(String userID) {

        Bundle args = new Bundle();
        args.putSerializable("userID", userID);
        AllFeedbacksFragment fragment = new AllFeedbacksFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_feedbacks, container, false);

        final String userID = (String) getArguments().getSerializable("userID");

        dataSource.getAllFeedbacks(userID, new FeedbackListener() {
            @Override
            public void onFeedbackListerner(ArrayList<Feedback> feedbacks) {
                System.out.println(feedbacks);
            }
        });

        return v;
    }

}
