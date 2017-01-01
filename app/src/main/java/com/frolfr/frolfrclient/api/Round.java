package com.frolfr.frolfrclient.api;

/**
 * Created by wowens on 1/1/17.
 */
public class Round {

    public static class RoundRequest extends ApiRequest {
        private static final String baseUri = "/api/rounds/";

        public RoundRequest(int roundId) {
            this.uri = baseUrl + baseUri + roundId;
            this.requestType = RequestType.GET;
        }
    }

}
