package michal.edu.survey;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import michal.edu.survey.Models.Branch;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder>{

    private String userID;
    private List<Branch> branches;
    private FragmentActivity activity;

    public BranchAdapter(String userID, List<Branch> branches, FragmentActivity activity) {
        this.userID = userID;
        this.branches = branches;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_branch, viewGroup, false);
        return new BranchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder branchViewHolder, int i) {
        final Branch branch = branches.get(i);

        branchViewHolder.smallWhiteButton.setText(branch.getBranchName());

        branchViewHolder.smallWhiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(activity, "hey-hey!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder{

        final Button smallWhiteButton;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);

            smallWhiteButton = (Button) itemView.findViewById(R.id.smallWhiteButton);
        }
    }
}
