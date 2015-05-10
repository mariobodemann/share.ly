package net.bodemann.sharely.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RequestShortUrlTask extends AsyncTask<String, Void, String> {
    private final static String LOG_TAG = RequestShortUrlTask.class.getCanonicalName();

    public interface Listener {
        void uriShortened(String uri);
    }

    private final Listener mListener;
    private final String mAccessKey;

    public RequestShortUrlTask(Listener listener, String accessKey) {
        mListener = listener;
        mAccessKey = accessKey;
    }

    @Override
    protected String doInBackground(String... params) {
        final String inputUri = params[0];
        if (TextUtils.isEmpty(inputUri)) {
            return null;
        }

        final Uri uri = Uri.parse("https://api-ssl.bitly.com/v3/").buildUpon()
                .appendPath("shorten")
                .appendQueryParameter("format", "txt")
                .appendQueryParameter("access_token", mAccessKey)
                .appendQueryParameter("longUrl", inputUri)
                .build();

        final String shortenedUri = getShortenedUri(uri);
        if (TextUtils.isEmpty(shortenedUri)) {
            return null;
        } else {
            return shortenedUri;
        }
    }

    private String getShortenedUri(Uri uri) {
        try {
            final URLConnection connection = new URL(uri.toString()).openConnection();
            final InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
            final BufferedReader reader = new BufferedReader(inputStream);
            final StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            return builder.toString();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed Url thrown");
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception thrown");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String url) {
        super.onPostExecute(url);

        if (mListener != null) {
            mListener.uriShortened(url);
        }
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);

        if (mListener != null) {
            mListener.uriShortened(null);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if (mListener != null) {
            mListener.uriShortened(null);
        }
    }
}
