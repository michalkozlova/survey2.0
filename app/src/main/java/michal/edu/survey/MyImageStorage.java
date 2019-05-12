package michal.edu.survey;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//TODO: check singletonClass
public class MyImageStorage {
    private static StorageReference myImageStorage;

    public static StorageReference getInstance(){
        if (myImageStorage == null){
            myImageStorage = FirebaseStorage.getInstance().getReference();
        }

        return myImageStorage;
    }
}
