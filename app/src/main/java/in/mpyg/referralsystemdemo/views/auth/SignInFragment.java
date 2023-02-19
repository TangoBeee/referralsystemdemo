package in.mpyg.referralsystemdemo.views.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import in.mpyg.referralsystemdemo.R;
import in.mpyg.referralsystemdemo.databinding.FragmentSignInBinding;
import in.mpyg.referralsystemdemo.viewmodels.AuthenticationViewModel;

public class SignInFragment extends Fragment {

    private FragmentSignInBinding signInBinding;

    private AuthenticationViewModel authViewModel;

    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        signInBinding = FragmentSignInBinding.inflate(inflater, container, false);
        return signInBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        authViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(AuthenticationViewModel.class);


        signInBinding.sendOtpBtn.setOnClickListener(v -> {
            String phoneNumber = signInBinding.phoneNumberET.getText().toString();
            authViewModel.login(phoneNumber, requireActivity());
        });

        authViewModel.getOtpSendStatusMutableLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean) {
                navController.navigate(R.id.action_signInFragment_to_OTPVerifyFragment);
            }
        });

        authViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                navController.navigate(R.id.action_signInFragment_to_signOutFragment);
            }
        });

    }
}