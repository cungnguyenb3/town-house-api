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

        // Admin controller
        String ADMIN_HOST_CATEGORY_ROOT = "/admin/host-categories";
        String ADMIN_HOST_CATEGORY_ID = "/admin/host-categories/{id}";
        String ADMIN_HOST_ROOM_TYPE_ROOT = "/admin/host-room-types";
        String ADMIN_HOST_ROOM_TYPE_ID = "/admin/host-room-types/{id}";
        String ADMIN_HOST_CITY_ROOT = "/host-cities";
        String ADMIN_HOST_CITY_ID = "/admin/host-cities/{id}";
        String ADMIN_ROLE_ROOT = "/admin/roles";
        String ADMIN_ROLE_ID = "/admin/roles/{id}";
        String ADMIN_LANGUAGE_ROOT = "/admin/languages";
        String ADMIN_LANGUAGE_ID = "/admin/languages/{id}";
        String ADMIN_RULE_ROOT = "/admin/rules";
        String ADMIN_CURRENCY_UNIT_ROOT = "/admin/currency-units";
        String ADMIN_PROCEDURE_CHECK_IN_ROOT = "/admin/procedures-check-in";
        String ADMIN_HOST_CANCELLATION_POLICY_ROOT = "/admin/host-cancellation-policies";
        String ADMIN_HOST_APPROVED = "/admin/hosts/approved/{id}";
        String ADMIN_BOOKING_REQUEST_SUCCESS = "/admin/bookings-request-success/{bookingId}";

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
        String USER_SIGN_OUT = "/users/sign-out";
        String USER_BOOKING_HISTORIES = "/users/booking-histories";
        String USER_ME = "/users/me";
        String USER_HOST = "/users/hosts";

        // Host controller
        String HOST_ROOT = "/hosts";
        String HOST_ID = "/hosts/{id}";
        String HOST_DISCOUNT = "/hosts/discount";
        String HOST_SEARCH = "/hosts/search";
        String HOST_GET_BY_CITY = "/hosts/get-by-city-id/{id}";
        String HOST_RECOMMENDATIONS = "hosts/recommendations";

        // Booking controller
        String BOOKING_ROOT = "/bookings";
        String BOOKING_CANCEL = "/bookings-cancel/{id}";
        String BOOKING_CALCULATE_PRICE = "/bookings-calculate-price";
        String BOOKING_CONFIRM_REQUEST = "/bookings-confirm-request/{bookingId}";
        String BOOKING_ID = "/bookings/{id}";
        String BOOKING_USER_CALENDER = "/bookings/users/calendar";
        String BOOKING_USER = "/bookings/users";
        String BOOKING_ANALYSING = "bookings/analysing";
        String BOOKING_CANCELING = "booking/canceling";

        // Host review controller
        String HOST_REVIEW_ROOT = "/host-reviews";
        String HOST_REVIEW_GET_BY_HOST_ID = "/host-reviews/get-by-host-id/{id}";

        // Host image
        String HOST_IMAGE_UPLOAD_FILE = "/host-images/upload-file";
        String HOST_IMAGE_DATA_INFO = "/host-images/data-info";
        String HOST_IMAGE_UPLOAD_MULTIPLE_FILES = "/host-images/upload-multiple-files";
        String HOST_IMAGE_DOWNLOAD_FILE_ID = "/host-images/download-file/{id}";
        String HOST_IMAGE_FILE_ID = "/host-images/{id}";
        String HOST_IMAGE_GET_ALL_FILE = "/host-images";

        // Momo
        String MOMO_BASIC_INFO = "/momo/basic-info";
        String MOMO_GET_BASIC_INFO = "/momo/basic-info-request";
        String MOMO_PAYMENT_CONFIRM = "/momo/payment/confirm";

        //Notification
        String NOTIFICATION_USER = "notifications/users";
        String NOTIFICATION_USER_ID = "notifications/users/{id}";
    }
}
