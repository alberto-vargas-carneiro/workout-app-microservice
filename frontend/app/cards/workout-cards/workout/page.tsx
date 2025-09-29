import { WorkoutDTO } from "@/app/models/workout";
import style from './page.module.css';
import Link from "next/link";

type WorkoutCardProps = {
    workout: WorkoutDTO;
}

function formatDate(isoString: string): string {
    const date = new Date(isoString);
    return date.toLocaleDateString();
  }

export default function WorkoutCard({ workout }: WorkoutCardProps) {
    return (
        <Link href={`../../../workout-details/${workout.id}`}>	
            <div className={style.container}>
                <div className={style.text}>
                    {workout.name}
                </div>
                <div className={style.date}>{formatDate(workout.date)}</div>
            </div >
        </Link>
    );
}