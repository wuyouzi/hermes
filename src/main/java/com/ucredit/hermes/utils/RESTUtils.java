/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.HeadersBuilder;

/**
 * @author ijay
 */
public class RESTUtils {
    private static Logger logger = LoggerFactory.getLogger(RESTUtils.class);

    public static ResponseEntity<?> buildBadRequestResponse(String o) {
        return ResponseEntity.badRequest().body(o);
    }

    public static ResponseEntity<?> buildCreatedResponse(String newLocation,
        Object id, Object... body) {
        ResponseEntity<?> entity = null;

        try {
            entity = ResponseEntity.created(new URI(newLocation))
                    .header("objectID", "" + id).body(body);
        } catch (URISyntaxException e) {
            RESTUtils.logger.warn("Cannot form new location URI", e);
            entity = ResponseEntity.status(HttpStatus.CREATED).body(body);
        }

        return entity;
    }

    public static ResponseEntity<?> buildNoContentResponse() {
        return RESTUtils.buildNoContentResponse(null);
    }

    public static ResponseEntity<?> buildNoContentResponse(
        Map<String, String> headers) {
        HeadersBuilder<?> builder = ResponseEntity.noContent();

        if (headers != null) {
            for (Entry<String, String> e : headers.entrySet()) {
                builder.header(e.getKey(), e.getValue());
            }
        }

        return builder.build();
    }

    public static ResponseEntity<?> buildNotFoundResponse() {
        return ResponseEntity.notFound().build();
    }

    public static ResponseEntity<?> buildConflictResponse() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    public static ResponseEntity<?> buildUnauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public static ResponseEntity<?> buildOKResponse() {
        return RESTUtils.buildOKResponse("");
    }

    public static ResponseEntity<?> buildAcceptedResponse() {
        return ResponseEntity.accepted().build();
    }

    public static ResponseEntity<?> buildOKResponse(Object o) {
        return ResponseEntity.ok(o);
    }

    public static ResponseEntity<?> buildForbiddenResponse(Object o) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(o);
    }
}
