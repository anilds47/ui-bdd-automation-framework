package com.utilities;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApiRetryAndLogFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(ApiRetryAndLogFilter.class);
    private final int maxRetries;

    public ApiRetryAndLogFilter(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        int attempt = 0;
        Response response = null;

        do {
            attempt++;
            long start = System.currentTimeMillis();

            logger.info("===== API Request Attempt #{} =====", attempt);
            logger.info("URL: {}", requestSpec.getURI());
            logger.info("Method: {}", requestSpec.getMethod());
            logger.info("Headers: {}", requestSpec.getHeaders());
            Object body = requestSpec.getBody();
            logger.info("Body: {}", body != null ? body : "No body for this request");

            response = ctx.next(requestSpec, responseSpec);

            long end = System.currentTimeMillis();
            long duration = end - start;

            logger.info("===== API Response (Attempt #{}) =====", attempt);
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Time Taken: {} ms", duration);
            logger.info("Response Body:\n{}", response.getBody().asPrettyString());

        } while (attempt < maxRetries && response.getStatusCode() >= 400);

        return response;
    }



}
