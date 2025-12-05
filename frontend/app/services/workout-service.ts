import { AxiosRequestConfig } from "axios";
import { requestWorkoutBackend } from "../utils/requests";
import * as authService from "./auth-service";
import { NewWorkoutDTO } from "../models/workout";

export function getWorkoutById(id: number) {

    const headers = {
        Authorization: "Bearer " + authService.getAccessToken()
    }

    const config: AxiosRequestConfig = {
        url: `/workouts/${id}`,
        headers
    }

    return requestWorkoutBackend(config);
}

export function newWorkout(workout: NewWorkoutDTO) {

    const headers = {
        Authorization: "Bearer " + authService.getAccessToken()
    }

    const config: AxiosRequestConfig = {
        method: "POST",
        url: "/workouts",
        data: workout,
        headers
    }

    return requestWorkoutBackend(config);

}

export function updateWorkout(id: number, workout: NewWorkoutDTO) {

    const headers = {
        Authorization: "Bearer " + authService.getAccessToken()
    }

    const config: AxiosRequestConfig = {
        method: "PUT",
        url: `/workouts/${id}`,
        data: workout,
        headers
    }

    return requestWorkoutBackend(config);

}

export function deleteWorkout(id: number) {
    const headers = {
        Authorization: "Bearer " + authService.getAccessToken()
    }

    const config: AxiosRequestConfig = {
        method: "DELETE",
        url: `/workouts/${id}`,
        headers
    }

    return requestWorkoutBackend(config);
}