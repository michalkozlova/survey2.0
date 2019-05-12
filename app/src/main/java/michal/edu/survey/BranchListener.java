package michal.edu.survey;

import java.util.ArrayList;

import michal.edu.survey.Models.Branch;

public interface BranchListener {
    void onBranchCallback(ArrayList<Branch> branches);
}
