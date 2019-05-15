package michal.edu.survey.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import michal.edu.survey.Models.Question;
import michal.edu.survey.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{

    private ArrayList<Question> questions;
    private FragmentActivity activity;

    public QuestionAdapter(ArrayList<Question> questions, FragmentActivity activity) {
        this.questions = questions;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_question, viewGroup, false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder questionViewHolder, int i) {
        Question question = questions.get(i);

        questionViewHolder.tvQuestion.setText(question.getQuestionText());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        //Button btnQuestionType;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            //btnQuestionType = itemView.findViewById(R.id.btnQuestionType);
        }
    }

}
