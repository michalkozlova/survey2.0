package michal.edu.survey;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import michal.edu.survey.Listeners.BranchListener;
import michal.edu.survey.Listeners.FeedbackListener;
import michal.edu.survey.Listeners.FloatResultListener;
import michal.edu.survey.Models.Answer;
import michal.edu.survey.Models.Branch;
import michal.edu.survey.Models.Feedback;
import michal.edu.survey.Models.FullQuestionnaire;
import michal.edu.survey.Models.Question;
import michal.edu.survey.Models.Store;

public class DataSource {

    private static DataSource dataSource;

    public static DataSource getInstance(){
        if (dataSource == null){
            dataSource = new DataSource();
        }

        return dataSource;
    }

    public ArrayList<Branch> getBranchesFromFirebase(String userID, final BranchListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(userID).child("branches");
        final ArrayList<Branch> mBranches = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Branch value = snapshot.getValue(Branch.class);
                    mBranches.add(value);
                }

                if (mBranches.isEmpty()){
                    callback.onBranchCallback(mBranches);
                    System.out.println("no branches");
                }else {
                    callback.onBranchCallback(mBranches);
                    System.out.println(mBranches);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mBranches;
    }

    public ArrayList<String> getStoreNameAndBranches(String userID, final ImageView imageView, final TextView firstLetter, final Context context){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(userID).child("storeName");
        final ArrayList<String> mStoreName = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storeName = dataSnapshot.getValue().toString();
                mStoreName.add(storeName);

                StorageReference storageRef = MyImageStorage.getInstance();
                storageRef.child(storeName+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(context).load(uri).into(imageView);
                        firstLetter.setText("");
                        System.out.println("am i here?");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        firstLetter.setText(Character.toString(mStoreName.get(0).charAt(0)));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mStoreName;
    }

    public FullQuestionnaire getQuestionnaireFromJson(int storeType, Context context){
        int x;
        if (storeType == Store.STORE_RESTAURANT) {
            x = R.raw.sample_questionnaire_restaurant;
        }else {
            x = R.raw.sample_questionnaire_retail;
        }

        InputStream resourceReader = context.getResources().openRawResource(x);
        Writer writer = new StringWriter();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = writer.toString();

        Gson gson = new Gson();
        return gson.fromJson(jsonString, FullQuestionnaire.class);
    }

    //TODO: do i need it?
    public void getTotalAverage(String userID){
//        float ratingSum = (float) 0.0;
//        final int ratingAmount = 0;
//        float yesNoSum = (float) 0.0;
//        int yesNoAmout = 0;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feedbacks").child(userID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float ratingSum = 0;
                float ratingAmount = 0;
                float yesNoSum = 0;
                float yesNoAmout = 0;

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Feedback feedback = child.getValue(Feedback.class);
                    System.out.println(feedback.getBranchName());

                    for (int i = 0; i < 3; i++) {
                        System.out.println(feedback.getAnswerSections().get(i).getSectionName());
                        float ratingSumSection = 0;
                        float ratingAmountSection = 0;
                        float yesNoSumSection = 0;
                        float yesNoAmountSection = 0;

                        ArrayList<Answer> answers = feedback.getAnswerSections().get(i).getAnswers();
                        for (Answer answer : answers) {
                            if (answer.getQuestionType() == Question.ONE_FIVE){
                                ratingSum+=answer.getAnswerValue();
                                ratingAmount++;

                                ratingSumSection+=answer.getAnswerValue();
                                ratingAmountSection++;
                            } else {
                                yesNoSum+=answer.getAnswerValue();
                                yesNoAmout++;

                                yesNoSumSection+=answer.getAnswerValue();
                                yesNoAmountSection++;
                            }
                        }

                        float ratingAverageSection = ratingSumSection / ratingAmountSection;
                        float yesNoAverageSection = yesNoSumSection * 5 / yesNoAmountSection;

                        float a = ratingAverageSection * (ratingAmountSection / (ratingAmountSection + yesNoAmountSection));
                        float b = yesNoAverageSection * (yesNoAmountSection / (ratingAmountSection + yesNoAmountSection));

                        System.out.println("Section final: " + (a+b));
                    }
                }

                System.out.println(ratingSum);
                System.out.println(ratingAmount);
                System.out.println(yesNoSum);
                System.out.println(yesNoAmout);

                float ratingAverage = ratingSum / ratingAmount;
                System.out.println(ratingAverage);
                float yesNoAverage = yesNoSum * 5 / yesNoAmout;
                System.out.println(yesNoAverage);

                float x = ratingAverage * (ratingAmount / (ratingAmount + yesNoAmout));
                System.out.println(x);
                float y = yesNoAverage * (yesNoAmout / (ratingAmount + yesNoAmout));
                System.out.println(y);

                System.out.println("final: " + (x+y));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });
    }

    public ArrayList<String> getAverageForSection(String userID, final int sectionID, final FloatResultListener callback){
        final ArrayList<String> mResults = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feedbacks").child(userID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float ratingSumSection = 0;
                float ratingAmountSection = 0;
                float yesNoSumSection = 0;
                float yesNoAmountSection = 0;

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Feedback feedback = child.getValue(Feedback.class);

                    ArrayList<Answer> answers = feedback.getAnswerSections().get(sectionID).getAnswers();
                    for (Answer answer : answers) {
                        if (answer.getQuestionType() == Question.ONE_FIVE){

                            ratingSumSection+=answer.getAnswerValue();
                            ratingAmountSection++;
                        } else {
                            yesNoSumSection+=answer.getAnswerValue();
                            yesNoAmountSection++;
                        }
                    }
                }

                float ratingAverageSection = ratingSumSection / ratingAmountSection;
                float yesNoAverageSection = yesNoSumSection * 5 / yesNoAmountSection;

                float a = ratingAverageSection * (ratingAmountSection / (ratingAmountSection + yesNoAmountSection));
                float b = yesNoAverageSection * (yesNoAmountSection / (ratingAmountSection + yesNoAmountSection));

                String sectionFinalRating = String.valueOf(a + b);
                String toShow = sectionFinalRating.substring(0, 3);
                System.out.println("Section final: " + (a+b));
                mResults.add(toShow);

                if (mResults.isEmpty()){
                    System.out.println("no results");
                }else{
                    callback.onResultListener(mResults);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });

        return mResults;
    }

    public ArrayList<Feedback> getAllFeedbacks(String userID, final FeedbackListener callback){
        final ArrayList<Feedback> mFeedbacks = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(userID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Feedback feedback = snapshot.getValue(Feedback.class);
                    mFeedbacks.add(feedback);
                }

                if (mFeedbacks.isEmpty()){
                    System.out.println("no feedbacks");
                }else {
                    callback.onFeedbackListener(mFeedbacks);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });

        return mFeedbacks;
    }

    public String getRatingForFeedback(Feedback feedback){
        float ratingSum = 0;
        float ratingAmount = 0;
        float yesNoSum = 0;
        float yesNoAmout = 0;

        for (int i = 0; i < 3; i++) {
            ArrayList<Answer> answers = feedback.getAnswerSections().get(i).getAnswers();
            for (Answer answer : answers) {
                if (answer.getQuestionType() == Question.ONE_FIVE){
                    ratingSum+=answer.getAnswerValue();
                    ratingAmount++;
                } else {
                    yesNoSum+=answer.getAnswerValue();
                    yesNoAmout++;
                }
            }
        }

        float ratingAverage = ratingSum / ratingAmount;
        System.out.println(ratingAverage);
        float yesNoAverage = yesNoSum * 5 / yesNoAmout;
        System.out.println(yesNoAverage);

        float x = ratingAverage * (ratingAmount / (ratingAmount + yesNoAmout));
        System.out.println(x);
        float y = yesNoAverage * (yesNoAmout / (ratingAmount + yesNoAmout));
        System.out.println(y);

        float result = x + y;
        String toShow = String.valueOf(result).substring(0,3);
        return toShow;
    }
}
