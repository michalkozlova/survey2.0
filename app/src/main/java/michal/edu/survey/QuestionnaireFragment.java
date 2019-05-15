package michal.edu.survey;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import michal.edu.survey.Adapters.QuestionAdapter;
import michal.edu.survey.Adapters.SectionAdapter;
import michal.edu.survey.Models.FullQuestionnaire;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionnaireFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private FullQuestionnaire questionnaire;
    private RecyclerView rvQuestionnaire;


    public static QuestionnaireFragment newInstance(int storeType) {

        Bundle args = new Bundle();
        args.putSerializable("storeType", storeType);
        QuestionnaireFragment fragment = new QuestionnaireFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questionnaire, container, false);

        rvQuestionnaire = v.findViewById(R.id.rvQuestionnaire);

        final int thisStoreType = (int) getArguments().getSerializable("storeType");

        System.out.println(dataSource.getQuestionnaireFromJson(thisStoreType, getContext()));

        questionnaire = dataSource.getQuestionnaireFromJson(thisStoreType, getContext());

        SectionAdapter adapter = new SectionAdapter(questionnaire, getActivity());
        rvQuestionnaire.setLayoutManager(new LinearLayoutManager(getContext()));
        rvQuestionnaire.setAdapter(adapter);

        return v;
    }

}
