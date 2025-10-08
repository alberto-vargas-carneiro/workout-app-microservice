import { ExerciseDTO } from '@/app/models/exercise';
import Image from 'next/image';
import style from './page.module.css';


type ExerciseCardProps = {
    exercise: ExerciseDTO;
}

export default function ExerciseCard({ exercise }: ExerciseCardProps) {
    return (
        <>
            <div className={style.container}>
                <div>
                    <Image className={style.image} src={exercise.image} width={65} height={65} alt='exercício' />
                </div>
                <span className={style.text}>{exercise.name}</span>
            </div >
        </>
    );
}