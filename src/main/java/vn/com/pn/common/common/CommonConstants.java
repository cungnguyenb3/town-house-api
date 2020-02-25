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

        // User controller
        String USER_ROOT = "/users";
        String USER_ID= "/users/{id}";

        // HostAgent controller
        String HOST_AGENT_ROOT = "/host-agents";
        String HOST_AGENT_ID = "/host-agents/{id}";

        // HostAgent controller
        String HOST_ROOT = "/hosts";
        String HOST_ID = "/hosts/{id}";

        // HostCategory controller
        String HOST_CATEGORY_ROOT = "/host-categories";
        String HOST_CATEGORY_ID = "/host-category/{id}";

        // HostRoomType controller
        String HOST_ROOM_TYPE_ROOT = "/host-room-types";
        String HOST_ROOM_TYPE_ID = "/host-room-types/{id}";

        // HostRoomType controller
        String HOST_CITY_ROOT = "/host-cities";
        String HOST_CITY_ID = "/host-cities/{id}";

    }
}
