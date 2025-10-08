import axios, { AxiosRequestConfig } from "axios";
import { EXERCISE_URL, USER_URL, WORKOUT_URL } from "./system";

export function requestExerciseBackend(config: AxiosRequestConfig) {
    return axios({...config, baseURL: EXERCISE_URL});
}

export function requestWorkoutBackend(config: AxiosRequestConfig) {
    return axios({...config, baseURL: WORKOUT_URL});
}

export function requestUserBackend(config: AxiosRequestConfig) {
    return axios({...config, baseURL: USER_URL});
}