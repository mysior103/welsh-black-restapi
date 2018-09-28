package pl.mysior.welshblackrestapi.controller.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;

public final class HeaderUtil {

    private static final Logger logger = LogManager.getLogger(HeaderUtil.class);
    private static final String APPLICATION_NAME = "welsh-black-restapi";


    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Welsh-black-restapi-alert", message);
        headers.add("Welsh-black-restapi-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        logger.error("Failed processing with " + defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Welsh-black-restapi-error", "error." + errorKey);
        headers.add("Welsh-black-restapi-params", entityName);
        return headers;
    }
}
