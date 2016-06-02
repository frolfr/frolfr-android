package com.frolfr.frolfrclient.api;

/**
 * Created by wowens on 1/22/16.
 */
public class Profile {

    private static final String baseUri = "/api/profile_stat_logs/current";

    public static class GetStatistics extends ApiRequest {
        public GetStatistics() {
            this.uri = baseUrl + baseUri;
            this.requestType = RequestType.GET;
        }
    }

}
