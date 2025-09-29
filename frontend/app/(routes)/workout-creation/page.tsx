'use client'

import ExerciseCard from "@/app/cards/exercise-card/page";
import * as exerciseService from "@/app/services/exercise-service";
import * as workoutService from "@/app/services/workout-service";
import Link from "next/link";
import { useEffect, useState } from "react";
import { FaArrowLeft } from "react-icons/fa";
import style from "./page.module.css";
import { useRouter } from "next/navigation";

interface Exercise {
    id: number;
    name: string;
    video: string;
}

interface WorkoutItem {
    exerciseId: number;
    setNumber: number;
    reps: string;
    rest: number;
    weight: number;
}

interface Workout {
    name: string;
    workoutItems: WorkoutItem[];
}

export default function WorkoutCreation() {
    const [exercises, setExercises] = useState<Exercise[]>([]);
    const [selectedExercises, setSelectedExercises] = useState<Record<number, WorkoutItem[]>>({});
    const [workoutName, setWorkoutName] = useState("Novo Treino");
    const router = useRouter();

    useEffect(() => {
        exerciseService.getExercises().then((response) => {
            setExercises(response.data.content);
        });
    }, []);

    const addSet = (exerciseId: number) => {
        setSelectedExercises((prev) => ({
            ...prev,
            [exerciseId]: [
                ...(prev[exerciseId] || []),
                { exerciseId, setNumber: (prev[exerciseId]?.length || 0) + 1, reps: '10', rest: 60, weight: 20 },
            ],
        }));
    };

    const updateSet = (exerciseId: number, index: number, key: string, value: number) => {
        setSelectedExercises((prev) => {
            const updatedSets = [...(prev[exerciseId] || [])];
            updatedSets[index] = { ...updatedSets[index], [key]: value };
            return { ...prev, [exerciseId]: updatedSets };
        });
    };

    const removeSet = (exerciseId: number, index: number) => {
        setSelectedExercises((prev) => {
            const updatedSets = [...(prev[exerciseId] || [])];
            updatedSets.splice(index, 1);
            return { ...prev, [exerciseId]: updatedSets };
        });
    };

    const saveWorkout = () => {
        const workout: Workout = {
            name: workoutName,
            workoutItems: Object.values(selectedExercises).flat(),
        };

        workoutService.newWorkout(workout).then(() => {
            setWorkoutName("");
            setSelectedExercises({});
            router.push('/workouts');
        });
    };

    return (
        <div className={style.container}>
            <div className={style.title}>
                <Link href={'/workouts'}>
                    <FaArrowLeft className={style.arrow} />
                </Link>
                <input
                    type="text"
                    placeholder="Novo Treino"
                    value={workoutName}
                    onChange={(e) => setWorkoutName(e.target.value)}
                />
                <button className={style.save_button} onClick={saveWorkout}>
                    Salvar Treino
                </button>
            </div>

            <div className={style.items_container}>
                <h2>Escolha os Exercícios</h2>
                {exercises.map((exercise) => (
                    <>
                        <div className={style.exercise_container}>
                            <div
                                className={style.exercise_card}
                                onClick={() => addSet(exercise.id)}>
                                <ExerciseCard key={exercise.id} exercise={exercise} />
                            </div>
                        </div>
                        <div>

                            {selectedExercises[exercise.id]?.map((set, index) => (
                                <div key={index}>
                                    <div className={style.sets_container}>

                                        <div className={style.setsHeader}>
                                            <div className={style.one}>PESO</div>
                                            <div className={style.two}>REPS</div>
                                            <div className={style.three}>DESCANSO</div>
                                        </div>

                                        <div className={style.sets}>
                                            <input
                                                type="number"
                                                value={set.weight}
                                                onChange={(e) => updateSet(exercise.id, index, "weight", +e.target.value)}
                                                placeholder="Peso"
                                            />
                                            <input
                                                type="number"
                                                value={set.reps}
                                                onChange={(e) => updateSet(exercise.id, index, "reps", +e.target.value)}
                                                placeholder="Reps"
                                            />
                                            <input
                                                type="number"
                                                value={set.rest}
                                                onChange={(e) => updateSet(exercise.id, index, "rest", +e.target.value)}
                                                placeholder="Descanso"
                                            />
                                        </div>
                                    </div>
                                    {index === selectedExercises[exercise.id].length - 1 && (
                                        <button
                                            className={style.remove_button}
                                            onClick={() => removeSet(exercise.id, index)}>
                                            x
                                        </button>
                                    )}
                                </div>
                            ))}

                        </div>
                    </>
                ))}
            </div>


        </div>
    );
}
