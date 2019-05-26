package michal.edu.survey.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Question implements Serializable {

    public static final int YES_NO = 0;
    public static final int ONE_FIVE = 1;

    @SerializedName("question")
    private String questionText;
    @SerializedName("type")
    private int questionType;
    private String questionID = "";

    public Question(String questionText, int questionType, String questionID) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.questionID = questionID;
    }

    public Question() {
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", questionType=" + questionType +
                ", questionID=" + questionID +
                '}';
    }
}
