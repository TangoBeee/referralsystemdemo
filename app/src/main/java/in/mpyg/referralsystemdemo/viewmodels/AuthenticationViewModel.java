package in.mpyg.referralsystemdemo.viewmodels;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import in.mpyg.referralsystemdemo.repository.AuthenticationRepository;

public class AuthenticationViewModel extends AndroidViewModel {

    private final AuthenticationRepository authenticationRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> loggedStatusMutableLiveData;
    private final MutableLiveData<Boolean> otpSendStatusMutableLiveData;

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);

        authenticationRepository = new AuthenticationRepository(application);
        userMutableLiveData = authenticationRepository.getFirebaseUserMutableLiveData();
        loggedStatusMutableLiveData = authenticationRepository.getUserLoggedStatusMutableLiveData();
        otpSendStatusMutableLiveData = authenticationRepository.getOtpSendStatusMutableLiveData();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedStatusMutableLiveData() {
        return loggedStatusMutableLiveData;
    }

    public MutableLiveData<Boolean> getOtpSendStatusMutableLiveData() {
        return otpSendStatusMutableLiveData;
    }

    public void login(String phoneNumber, Activity activity) {
        authenticationRepository.phoneAuthentication(phoneNumber, activity);
    }

    public void verifyCode(String code, Activity activity) {
        authenticationRepository.verifyCode(code, activity);
    }

    public void signout() {
        authenticationRepository.signout();
    }

}
