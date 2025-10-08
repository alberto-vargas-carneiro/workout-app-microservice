import style from './page.module.css';
import Image from 'next/image';

interface ItemsCardProps {
    name: string;
    image: string;
}

export default function WorkoutItemsCard(props: ItemsCardProps) {
    return (
        <div className={style.container}>
            <div className={style.exercise_name}>
                <Image src={props.image} width={65} height={65} alt='exercício' />
                <span>{props.name}</span>
            </div>
        </div>
    );
}