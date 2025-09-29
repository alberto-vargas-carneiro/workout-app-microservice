import { requestBackend } from "../utils/requests";
import * as authService from "./auth-service";

export function getExercises() {

    const headers = {
        Authorization: "Bearer " + authService.getAccessToken()
    }

    return requestBackend({ url: "/exercises?sort=name", headers });
}