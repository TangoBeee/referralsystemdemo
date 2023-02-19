package in.mpyg.referralsystemdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import org.json.JSONObject;

import in.mpyg.referralsystemdemo.databinding.ActivityMainBinding;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // login
        Branch.getInstance().setIdentity("12345678901234");

        Branch.sessionBuilder(MainActivity.this).withCallback(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error != null) {
                    Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.getMessage());
                } else {
                    Log.e("BranchSDK_Tester", "branch init complete!");
                    if (branchUniversalObject != null) {
                        Log.e("BranchSDK_Tester", "title " + branchUniversalObject.getTitle());
                        Log.e("BranchSDK_Tester", "CanonicalIdentifier " + branchUniversalObject.getCanonicalIdentifier());
                        Log.e("BranchSDK_Tester", "metadata " + branchUniversalObject.getContentMetadata().convertToJson());
                    }

                    if (linkProperties != null) {
                        Log.e("BranchSDK_Tester", "Channel " + linkProperties.getChannel());
                        Log.e("BranchSDK_Tester", "control params " + linkProperties.getControlParams());
                    }
                }
            }
        }).withData(this.getIntent().getData()).init();


        // latest
        JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();

        // first
        JSONObject installParams = Branch.getInstance().getFirstReferringParams();
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        Branch.sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error != null) {
                    Log.e("BranchSDK_Tester", error.getMessage());
                } else if (referringParams != null) {
                    Log.e("BranchSDK_Tester", referringParams.toString());
                }
            }
        }).reInit();
    }
}