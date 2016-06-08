package com.frolfr.frolfrclient.api;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Represents a REST request to the Frolfr API
 *
 * Created by wowens on 1/17/16.
 */
public abstract class ApiRequest {

    // TODO: Allow for dev/prod modes
    protected final String baseUrl = "https://frolfr.herokuapp.com"; //"http://10.0.2.2:3000";
    protected String uri;
    protected RequestType requestType;
    protected String postBody = null;

    // TODO: add a responseHandler with instructions for parsing JSON and returning data?

    protected enum RequestType {
        GET,
        POST
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return requestType.name();
    }

    public String execute() {
        return execute(null, null);
    }
    public String execute(String email, String authToken) {

        // TODO: Get latest stored auth token

        Log.d(getClass().getSimpleName(), "Executing " + getType() + " request for " + getUri()
                + " with email[" + email + "] and token[" + authToken + "]");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(getUri());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(getType());
            if (authToken != null && email != null)
                urlConnection.setRequestProperty("Authorization", "Token token=\"" + authToken + "\",email=\"" + email + "\"");

            if (RequestType.valueOf(getType()) == RequestType.POST) {
//                urlConnection.setDoOutput( true );
//                urlConnection.setInstanceFollowRedirects( false );
//                urlConnection.setUseCaches( false );
                urlConnection.setRequestProperty( "Content-Type", "application/json");
                urlConnection.setRequestProperty( "charset", "utf-8");
                urlConnection.setRequestProperty( "Content-Length", Integer.toString(postBody == null ? 0 : postBody.getBytes("UTF-8").length));

                urlConnection.setDoOutput(true);

                if (postBody != null) {
                    urlConnection.getOutputStream().write(postBody.getBytes());
                    urlConnection.getOutputStream().flush();
                }
            }

            int responseCode = urlConnection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                Log.d(getClass().getSimpleName(), "Got successful response code: " + responseCode);
            } else {
                Log.w(getClass().getSimpleName(), "Got unsuccessful response code: " + responseCode);
            }

            // Read the input stream into a String
            InputStream responseStream = null;
            try {
                responseStream = urlConnection.getInputStream();
            } catch (FileNotFoundException e) {
                // No/unprocessable response
                return null;
            }
            StringBuffer buffer = new StringBuffer();
            if (responseStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(responseStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            String response = buffer.toString();
            Log.d(getClass().getSimpleName(), "Got response:\n" + response);

            return response;
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(getClass().getSimpleName(), "Error closing stream", e);
                }
            }
        }
    }

}
