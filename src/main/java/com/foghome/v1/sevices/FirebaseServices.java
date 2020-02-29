package com.foghome.v1.sevices;

import com.foghome.v1.represents.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServices {

    private static Logger Log=Logger.getLogger(FirebaseServices.class);

    public Boolean ifExsits(DocumentReference docRef) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future=docRef.get();
        DocumentSnapshot doc= future.get();
        if (doc.exists())
            return true;
        else
            return false;
    }

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
        DocumentReference documentReference=db.collection("loginfo").document(login.getUserName());
        if (ifExsits(documentReference)){
            ApiFuture<DocumentSnapshot> future=documentReference.get();
            DocumentSnapshot loginDoc=future.get();
            String actualPass=loginDoc.getString("password");
            String givenPass=login.getPassword();
            if (givenPass.equals(actualPass)){
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

    public Response grantAccessTo(GrantAccess grantAccess, String userId, String homeId) throws ExecutionException, InterruptedException {
        Firestore db=FirestoreClient.getFirestore();
        DocumentReference grantUser=db.collection("users").document(grantAccess.getUserId());
        HashMap<String,String> update=new HashMap<>();
        update.put(grantAccess.getUserName(),grantAccess.getUserId());
        if (ifExsits(grantUser)){
            Log.info("updating homes user access array");
            ApiFuture<WriteResult> arrayUserUpdate=db.collection("users").document(userId)
                    .collection("homes").document(homeId).update("user", FieldValue.arrayUnion(update));
            Log.info("adding home name to grant users Homes array");
            ApiFuture<WriteResult> arrayHomesUpdate=db.collection("users").document(grantAccess.getUserId())
                    .update("Homes", FieldValue.arrayUnion(homeId));
            ApiFuture<DocumentSnapshot> grantHome=db.collection("users").document(userId)
                    .collection("homes").document(homeId).get();
            DocumentSnapshot grantHomeDoc=grantHome.get();
            ApiFuture<WriteResult> addGrantHome=db.collection("users").document(grantAccess.getUserId())
                    .collection("homes").document(homeId).set(grantHomeDoc.getData());
            return new Response(null, grantAccess.getUserName()+" has been given access to home "+homeId,null);
        }
        return new Response(null,grantAccess.getUserId()+" does not exists.",null);
    }
}
