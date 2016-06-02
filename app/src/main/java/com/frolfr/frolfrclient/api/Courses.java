package com.frolfr.frolfrclient.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wowens on 1/22/16.
 */
public class Courses {

    public static class CoursesRequest extends ApiRequest {
        private static final String baseUri = "/api/courses";

        public CoursesRequest() {
            this.uri = baseUrl + baseUri;
            this.requestType = ApiRequest.RequestType.GET;
        }
    }

}
