import style from './page.module.css';

interface SetsCardProps {
    set: number;
    weight: number | null;
    reps: string;
    rest: number;
}

export default function SetsCard(props: SetsCardProps) {
    return (
        <>
            <div className={style.stats}>
                <span className={style.one}>SET</span>
                <span className={style.two}>PESO</span>
                <span className={style.three}>REPS</span>
            </div>
            <div className={style.sets}>
                <span className={style.one}>{props.set}</span>
                <span className={style.two}>{props.weight}</span>
                <span className={style.three}>{props.reps}</span>
            </div>
            <div className={style.rest}>
                <span className={style.line}></span>
                <span>{props.rest}s</span>
                <span className={style.line}></span>
            </div>
        </>
    );

}