package michal.edu.survey.Models;

import java.io.Serializable;

import michal.edu.survey.Models.Address;

public class Branch implements Serializable {

    private String branchName;
    private Address branchAddress;

    public Branch() {
    }

    public Branch(String branchName, Address branchAddress) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Address getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(Address branchAddress) {
        this.branchAddress = branchAddress;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branchName='" + branchName + '\'' +
                ", branchAddress=" + branchAddress +
                '}';
    }
}

