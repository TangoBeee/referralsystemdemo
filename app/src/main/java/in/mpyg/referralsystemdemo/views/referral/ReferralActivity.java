package in.mpyg.referralsystemdemo.views.referral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import in.mpyg.referralsystemdemo.MainActivity;
import in.mpyg.referralsystemdemo.databinding.ActivityReferralBinding;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class ReferralActivity extends AppCompatActivity {

    ActivityReferralBinding referralBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referralBinding = ActivityReferralBinding.inflate(getLayoutInflater());
        setContentView(referralBinding.getRoot());

        String uid = FirebaseAuth.getInstance().getUid();

        referralBinding.referrerButton.setOnClickListener(v -> {

            BranchUniversalObject buo = new BranchUniversalObject()
                    .setCanonicalIdentifier("content/12345")
                    .setTitle("USA Got Exposed")
                    .setContentDescription("[LIVE] USA Airport Security Checking")
                    .setContentImageUrl("https://i.imgur.com/t75V7oe.png")
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setContentMetadata(new ContentMetadata().addCustomMetadata("key1", "value1"));

            LinkProperties lp = new LinkProperties()
                    .setChannel("facebook")
                    .setFeature("sharing")
                    .setCampaign("content 123 launch")
                    .setStage("new user")
                    .addControlParameter("$desktop_url", "https://example.com/home")
                    .addControlParameter("custom", "data")
                    .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

            ShareSheetStyle ss = new ShareSheetStyle(this, "Check this out!", "This stuff is awesome: ")
                    .setCopyUrlStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                    .setMoreOptionStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_search), "Show more")
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                    .setAsFullWidthStyle(true)
                    .setSharingTitle("Share With");

            buo.showShareSheet(this, lp,  ss,  new Branch.BranchLinkShareListener() {
                @Override
                public void onShareLinkDialogLaunched() {
                }
                @Override
                public void onShareLinkDialogDismissed() {
                }
                @Override
                public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                }
                @Override
                public void onChannelSelected(String channelName) {
                }
            });
        });

    }
}