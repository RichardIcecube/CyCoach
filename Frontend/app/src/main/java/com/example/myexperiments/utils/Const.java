package com.example.myexperiments.utils;

public class Const {
    public static final String URL_JSON_USERS = "http://coms-309-043.class.las.iastate.edu:8080/users";
    public static final String URL_UPDATE_USER_PUT = "http://coms-309-043.class.las.iastate.edu:8080/users/{userId}";

    public static final String URL_JSON_POST_WORKOUT = "http://coms-309-043.class.las.iastate.edu:8080/workouts";
    public static final String URL_JSON_POST_EXERCISE = "http://coms-309-043.class.las.iastate.edu:8080/exercises";
    public static final String URL_JSON_POST_ATHLETE_EXERCISE = "http://coms-309-043.class.las.iastate.edu:8080/coaches/{coachId}/athletes/{athleteId}/workouts/{workoutId}";
    public static final String URL_JSON_GET_EXERCISES = "http://coms-309-043.class.las.iastate.edu:8080/exercises";
    public static final String URL_JSON_GET_WORKOUTS = "http://coms-309-043.class.las.iastate.edu:8080/workouts";
    public static final String URL_JSON_GET_COACHES_ATHLETES = "http://coms-309-043.class.las.iastate.edu:8080/users/{id}/coach/athletes";

    public static final String URL_JSON_GET_COACHES_ID = "http://coms-309-043.class.las.iastate.edu:8080/users/{userId}/classID";
    public static final String URL_JSON_GET_ATHLETES_WORKOUTS = "http://coms-309-043.class.las.iastate.edu:8080/users/{id}/athlete/workouts";
    public static final String URL_JSON_DELETE_EXERCISE = "http://coms-309-043.class.las.iastate.edu:8080/exercises/{id}";
    public static final String URL_JSON_DELETE_ATHLETE_EXERCISE = "http://coms-309-043.class.las.iastate.edu:8080/coaches/{coachId}/athletes/{athleteId}/workouts/{workoutId}";
    public static final String URL_JSON_WORKOUT_ARRAY = "http://coms-309-043.class.las.iastate.edu:8080/workouts";
    public static final String URL_GET_COACH_ATHLETES_BASE = "http://coms-309-043.class.las.iastate.edu:8080/coaches/{id}/athletes";
    public static final String URL_GET_MANAGERS = "http://coms-309-043.class.las.iastate.edu:8080/managers";

    public static final String URL_GET_ATHLETES = "http://coms-309-043.class.las.iastate.edu:8080/managers/{managerId}/athletes";
    public static final String URL_COACH_ATHLETE_UNLINK = "http://coms-309-043.class.las.iastate.edu:8080/managers/{managerId}/athletes/{athleteId}/unlink";
    public static final String URL_COACH_ATHLETE_LINK = "http://coms-309-043.class.las.iastate.edu:8080/managers/{managerId}/coaches/{coachId}/athletes/{athleteId}";

    public static final String URL_GET_ALL_COACHES = "http://coms-309-043.class.las.iastate.edu:8080/managers/{managerId}/coaches";
    public static final String URL_GET_MANAGERS_COACHES = "http://coms-309-043.class.las.iastate.edu:8080/managers/{managerId}/coachesList";
    public static final String URL_MANAGER_COACH_LINK = "http://coms-309-043.class.las.iastate.edu:8080/managers/{managerId}/coaches/{coachId}";
    public static final String URL_MANAGER_COACH_UNLINK ="http://coms-309-043.class.las.iastate.edu:8080/managers/{managerId}/coaches/{coachId}/unlink";
}
