package com.foghome.v1.sevices;

import com.foghome.v1.represents.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServices {

    private static Logger Log=Logger.getLogger(FirebaseServices.class);

    public void addUser(User user){
        Firestore db= FirestoreClient.getFirestore();
        user.setUserId();
        ApiFuture<WriteResult> newUser= db.collection("users").document(user.getUserId())
                .collection("userInfo").document(user.getUsername()).set(user);
        Loginfo login=new Loginfo(user.getUsername(),user.getPassword(),user.getUserId());
        ApiFuture<WriteResult> newUserLogin= db.collection("loginfo").document(user.getUsername()).set(login);
        Log.info("User "+user.getUsername()+" has been successfully added");
    }

    public Response loginUser(Login login) throws ExecutionException, InterruptedException {
        Firestore db=FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> loginFuture=db.collection("loginfo").document(login.getUserName()).get();
        DocumentSnapshot loginDoc=loginFuture.get();
        if (loginDoc.exists()){
            String actualPass= (String) loginDoc.get("password");
            String givenPass=login.getPassword();
            if (actualPass.equals(givenPass)) {
                Log.info(login.getUserName() + " has succeccfully logged in.");
                return new Response(loginDoc.getString("userId"),
                        login.getUserName() + " has succeccfully logged in.",null);
            }
            else {
                Log.error("Wrong Password.");
                return new Response(null, "Wrong password.",null);
            }
        }
        Log.error(login.getUserName()+" does not exists. Please sign Up.");
        return new Response(null,login.getUserName()+" does not exists. Please sign Up.",null);
    }

    public Response listhomes(String userId) throws ExecutionException, InterruptedException {
        Firestore db=FirestoreClient.getFirestore();
        DocumentReference userDocRef=db.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> userFuture=userDocRef.get();
        DocumentSnapshot userDoc=userFuture.get();
        if (userDoc.exists()){
            ApiFuture<DocumentSnapshot> listHomesFuture=userDocRef.get();
            DocumentSnapshot userHomes=listHomesFuture.get();
            ArrayList<String> listHomes=(ArrayList<String>) userHomes.get("Homes");
            return new Response(null,"success",listHomes);
        }
        return new Response(null,userId+" does not exists.",null);
    }

    public Response addHome(Home home, String userId){
        Firestore db=FirestoreClient.getFirestore();
        ArrayList<String> list=new ArrayList<>();
        list.add(home.getHomeName());
        HashMap<String,ArrayList<String>> addedHomes=new HashMap<String, ArrayList<String>>();
        ApiFuture<WriteResult> homes=db.collection("users").document(userId).collection("homes")
                .document(home.getHomeName()).set(home);
        addedHomes.put("Homes",list);
        ApiFuture<WriteResult> userHomesList=db.collection("users").document(userId).set(addedHomes);
        return new Response(null,"Successfully created home"+home.getHomeName(),list);
    }
}
