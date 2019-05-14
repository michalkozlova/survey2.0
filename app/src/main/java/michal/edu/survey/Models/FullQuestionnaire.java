package michal.edu.survey.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FullQuestionnaire {

    @SerializedName("questionnaire")
    private ArrayList<Section> fullQuestionnaire;

    public FullQuestionnaire(ArrayList<Section> fullQuestionnaire) {
        this.fullQuestionnaire = fullQuestionnaire;
    }

    public FullQuestionnaire() {
    }

    public ArrayList<Section> getFullQuestionnaire() {
        return fullQuestionnaire;
    }

    public void setFullQuestionnaire(ArrayList<Section> fullQuestionnaire) {
        this.fullQuestionnaire = fullQuestionnaire;
    }

    @Override
    public String toString() {
        return "FullQuestionnaire{" +
                "fullQuestionnaire=" + fullQuestionnaire +
                '}';
    }
}
