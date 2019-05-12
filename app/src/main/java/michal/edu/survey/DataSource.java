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
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import michal.edu.survey.Models.Branch;

public class DataSource {

    private static DataSource dataSource;

    public static DataSource getInstance(){
        if (dataSource == null){
            dataSource = new DataSource();
        }

        return dataSource;
    }

    public ArrayList<Branch> getBranchesFromFirebase(String userID, final BranchListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(userID).child("Branches");
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mBranches;
    }

    public ArrayList<String> getStoreName(String userID, final ImageView imageView, final TextView firstLetter, final Context context){
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

    public void getStoreLogoFromFirebase(final String storeName, final ImageView imageView, final TextView firstLetter, final Context context){
        StorageReference storageRef = MyImageStorage.getInstance();

        storageRef.child(storeName+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(imageView);
                firstLetter.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firstLetter.setText(Character.toString(storeName.charAt(0)));
            }
        });
    }
}
