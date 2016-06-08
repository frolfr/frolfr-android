package com.frolfr.frolfrclient.api;

/**
 * Created by wowens on 1/22/16.
 */
public class CourseScorecards {

    public static class CourseScorecardRequest extends ApiRequest {
        private static final String baseUri = "/api/course_scorecards?course_id=";

        public CourseScorecardRequest(int courseId) {
            this.uri = baseUrl + baseUri + courseId;
            this.requestType = RequestType.GET;
        }
    }

}
