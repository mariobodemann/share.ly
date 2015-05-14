package net.bodemann.sharely.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;

import net.bodemann.sharely.R;
import net.bodemann.sharely.task.RequestShortUrlTask;

import java.net.MalformedURLException;
import java.net.URL;

public class ShareActivity extends Activity {

    private AsyncTask<String, Void, String> mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String extraText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        try {
            final URL url = new URL(extraText);
            requestShortenedUri(url.toString());
        } catch (MalformedURLException e) {
            sendShareIntent(extraText, R.string.share_original_text);
        }
    }

    private void requestShortenedUri(String urlString) {
        final RequestShortUrlTask.Listener requestListener = new RequestShortUrlTask.Listener() {
            @Override
            public void uriShortened(String uri) {
                sendShareIntent(uri, R.string.share_shortened_uri);
            }
        };

        mAsyncTask = new RequestShortUrlTask(requestListener, getString(R.string.bitly_access_key))
                .execute(urlString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mAsyncTask.cancel(true);
    }

    private void sendShareIntent(String url, @StringRes int titleId) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(Intent.createChooser(intent, getString(titleId)));
        finish();
    }
}
