package in.mpyg.referralsystemdemo.repository;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthenticationRepository {

    final Application application;
    private final MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private final MutableLiveData<Boolean> userLoggedStatusMutableLiveData;
    private final MutableLiveData<Boolean> otpSendStatusMutableLiveData;

    private final FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String verificationOTP;

    public AuthenticationRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userLoggedStatusMutableLiveData = new MutableLiveData<>();
        otpSendStatusMutableLiveData = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            firebaseUserMutableLiveData.postValue(mAuth.getCurrentUser());
        }
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedStatusMutableLiveData() {
        return userLoggedStatusMutableLiveData;
    }

    public MutableLiveData<Boolean> getOtpSendStatusMutableLiveData() {
        return otpSendStatusMutableLiveData;
    }

    //Phone Auth Start from here --------------------->
    public void phoneAuthentication(String phoneNumber, Activity activity) {

        phoneAuthCallback();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void phoneAuthCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {}

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {}

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                verificationOTP = verificationId;

                otpSendStatusMutableLiveData.postValue(true);
            }
        };
    }


    public void verifyCode(String userCode, Activity activity) {
        PhoneAuthCredential mCredential = PhoneAuthProvider.getCredential(verificationOTP, userCode);

        signInWithPhoneAuthCredential(mCredential, activity);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, Activity activity) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if(task.isSuccessful()) {
                        firebaseUserMutableLiveData.postValue(mAuth.getCurrentUser());
                    }
                    else {
                        Log.e(TAG, "signInWithPhoneAuthCredential: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void signout() {
        mAuth.signOut();
        firebaseUserMutableLiveData.postValue(null);
        otpSendStatusMutableLiveData.postValue(false);
        userLoggedStatusMutableLiveData.postValue(true);
    }

}
