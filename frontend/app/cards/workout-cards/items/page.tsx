import style from './page.module.css';
import Image from 'next/image';

interface ItemsCardProps {
    name: string;
    video: string;
}

export default function WorkoutItemsCard(props: ItemsCardProps) {
    return (
        <div className={style.container}>
            <div className={style.exercise_name}>
                <Image src={props.video} width={65} height={65} alt='exercício' />
                <span>{props.name}</span>
            </div>
        </div>
    );
}