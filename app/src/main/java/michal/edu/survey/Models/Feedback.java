package michal.edu.survey.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Feedback implements Serializable {

    private String customerID;
    private String storeID;
    private String branchName;
    private long timestamp;
    private String comment;
    private String city;
    private ArrayList<SectionAnswer> answerSections;

    public Feedback(String customerID, String storeID, String branchName, long timestamp, String comment, String city, ArrayList<SectionAnswer> answerSections) {
        this.customerID = customerID;
        this.storeID = storeID;
        this.branchName = branchName;
        this.timestamp = timestamp;
        this.comment = comment;
        this.city = city;
        this.answerSections = answerSections;
    }

    public Feedback() {
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<SectionAnswer> getAnswerSections() {
        return answerSections;
    }

    public void setAnswerSections(ArrayList<SectionAnswer> answerSections) {
        this.answerSections = answerSections;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "customerID='" + customerID + '\'' +
                ", storeID='" + storeID + '\'' +
                ", branchName='" + branchName + '\'' +
                ", timestamp=" + timestamp +
                ", comment='" + comment + '\'' +
                ", city='" + city + '\'' +
                ", answerSections=" + answerSections +
                '}';
    }

}
