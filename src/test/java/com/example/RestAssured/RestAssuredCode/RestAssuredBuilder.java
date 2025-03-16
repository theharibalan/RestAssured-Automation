package com.example.RestAssured.RestAssuredCode;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Collections;
import java.util.Map;

public class RestAssuredBuilder {

    private String baseUri;
    private Map<String, String> headers = Collections.emptyMap();
    private String authToken;
    private Map<String, String> queryParams = Collections.emptyMap();
    private Map<String, String> pathParams = Collections.emptyMap();
    private boolean enableogging;
    private ContentType contentType = ContentType.JSON;
    private boolean enableLogging;

    private RestAssuredBuilder() {}

    public static RestAssuredBuilder builder() {
        return new RestAssuredBuilder();
    }

    public RestAssuredBuilder setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public RestAssuredBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers != null ? headers : Collections.emptyMap();
        return this;
    }

    public RestAssuredBuilder setAuthToken(String authToken) {
        this.authToken = authToken;
        return this;
    }

    public RestAssuredBuilder setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams != null ? queryParams : Collections.emptyMap();
        return this;
    }

    public RestAssuredBuilder setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams != null ? pathParams : Collections.emptyMap();
        return this;
    }

    public RestAssuredBuilder enableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
        return this;
    }

    public RestAssuredBuilder setContentType(ContentType contentType) {
        this.contentType = contentType != null ? contentType : ContentType.JSON;
        return this;
    }

    public RequestSpecification build() {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();

        if (baseUri != null) {
            specBuilder.setBaseUri(baseUri);
        }

        if (!headers.isEmpty()) {
            specBuilder.addHeaders(headers);
        }

        if (authToken != null) {
            specBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        if (!queryParams.isEmpty()) {
            specBuilder.addQueryParams(queryParams);
        }

        if (!pathParams.isEmpty()) {
            specBuilder.addPathParams(pathParams);
        }

        specBuilder.setContentType(contentType);

        if (enableLogging) {
            specBuilder.log(LogDetail.ALL);
        }

        return specBuilder.build();
    }
}
