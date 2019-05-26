package michal.edu.survey.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import michal.edu.survey.Adapters.SectionAdapter;
import michal.edu.survey.DataSource;
import michal.edu.survey.MainActivity;
import michal.edu.survey.Models.FullQuestionnaire;
import michal.edu.survey.Models.Question;
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

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 3; i++) {

                    //set questionID for all questions
                    for (Question question : questionnaire.getFullQuestionnaire().get(i).getQuestions()) {
                        question.setQuestionID(i + "" + questionnaire.getFullQuestionnaire().get(i).getQuestions().indexOf(question));
                        System.out.println(question);
                    }

                    String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(currentUserID).child("questionnaire");
                    reference.setValue(questionnaire.getFullQuestionnaire());

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        return v;
    }

}
