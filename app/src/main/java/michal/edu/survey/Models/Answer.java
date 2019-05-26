package michal.edu.survey.Models;

import java.io.Serializable;

public class Answer implements Serializable {

    private String questionID;
    private int questionType;
    private float answerValue;

    public Answer() {
    }


    public Answer(String questionID, int questionType, float answerValue) {
        this.questionID = questionID;
        this.questionType = questionType;
        this.answerValue = answerValue;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public float getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(float answerValue) {
        this.answerValue = answerValue;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "questionID='" + questionID + '\'' +
                ", questionType=" + questionType +
                ", answerValue=" + answerValue +
                '}';
    }

}
