package michal.edu.survey.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionAnswer implements Serializable {

    private String sectionName;
    private ArrayList<Answer> answers;

    public SectionAnswer(String sectionName, ArrayList<Answer> answers) {
        this.sectionName = sectionName;
        this.answers = answers;
    }

    public SectionAnswer() {
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "SectionAnswer{" +
                "sectionName='" + sectionName + '\'' +
                ", answers=" + answers +
                '}';
    }

}
