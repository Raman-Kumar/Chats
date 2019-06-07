package com.example.chats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private DocumentReference mDocRef ;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String uid = prefs.getString("email", null);
        if(uid == null) {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
//                new AuthUI.IdpConfig.TwitterBuilder().build()
            );

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
        else {
            mDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        }

        recyclerView = (RecyclerView) findViewById(R.id.friend_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        FriendAdapter mAdapter = new FriendAdapter(new String[]{"Raman", "Guddu", "Shivesh"});
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("email", user.getUid());
                prefsEditor.apply();

                Map<String, Object> docData = new HashMap<>();
                docData.put("stringExample", "Hello world!");
                docData.put("booleanExample", true);
                mDocRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());

                        mDocRef.set(docData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("doc", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("doc", "Error writing document", e);
                            }
                        });
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}
