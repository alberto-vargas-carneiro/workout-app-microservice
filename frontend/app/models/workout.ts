export type WorkoutDTO = {
    id: number;
    name: string;
    date: string;
}

export type NewWorkoutDTO = {
    name: string;
    workoutItems: WorkoutItemDTO[];
}

export type WorkoutItemDTO = {
    exerciseId: number;
    setNumber: number;
    reps: string;
    rest: number;
    weight: number;
}