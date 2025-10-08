import { requestUserBackend } from "../utils/requests";
import * as authService from "./auth-service";

export function findLoggedUser() {

    const headers = {
        Authorization: "Bearer " + authService.getAccessToken()
    }

    return requestUserBackend({ url: "/users/me", headers });
}