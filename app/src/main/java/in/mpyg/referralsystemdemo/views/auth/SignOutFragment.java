package in.mpyg.referralsystemdemo.views.auth;

import android.content.Intent;
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
import in.mpyg.referralsystemdemo.databinding.FragmentSignOutBinding;
import in.mpyg.referralsystemdemo.viewmodels.AuthenticationViewModel;
import in.mpyg.referralsystemdemo.views.referral.ReferralActivity;
import io.branch.referral.Branch;

public class SignOutFragment extends Fragment {

    private FragmentSignOutBinding signOutBinding;

    private AuthenticationViewModel authViewModel;

    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        signOutBinding = FragmentSignOutBinding.inflate(inflater, container, false);
        return signOutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        authViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(AuthenticationViewModel.class);

        signOutBinding.signoutBtn.setOnClickListener(v -> {
            Branch.getInstance().logout();
            authViewModel.signout();
        });
        signOutBinding.referralActivity.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ReferralActivity.class);
            startActivity(intent);
        });

        authViewModel.getLoggedStatusMutableLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean) {
                navController.navigate(R.id.action_signOutFragment_to_signInFragment);
            }
        });
    }
}