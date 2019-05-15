package michal.edu.survey.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import michal.edu.survey.Models.FullQuestionnaire;
import michal.edu.survey.Models.Section;
import michal.edu.survey.R;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder>{

    private FullQuestionnaire questionnaire;
    private FragmentActivity activity;

    public SectionAdapter(FullQuestionnaire questionnaire, FragmentActivity activity) {
        this.questionnaire = questionnaire;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_section, viewGroup, false);
        return new SectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder sectionViewHolder, int i) {
        Section section = questionnaire.getFullQuestionnaire().get(i);

        sectionViewHolder.tvSectionName.setText(section.getSectionName());

        System.out.println(section.getQuestions());
        QuestionAdapter adapter = new QuestionAdapter(section.getQuestions(), activity);
        sectionViewHolder.rvQuestions.setLayoutManager(new LinearLayoutManager(activity));
        sectionViewHolder.rvQuestions.setAdapter(adapter);


    }

    @Override
    public int getItemCount() {
        return questionnaire.getFullQuestionnaire().size();
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView tvSectionName;
        RecyclerView rvQuestions;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSectionName = itemView.findViewById(R.id.tvSectionName);
            rvQuestions = itemView.findViewById(R.id.rvQuestions);
        }
    }

}
