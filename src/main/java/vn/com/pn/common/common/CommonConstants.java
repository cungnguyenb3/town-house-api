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

    public interface HTTP_STATUS_CODE {
        int SUCCESS = 200;
        int BAD_REQUEST = 400;
        int FORBIDDEN = 403;
    }

    /**
     * URL Constants
     */
    public interface API_URL_CONST {
        String ROOT = "/api";

        // User controller
        String USER_ROOT = "/users";
        String USER_ID = "/users/{id}";
        String USER_CHANGE_PASSWORD = "/users/change-password/{id}";
        String USER_CHANGE_HOST_WISH_LIST = "/users/change-host-wish-list/{id}";
        String USER_ACTIVATION = "/users/activation";
        String USER_UPDATE_PASSWORD_WITH_CODE = "/users/update-password-with-code";
        String USER_SEND_FORGOT_CODE_VIA_EMAIL = "/users/send-code-via-email";
        String USER_SIGN_UP = "/users/sign-up";
        String USER_SIGN_UP_ADMIN = "/users/sign-up/admin";
        String USER_SIGN_IN = "/users/sign-in";
        String USER_BOOKING_HISTORIES = "/users/booking-histories";
        String USER_GET_USER_VIA_TOKEN = "/users/get-user-via-token";

        // Host controller
        String HOST_ROOT = "/hosts";
        String HOST_ID = "/hosts/{id}";
        String HOST_DISCOUNT = "/hosts/discount";
        String HOST_APPROVED = "/hosts/approved/{id}";
        String HOST_SEARCH = "/hosts/search";
        String HOST_GET_BY_CITY = "/hosts/get-by-city-id/{id}";

        // HostCategory controller
        String HOST_CATEGORY_ROOT = "/host-categories";
        String HOST_CATEGORY_ID = "/host-categories/{id}";

        // HostRoomType controller
        String HOST_ROOM_TYPE_ROOT = "/host-room-types";
        String HOST_ROOM_TYPE_ID = "/host-room-types/{id}";

        // Host City controller
        String HOST_CITY_ROOT = "/host-cities";
        String HOST_CITY_ID = "/host-cities/{id}";

        // Role controller
        String ROLE_ROOT = "/roles";
        String ROLE_ID = "/roles/{id}";

        // Booking controller
        String BOOKING_ROOT = "/bookings";
        String BOOKING_CANCEL = "/bookings-cancel/{id}";
        String BOOKING_CALCULATE_PRICE = "/bookings-calculate-price";
        String BOOKING_REQUEST_SUCCESS = "/bookings-request-success/{bookingId}";
        String BOOKING_CONFIRM_REQUEST = "/bookings-confirm-request/{bookingId}";

        // Host review controller
        String HOST_REVIEW_ROOT = "/host-reviews";
        String HOST_REVIEW_GET_BY_HOST_ID = "/host-reviews/get-by-host-id/{id}";

        // Host cancellation policy
        String HOST_CANCELLATION_POLICY_ROOT = "/host-cancellation-policies";

        // Host image
        String HOST_IMAGE_UPLOAD_FILE = "/host-images/upload-file";
        String HOST_IMAGE_DATA_INFO = "/host-images/data-info";
        String HOST_IMAGE_UPLOAD_MULTIPLE_FILES = "/host-images/upload-multiple-files";
        String HOST_IMAGE_DOWNLOAD_FILE_ID = "/host-images/download-file/{id}";
        String HOST_IMAGE_FILE_ID = "/host-images/{id}";
        String HOST_IMAGE_GET_ALL_FILE = "/host-images";

        // Language
        String LANGUAGE_ROOT = "/languages";
        String LANGUAGE_ID = "/languages/{id}";

        // Rule
        String RULE_ROOT = "/rules";

        // Currency unit
        String CURRENCY_UNIT_ROOT = "/currency_units";

        // Procedure checkin
        String PROCEDURE_CHECK_IN_ROOT = "/procedures_check_in";
    }
}
