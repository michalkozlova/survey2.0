package michal.edu.survey.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Section implements Serializable {

    private final static String STORE_APPEARANCE = "Store Appearance";
    private final static String STORE_CLERK = "Store Clerk";
    private final static String STORE_MERCHANDISE = "Merchandise";

    private final static String RESTAURANT_APPEARANCE = "Restaurant Appearance";
    private final static String RESTAURANT_STAFF = "Restaurant Staff";
    private final static String RESTAURANT_FOOD = "Food";

    @SerializedName("sectionTitle")
    private String sectionName;
    @SerializedName("listOfQuestions")
    private ArrayList<Question> questions;

    public Section(String sectionName, ArrayList<Question> questions) {
        this.sectionName = sectionName;
        this.questions = questions;
    }

    public Section() {
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", questions=" + questions +
                '}';
    }
}
