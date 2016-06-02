package com.frolfr.frolfrclient.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wowens on 1/17/16.
 */
public class Authorization {

    private static final String baseUri = "/api/authorizations";    // TODO: rename

    public static class AuthorizationRequest extends ApiRequest {
        private final String EMAIL = "email";
        private final String PASS = "password";
        public AuthorizationRequest(String email, String password) {
            this.uri = baseUrl + baseUri;
            this.requestType = ApiRequest.RequestType.POST;
            JSONObject json = new JSONObject();
            try {
                json.put(EMAIL, email);
                json.put(PASS, password);
                this.postBody = json.toString();
            } catch (JSONException e) {
                Log.e(getClass().getSimpleName(), "Failed to create JSON body", e);
            }
        }
    }

}
