package michal.edu.survey.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import michal.edu.survey.DataSource;
import michal.edu.survey.Models.Feedback;
import michal.edu.survey.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{

    private DataSource dataSource = DataSource.getInstance();
    private List<Feedback> feedbacks;
    private FragmentActivity activity;

    public FeedbackAdapter(List<Feedback> feedbacks, FragmentActivity activity) {
        this.feedbacks = feedbacks;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_feedback, viewGroup, false);
        return new FeedbackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder feedbackViewHolder, int i) {
        Feedback feedback = feedbacks.get(i);

        feedbackViewHolder.tvBranchName.setText(feedback.getBranchName());

        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");
        String fullDateAndTime = formatter.format(new Date(feedback.getTimestamp()));
        String date = fullDateAndTime.substring(6, 16);
        feedbackViewHolder.tvDate.setText(date);

        feedbackViewHolder.blueButton.setText(dataSource.showResult(dataSource.getRatingForFeedback(feedback)));
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder{

        TextView tvBranchName, tvDate;
        Button blueButton;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBranchName = itemView.findViewById(R.id.tvBranchName);
            tvDate = itemView.findViewById(R.id.tvDate);
            blueButton = itemView.findViewById(R.id.blueButton);
        }
    }

}
