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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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


        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffFEDC32"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffEA4C5F")));

        final String userID = (String) getArguments().getSerializable("userID");

        showProgress(true);
        dataSource.getBranchesFromFirebase(userID, new BranchListener() {
            @Override
            public void onBranchCallback(ArrayList<Branch> branches) {
                adapter = new BranchAdapter(userID, branches, getActivity());
                rvBranches.setLayoutManager(new LinearLayoutManager(getContext()));
                rvBranches.setAdapter(adapter);
                showProgress(false);
            }
        });

        //dataSource.getStoreLogoFromFirebase(dataSource.getStoreName(userID).get(0), cardImage, firstLetter, getContext());

        dataSource.getStoreName(userID, cardImage, firstLetter, getContext());

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
