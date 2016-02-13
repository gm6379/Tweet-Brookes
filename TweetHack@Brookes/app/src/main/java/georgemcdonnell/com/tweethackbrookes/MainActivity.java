package georgemcdonnell.com.tweethackbrookes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LH0yCqIw89xrwDSIGeq4qfWI4";
    private static final String TWITTER_SECRET = "dbV4vEXbKXmfzLJaE35XRaLvfq9nFQo3s42BYV6A119X2RQWwh";

    private TwitterLoginButton loginButton;
    private ListView tweetListView;
    private EditText userSearchEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        userSearchEditText = (EditText) findViewById(R.id.userSearchEditText);

        if (Twitter.getInstance().core.getSessionManager().getActiveSession() != null) {
            loginButton.setVisibility(View.INVISIBLE);
            setupSearch();
            displayTweetsForScreenName("hackatbrookes");

        } else {
            userSearchEditText.setVisibility(View.INVISIBLE);

            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    loginButton.setVisibility(View.INVISIBLE);
                    userSearchEditText.setVisibility(View.VISIBLE);
                    setupSearch();
                    displayTweetsForScreenName("hackatbrookes");
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Login with Twitter failure", exception);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void displayTweetsForScreenName(String screenName) {
        tweetListView = (ListView) findViewById(R.id.tweetListView);
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(screenName)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getApplicationContext())
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                .setTimeline(userTimeline)
                .build();
        tweetListView.setAdapter(adapter);
    }

    public void setupSearch() {
        userSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                displayTweetsForScreenName(userSearchEditText.getEditableText().toString());
                return true;
            }
        });
    }

}
