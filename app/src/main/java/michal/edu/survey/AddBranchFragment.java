package michal.edu.survey;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import michal.edu.survey.Listeners.BranchListener;
import michal.edu.survey.Models.Address;
import michal.edu.survey.Models.Branch;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBranchFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private Button btnDone;
    private EditText etBranchName, etStreet, etNum, etCity;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public static AddBranchFragment newInstance(int branchPosition) {

        Bundle args = new Bundle();
        args.putSerializable("branchPosition", branchPosition);
        AddBranchFragment fragment = new AddBranchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_add_branch, container, false);

        btnDone = v.findViewById(R.id.btnDone);
        etBranchName = v.findViewById(R.id.etBranchName);
        etStreet = v.findViewById(R.id.etStreet);
        etNum = v.findViewById(R.id.etNum);
        etCity = v.findViewById(R.id.etCity);

        final int branchPosition = (int) getArguments().getSerializable("branchPosition");

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNameValid() | !isStreetValid() | !isCityValid() | !isNumValid()){
                    return;
                }

                showProgress(true);

                Address newAddress = new Address(city(), street(), Integer.valueOf(num()));
                Branch newBranch = new Branch(branchName(), newAddress);


                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                String key = ref.push().getKey();

                DatabaseReference reference = FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Stores")
                        .child(userID)
                        .child("branches")
                        .child(key);
                reference.setValue(newBranch);



                dataSource.getBranchesFromFirebase(userID, new BranchListener() {
                    @Override
                    public void onBranchCallback(ArrayList<Branch> branches) {
                        if (branches.size() == 1){
                            ref.child("Stores").child(userID).child("hasBranches").setValue(true);
                        }

                        showProgress(false);

                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, BranchesFragment.newInstance(userID))
                                .disallowAddToBackStack()
                                .commit();

                    }
                });


            }
        });

        return v;
    }

    private String branchName(){
        return etBranchName.getText().toString();
    }
    private String street(){
        return etStreet.getText().toString();
    }
    private String city(){
        return etCity.getText().toString();
    }
    private String num(){
        return etNum.getText().toString();
    }

    private boolean isNameValid() {
        if (branchName().isEmpty()) {
            etBranchName.setError("Please put the name");
            return false;
        } else {
            return true;
        }
    }
    private boolean isStreetValid() {
        if (street().isEmpty()) {
            etStreet.setError("Please put the phone street");
            return false;
        } else {
            return true;
        }
    }
    private boolean isCityValid() {
        if (city().isEmpty()) {
            etCity.setError("Please put the city");
            return false;
        } else {
            return true;
        }
    }
    private boolean isNumValid() {
        if (num().isEmpty()) {
            etNum.setError("Please put the number");
            return false;
        } else {
            return true;
        }
    }

    ProgressDialog dialog;
    private void showProgress(boolean show){
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());

            dialog.setCancelable(true);
            dialog.setTitle("Please wait");
            dialog.setMessage("Saving...");
        }
        if (show){
            dialog.show();
        }else {
            dialog.dismiss();
        }
    }

}
