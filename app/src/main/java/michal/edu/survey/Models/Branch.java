package michal.edu.survey.Models;

import java.io.Serializable;

import michal.edu.survey.Models.Address;

public class Branch implements Serializable {

    private String branchName;
    private String branchPhone;
    private Address branchAddress;

    public Branch() {
    }

    public Branch(String branchName, String branchPhone, Address branchAddress) {
        this.branchName = branchName;
        this.branchPhone = branchPhone;
        this.branchAddress = branchAddress;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
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
                ", branchPhone='" + branchPhone + '\'' +
                ", branchAddress=" + branchAddress +
                '}';
    }
}

