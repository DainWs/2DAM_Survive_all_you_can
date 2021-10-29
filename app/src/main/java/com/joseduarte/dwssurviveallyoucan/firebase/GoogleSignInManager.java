package com.joseduarte.dwssurviveallyoucan.firebase;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.StartActivity;
import com.joseduarte.dwssurviveallyoucan.models.User;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

public class GoogleSignInManager {

    public static final int RC_SIGN_IN = 9001;

    private Activity activity;
    private User loggedUser = new User();
    private GoogleSignInClient googleSignInClient;

    private View btn;
    private FirebaseAuth auth;

    public GoogleSignInManager(Activity activity) {
        this.activity = activity;


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, gso);

    }

    public void addListener(View btn) {
        this.btn = btn;
        this.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                activity.startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    public void signInSilenceMode() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(Task<GoogleSignInAccount> task, final FirebaseAuth auth) {
        GoogleSignInAccount account = null;
        this.auth = auth;
        try {
            account = task.getResult(ApiException.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        if(account != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            final GoogleSignInAccount finalAccount = account;
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loggedUser.setMail(finalAccount.getEmail());
                                loggedUser.setCurrentName(finalAccount.getDisplayName());
                                loggedUser.setPhotoURL(finalAccount.getPhotoUrl().toString());
                                loggedUser.setSignIn(true);

                                if(btn != null) addSignOutListener(btn);
                                ((StartActivity) activity).updateUser(loggedUser);
                            }
                        }
                    });
        }
    }

    public void addSignOutListener(View googleSignInBtn) {
        if(googleSignInBtn != null) this.btn = googleSignInBtn;
        this.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                ((StartActivity)activity).restartGoogleBtn();
            }
        });
    }
}

