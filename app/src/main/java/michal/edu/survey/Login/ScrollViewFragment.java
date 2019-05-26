package michal.edu.survey.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import michal.edu.survey.Adapters.SectionAdapter;
import michal.edu.survey.DataSource;
import michal.edu.survey.Models.FullQuestionnaire;
import michal.edu.survey.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollViewFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private RecyclerView rvQuestionnaire;
    private Button btnContinue;
    private FullQuestionnaire questionnaire;
    private int storeType;

    public static ScrollViewFragment newInstance(int storeType) {

        Bundle args = new Bundle();
        args.putSerializable("storeType", storeType);
        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_scroll_view, container, false);

        rvQuestionnaire = v.findViewById(R.id.rvQuestionnaire);
        btnContinue = v.findViewById(R.id.btnContinue);


        storeType = (int) getArguments().getSerializable("storeType");
        questionnaire = dataSource.getQuestionnaireFromJson(storeType, getContext());

        SectionAdapter adapter = new SectionAdapter(questionnaire, getActivity());
        rvQuestionnaire.setLayoutManager(new LinearLayoutManager(getContext()));
        rvQuestionnaire.setAdapter(adapter);

        return v;
    }

}
