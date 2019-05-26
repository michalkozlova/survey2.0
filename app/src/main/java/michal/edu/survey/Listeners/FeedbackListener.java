package michal.edu.survey.Listeners;

import java.util.ArrayList;

import michal.edu.survey.Models.Feedback;

public interface FeedbackListener {
    void onFeedbackListerner(ArrayList<Feedback> feedbacks);
}
