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
        int SUCCESS = 400;
        int BAD_REQUEST = 400;
    }

    /**
     * URL Constants
     */
    public interface API_URL_CONST {
        String ROOT = "/api";

        // User controller
        String USER_ROOT = "/users";
        String USER_ID= "/users/{id}";
        String USER_CHANGE_PASSWORD= "/users/change-password/{id}";
        String USER_CHANGE_HOST_WISH_LIST= "/users/change-host-wish-list/{id}";
        String USER_ACTIVATION="/users/activation";
        String USER_UPDATE_PASSWORD_WITH_CODE="/users/update-password-with-code";
        String USER_SEND_FORGOT_CODE_VIA_EMAIL="/users/send-code-via_email";

        // HostAgent controller
        String HOST_ROOT = "/hosts";
        String HOST_ID = "/hosts/{id}";

        // HostCategory controller
        String HOST_CATEGORY_ROOT = "/host-categories";
        String HOST_CATEGORY_ID = "/host-category/{id}";

        // HostRoomType controller
        String HOST_ROOM_TYPE_ROOT = "/host-room-types";
        String HOST_ROOM_TYPE_ID = "/host-room-types/{id}";

        // Host City controller
        String HOST_CITY_ROOT = "/host-cities";
        String HOST_CITY_ID = "/host-cities/{id}";

        // Role controller
        String ROLE_ROOT = "/roles";
        String ROLE_ID= "/roles/{id}";

        // Booking controller
        String BOOKING_ROOT = "/bookings";
        String BOOKING_CANCEL = "/bookings-cancel/{id}";

        // Host review controller
        String HOST_REVIEW_ROOT = "/host-reviews";

        // Host cancellation policy
        String HOST_CANCELLATION_POLICY_ROOT = "/host-cancellation-policies";

        // Host image
        String HOST_IMAGE_UPLOAD_FILE = "/host-images/upload-file";
        String HOST_IMAGE_DATA_INFO = "/host-images/data-info";
        String HOST_IMAGE_UPLOAD_MULTIPLE_FILES = "/host-images/upload-multiple-files";
        String HOST_IMAGE_DOWNLOAD_FILE_ID = "/host-images/download-file/{id}";
        String HOST_IMAGE_FILE_ID = "/host-images/{id}";
    }
}
