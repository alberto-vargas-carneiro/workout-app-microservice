INSERT INTO tb_workout (name, date, user_id) VALUES ('Treino de Peito', TIMESTAMP WITH TIME ZONE '2022-05-25T13:00:00Z', 1);
INSERT INTO tb_workout (name, date, user_id) VALUES ('Treino de Costas', TIMESTAMP WITH TIME ZONE '2023-02-05T13:00:00Z', 2);
INSERT INTO tb_workout (name, date, user_id) VALUES ('Treino de Braço', TIMESTAMP WITH TIME ZONE '2024-01-20T13:00:00Z', 2);
INSERT INTO tb_workout (name, date, user_id) VALUES ('Treino de Perna', TIMESTAMP WITH TIME ZONE '2022-07-13T13:00:00Z', 1);

INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (1, 1, 1, '12', 60, 20);
INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (1, 1, 2, '10', 100, 30);
INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (1, 1, 3, '8', 120, 55);
INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (1, 2, 4, '12', 120, 10);
INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (2, 3, 4, '12', 120, 70);
INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (3, 4, 4, '12', 120, 40);
INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (3, 5, 4, '12', 120, 20);
INSERT INTO tb_workout_item (workout_id, exercise_id, set_number, reps, rest, weight) VALUES (4, 6, 4, '12', 120, 60);