package michal.edu.survey.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import michal.edu.survey.Models.Question;
import michal.edu.survey.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{

    private ArrayList<Question> questions;
    private FragmentActivity activity;
    private AlertDialog dialog;

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
        final Question question = questions.get(i);

        questionViewHolder.tvQuestion.setText(question.getQuestionText());

        if (question.getQuestionType() == Question.ONE_FIVE){
            questionViewHolder.ivQuestionType.setImageResource(R.drawable.ic_filter_5);
        }else {
            questionViewHolder.ivQuestionType.setImageResource(R.drawable.ic_check);
        }

        questionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editQuestion(question);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        ImageView ivQuestionType;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            ivQuestionType = itemView.findViewById(R.id.ivQuestionType);

        }
    }


    void editQuestion(final Question question){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Edit the question");

        dialog.setItems(R.array.possibleActionsWithQuestion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        editQuestionText(question);
                        break;
                    case 1:
                        changeQuestionType(question);
                        break;
                    case 2:
                        delete(question);
                        break;
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void delete(final Question question) {
        final int i = questions.indexOf(question);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Are you sure you want to delete question?");

        dialog.setMessage(question.getQuestionText());

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                questions.remove(question);
                notifyItemRemoved(i);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void changeQuestionType(final Question question) {
        final int i = questions.indexOf(question);
        final int[] y = new int[1];
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("QuestionType");

        dialog.setSingleChoiceItems(R.array.questionTypes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                y[0] = which;
            }
        });

        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                question.setQuestionType(y[0]);
                notifyItemChanged(i);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void editQuestionText(final Question question) {
        int i = questions.indexOf(question);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Current text question:");

        builder.setMessage(question.getQuestionText());

        builder.setView(R.layout.edit_question_text);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                if (dialog != null) {
                    EditText editText = dialog.findViewById(R.id.etNewQuestionText);
                    question.setQuestionText(editText.getText().toString());
                    notifyItemChanged(questions.indexOf(question));
                    dialog = null;
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog = builder.show();
    }

}
