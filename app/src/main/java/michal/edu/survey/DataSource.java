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

import michal.edu.survey.Models.Branch;
import michal.edu.survey.Models.FullQuestionnaire;
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

}
