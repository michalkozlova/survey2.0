package michal.edu.survey;


import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import michal.edu.survey.Adapters.BranchAdapter;
import michal.edu.survey.Listeners.BranchListener;
import michal.edu.survey.Models.Branch;


/**
 * A simple {@link Fragment} subclass.
 */
public class BranchesFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private RecyclerView rvBranches;
    private BranchAdapter adapter;
    private TextView firstLetter;
    private ImageView cardImage;
    private Button btnAddBranch;
    private int branchesAmount;
//    private View bottomBar;

    public static BranchesFragment newInstance(String userID) {

        Bundle args = new Bundle();
        args.putSerializable("userID", userID);
        BranchesFragment fragment = new BranchesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_branches, container, false);

        rvBranches = v.findViewById(R.id.rvBranches);
        firstLetter = v.findViewById(R.id.firstLetter);
        cardImage = v.findViewById(R.id.cardImage);
        btnAddBranch = v.findViewById(R.id.btnAddBranch);
//        bottomBar = getActivity().getWindow().findViewById(R.id.navigation);

//        bottomBar.setVisibility(View.VISIBLE);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffFEDC32"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffEA4C5F")));

        final String userID = (String) getArguments().getSerializable("userID");

        showProgress(true);
        dataSource.getBranchesFromFirebase(userID, new BranchListener() {
            @Override
            public void onBranchCallback(ArrayList<Branch> branches) {
                if (branches.isEmpty()){
                    showProgress(false);
                }else {
                    //sort branches with ABC
                    Collections.sort(branches, new Comparator<Branch>() {
                        @Override
                        public int compare(Branch o1, Branch o2) {
                            return o1.getBranchName().compareTo(o2.getBranchName());
                        }
                    });


                    adapter = new BranchAdapter(userID, branches, getActivity());
                    rvBranches.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvBranches.setAdapter(adapter);
                    branchesAmount = branches.size();
                    showProgress(false);
                }
            }
        });

        dataSource.getStoreNameAndBranches(userID, cardImage, firstLetter, getContext());

        btnAddBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, AddBranchFragment.newInstance(branchesAmount))
                        .addToBackStack("")
                        .commit();
            }
        });

        return v;
    }

    ProgressDialog dialog;
    private void showProgress(boolean show){
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());

            dialog.setCancelable(true);
            dialog.setTitle("Please wait");
            dialog.setMessage("Loading...");
        }
        if (show){
            dialog.show();
        }else {
            dialog.dismiss();
        }
    }

}
