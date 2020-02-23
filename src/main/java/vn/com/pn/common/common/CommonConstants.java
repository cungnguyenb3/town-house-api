package vn.com.pn.common.common;

public class CommonConstants {

    /**
     * Status return from API
     */
    public interface STATUS {
        int STATUS_SUCCESS = 0;
        int STATUS_NOT_LOGIN = 1;
        int STATUS_ROLE_ERROR = 2;
        int STATUS_PARAM_ERROR = 3;
        int STATUS_DATA_NOT_EXIST = 4;
        int STATUS_CONVERT_ERROR = 5;
        int STATUS_FAILURE = 9;
        int STATUS_NO_RECORD = 8;
    }

    /**
     * URL Constants
     */
    public interface API_URL_CONST {
        String ROOT = "/api";

        // user controller
        String USER_ROOT = "/users";
        String USER_ID= "/users/{id}";
    }
}
