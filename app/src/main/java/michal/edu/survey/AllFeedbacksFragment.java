package michal.edu.survey;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import michal.edu.survey.Adapters.FeedbackAdapter;
import michal.edu.survey.Listeners.FeedbackListener;
import michal.edu.survey.Models.Feedback;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllFeedbacksFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private RecyclerView rvFeedbacks;

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

        rvFeedbacks = v.findViewById(R.id.rvFeedbacks);

        final String userID = (String) getArguments().getSerializable("userID");

        dataSource.getAllFeedbacks(userID, new FeedbackListener() {
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
            }
        });

        return v;
    }

}
