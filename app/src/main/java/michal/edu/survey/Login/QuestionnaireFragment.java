package michal.edu.survey.Login;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import michal.edu.survey.Adapters.SectionAdapter;
import michal.edu.survey.BranchesFragment;
import michal.edu.survey.DataSource;
import michal.edu.survey.Models.FullQuestionnaire;
import michal.edu.survey.Models.Question;
import michal.edu.survey.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionnaireFragment extends Fragment {

//    private DataSource dataSource = DataSource.getInstance();
//    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    private FullQuestionnaire questionnaire;
//    private RecyclerView rvQuestionnaire;
//    private View bottomBar;
//    private Button btnContinue;


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

        setInitialView(v);

//        btnContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                for (int i = 0; i < 3; i++) {
//                    for (Question question : questionnaire.getFullQuestionnaire().get(i).getQuestions()) {
//                        question.setQuestionID(i + "" + questionnaire.getFullQuestionnaire().get(i).getQuestions().indexOf(question));
//                        System.out.println(question);
//                    }
//                }
//
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(currentUserID).child("questionnaire");
//                reference.setValue(questionnaire.getFullQuestionnaire());
//
//                getActivity()
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, BranchesFragment.newInstance(currentUserID))
//                        .disallowAddToBackStack()
//                        .commit();
//            }
//        });

        return v;
    }

    private void setInitialView(View v){
//        rvQuestionnaire = v.findViewById(R.id.rvQuestionnaire);
//        btnContinue = v.findViewById(R.id.btnContinue);
//        bottomBar = getActivity().getWindow().findViewById(R.id.navigation);

        final int storeType = (int) getArguments().getSerializable("storeType");

//        questionnaire = dataSource.getQuestionnaireFromJson(thisStoreType, getContext());

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffEA4C5F"));

        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.scrollContainer, ScrollViewFragment.newInstance(storeType))
                .commit();

//        SectionAdapter adapter = new SectionAdapter(questionnaire, getActivity());
//        rvQuestionnaire.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvQuestionnaire.setAdapter(adapter);

//        bottomBar.setVisibility(View.GONE);
    }

}
