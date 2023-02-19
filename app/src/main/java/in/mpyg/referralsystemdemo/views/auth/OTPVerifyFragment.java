package in.mpyg.referralsystemdemo.views.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mpyg.referralsystemdemo.R;
import in.mpyg.referralsystemdemo.databinding.FragmentOTPVerifyBinding;
import in.mpyg.referralsystemdemo.viewmodels.AuthenticationViewModel;

public class OTPVerifyFragment extends Fragment {

    private FragmentOTPVerifyBinding otpVerifyBinding;

    private AuthenticationViewModel authViewModel;

    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        otpVerifyBinding = FragmentOTPVerifyBinding.inflate(inflater, container, false);
        return otpVerifyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        authViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(AuthenticationViewModel.class);

        otpVerifyBinding.loginBtn.setOnClickListener(v -> {
            String otp = otpVerifyBinding.otpNumberET.getText().toString();

            authViewModel.verifyCode(otp, requireActivity());
        });

        authViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                navController.navigate(R.id.action_OTPVerifyFragment_to_signOutFragment);
            }
        });
    }
}