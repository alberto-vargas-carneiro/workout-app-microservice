'use client'

import WorkoutItemsCard from '@/app/cards/workout-cards/items/page';
import SetsCard from '@/app/cards/workout-cards/sets/page';
import * as workoutService from '@/app/services/workout-service';
import * as exerciseService from '@/app/services/exercise-service';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';
import { FaArrowLeft } from "react-icons/fa";
import { FaRegTrashAlt } from "react-icons/fa";
import { RiPencilFill } from "react-icons/ri";
import style from './page.module.css';
import ExerciseCard from '@/app/cards/exercise-card/page';

interface WorkoutItemDTO {
  id?: number;
  exerciseId: number;
  exerciseName: string;
  setNumber: number;
  reps: string;
  rest: number;
  weight: number;
  image: string;
}

interface GroupedWorkout {
  exerciseName: string;
  exerciseId: number;
  sets: WorkoutItemDTO[];
}

interface WorkoutFullDTO {
  id: number;
  name: string;
  workoutItems: WorkoutItemDTO[];
}

interface Exercise {
  id: number;
  name: string;
  image: string;
}

export default function WorkoutDetailsPage() {
  const { id } = useParams();
  const [loading, setLoading] = useState(true);
  const [groupedItems, setGroupedItems] = useState<GroupedWorkout[]>([]);
  const [isEditing, setIsEditing] = useState(false);
  const [workoutName, setWorkoutName] = useState('');
  const [newSetCounter, setNewSetCounter] = useState<number>(-1);
  const [showExerciseList, setShowExerciseList] = useState(false);
  const [availableExercises, setAvailableExercises] = useState<Exercise[]>([]);

  useEffect(() => {
    if (id) {
      workoutService.getWorkoutById(Number(id))
        .then(response => {
          const data: WorkoutFullDTO = response.data;
          setWorkoutName(data.name);

          const grouped = data.workoutItems.reduce((acc: Record<string, GroupedWorkout>, item: WorkoutItemDTO) => {
            if (!acc[item.exerciseName]) {
              acc[item.exerciseName] = {
                exerciseName: item.exerciseName,
                exerciseId: item.exerciseId,
                sets: []
              };
            }
            acc[item.exerciseName].sets.push(item);
            return acc;
          }, {} as Record<string, GroupedWorkout>);

          const groupedArray = Object.keys(grouped).map(key => grouped[key]);
          setGroupedItems(groupedArray);
          setLoading(false);
        })
        .catch(error => {
          console.error(error);
          setLoading(false);
        });
    }
  }, [id]);

  const fetchExercises = async () => {
    const response = await exerciseService.getExercises();
    setAvailableExercises(response.data.content);
    setShowExerciseList(true);
  };

  function handleAddExercise(exercise: Exercise) {
    setGroupedItems(prev => [
      ...prev,
      {
        exerciseName: exercise.name,
        exerciseId: exercise.id,
        sets: [
          {
            exerciseId: exercise.id,
            exerciseName: exercise.name,
            setNumber: 1,
            reps: '12',
            rest: 60,
            weight: 0,
            image: exercise.image,
          }
        ]
      }
    ]);
    setShowExerciseList(false);
  }

  function handleRemoveExercise(groupIndex: number) {
    setGroupedItems(prev => prev.filter((_, idx) => idx !== groupIndex));
  }

  function handleSetChange(groupIndex: number, setIndex: number, field: keyof WorkoutItemDTO, value: WorkoutItemDTO[keyof WorkoutItemDTO]) {
    setGroupedItems(prev => prev.map((group, idx) => {
      if (idx !== groupIndex) return group;
      return {
        ...group,
        sets: group.sets.map((set, sIdx) => {
          if (sIdx !== setIndex) return set;
          return { ...set, [field]: value };
        })
      };
    }));
  }

  function handleAddSet(groupIndex: number) {
    setGroupedItems(prev => prev.map((group, idx) => {
      if (idx !== groupIndex) return group;
      const newId = newSetCounter;
      const newSet: WorkoutItemDTO = {
        id: newId,
        exerciseId: group.exerciseId,
        exerciseName: group.exerciseName,
        setNumber: group.sets.length + 1,
        reps: '12',
        rest: 60,
        weight: 0,
        image: group.sets[0].image
      };
      return { ...group, sets: [...group.sets, newSet] };
    }));
    setNewSetCounter(prev => prev - 1);
  }

  function handleRemoveSet(groupIndex: number, setIndex: number) {
    setGroupedItems(prev => prev.map((group, idx) => {
      if (idx !== groupIndex) return group;
      return { ...group, sets: group.sets.filter((_, i) => i !== setIndex) };
    }));
  }

  function buildPayload(): WorkoutFullDTO {
    const workoutItems: WorkoutItemDTO[] = [];
    groupedItems.forEach(group => {
      group.sets.forEach(set => {
        workoutItems.push(set);
      });
    });
    return {
      id: Number(id),
      name: workoutName,
      workoutItems
    };
  }

  async function handleSave() {
    try {
      const payload = buildPayload();
      await workoutService.updateWorkout(Number(id), payload);
      setIsEditing(false);
    } catch (error) {
      console.error(error);
      alert('Erro ao atualizar o treino.');
    }
  }

  if (loading) return <div>Carregando...</div>;

  return (
    <div className={style.container}>
      <div className={style.title}>
        {isEditing ? (
          <button onClick={() => setIsEditing(false)} >
            <FaArrowLeft className={style.arrow} />
          </button>
        ) : (
          <Link href={'/workouts'}>
            <FaArrowLeft className={style.arrow} />
          </Link>
        )}

        {isEditing ? (
          <>
            <input
              type="text"
              value={workoutName}
              onChange={e => setWorkoutName(e.target.value)}
            />
            <button className={style.add_exercise_button} onClick={fetchExercises}>Adicionar Exercício</button>
            <button className={style.save_button} onClick={handleSave}>Salvar Alterações</button>
          </>
        ) : (
          <h1>{workoutName}</h1>
        )}
        <button className={style.edit_button} onClick={() => setIsEditing(!isEditing)}>
          {!isEditing && <RiPencilFill />}
        </button>
      </div>

      {showExerciseList && (
        <div className={style.modal_container}>
          <div className={style.modal}>
            <h3>Escolha um exercício:</h3>
            {availableExercises.map(ex => (
              <div key={ex.id} onClick={() => handleAddExercise(ex)}>
                <ExerciseCard exercise={ex} />
              </div>
            ))}
            <button
              className={style.cancel_button}
              onClick={() => setShowExerciseList(false)}>Cancelar</button>
          </div>
        </div>
      )
      }

      <div className={style.items_container}>
        <div className={style.exercise_container}>
        {groupedItems.map((group, gIndex) => (
          <div key={group.exerciseName} className={style.exercise_group}>
            <div className={style.exercise_header}>
              <WorkoutItemsCard name={group.exerciseName} image={group.sets[0].image} />
              {isEditing && (
                <button
                  className={style.remove_exercise_button}
                  onClick={() => handleRemoveExercise(gIndex)}>
                  <FaRegTrashAlt />
                </button>
              )}
            </div>
            {group.sets.map((set, sIndex) => (
              <div key={`${gIndex}-${sIndex}`}>
                {isEditing ? (
                  <>
                    <div className={style.sets_header}>
                      <div>SÉRIE</div>
                      <div>PESO</div>
                      <div>REPS</div>
                      <div>DESCANSO</div>
                    </div>

                    <div className={style.sets}>
                      <input
                        type="number"
                        value={set.setNumber}
                        onChange={e => handleSetChange(gIndex, sIndex, 'setNumber', parseInt(e.target.value))}
                        placeholder="Série"
                      />
                      <input
                        type="number"
                        value={set.weight ?? ''}
                        onChange={e => handleSetChange(gIndex, sIndex, 'weight', parseInt(e.target.value))}
                        placeholder="Peso"
                      />
                      <input
                        type="text"
                        value={set.reps}
                        onChange={e => handleSetChange(gIndex, sIndex, 'reps', e.target.value)}
                        placeholder="Reps"
                      />
                      <input
                        type="number"
                        value={set.rest}
                        onChange={e => handleSetChange(gIndex, sIndex, 'rest', parseInt(e.target.value))}
                        placeholder="Descanso"
                      />
                    </div>

                    {isEditing && sIndex === group.sets.length - 1 && (
                      <div className={style.add_remove_buttons}>
                        {group.sets.length > 1 && (
                          <button
                            className={style.remove_button}
                            onClick={() => handleRemoveSet(gIndex, sIndex)}>
                            x
                          </button>
                        )}
                        <button
                          className={style.add_button}
                          onClick={() => handleAddSet(gIndex)}>
                          +
                        </button>
                      </div>
                    )}

                  </>
                ) : (
                  <SetsCard set={set.setNumber} weight={set.weight} reps={set.reps} rest={set.rest} />
                )}
              </div>
            ))}
          </div>
        ))}
        </div>
      </div>
    </div >
  );
}