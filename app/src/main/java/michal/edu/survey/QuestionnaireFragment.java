package michal.edu.survey;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionnaireFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();


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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_questionnaire, container, false);

        final int thisStoreType = (int) getArguments().getSerializable("storeType");

        System.out.println(dataSource.getQuestionnaireFromJson(thisStoreType, getContext()));

        return v;
    }

}
