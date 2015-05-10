package net.bodemann.sharely.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import net.bodemann.sharely.R;
import net.bodemann.sharely.task.RequestShortUrlTask;

public class ShareActivity extends Activity {

    private AsyncTask<String, Void, String> mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String possibleUri = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        final RequestShortUrlTask.Listener requestListener = new RequestShortUrlTask.Listener() {
            @Override
            public void uriShortened(String uri) {
                if (TextUtils.isEmpty(uri)) {
                    sendShareIntent(possibleUri);
                } else {
                    sendShareIntent(uri);
                }
            }
        };

        mAsyncTask = new RequestShortUrlTask(requestListener, getString(R.string.bitly_access_key)).execute(possibleUri);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mAsyncTask.cancel(true);
    }

    private void sendShareIntent(String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(Intent.createChooser(intent, getString(R.string.share_shortened_uri)));
        finish();
    }
}
