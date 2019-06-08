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
import android.view.View;

import java.util.ArrayList;
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
    ArrayList<Friend> mFriendList;


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

        mFriendList = new ArrayList<>();
        mFriendList.add(new Friend("Raman", "data:image/jpeg;base64,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"));
        mFriendList.add(new Friend("Guddu", "https://www.jiocinema.com/assets/img/facebook-Hover.png"));
        mFriendList.add(new Friend("Shivesh", "https://www.jiocinema.com/assets/img/facebook-Hover.png"));

        // specify an adapter (see also next example)
        FriendAdapter mAdapter = new FriendAdapter(mFriendList, this);
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

    public void addFriend(View view) {
        Intent intent = new Intent(this, AddFriend.class);
        startActivity(intent);
    }
}
