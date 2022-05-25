import React from "react";
import {TutorNav} from "../../../../../components/navigate/Tutor/TutorNav";
import {Back} from "../../../../../components/back/Back";
import {VDash} from "../dashboard/VDash";
import {Container, Col, Row} from "react-bootstrap";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {ButtonC} from "../../../../../components/buttons/ButtonC/ButtonC";
import {useNavigate} from "react-router-dom";

export function VStat(props) {
    const router = useNavigate();
    const createPagination = () => {
        let view = [];

        for (let index = 0; index < props.attempts.length; index++){
            view.push(
                <ButtonC text={index+1} onClick={() => {
                    props.setCurrentAttempt(index);
                    props.refresh();
                }}/>
            )
        }
        view = (
            <div style={{display: "flex", justifyContent: "space-around"}}>
                view
            </div>
        )
        return view;
    }

    return (
        <Back navPanel={<TutorNav context={props.context}/>}>
            <Container>
                <Row>
                    <ButtonA width={200} text={"Вернуться"} onClick={() => router("/user/tutor/detail")}/>
                </Row>
                <Row>
                    <div style={
                        {
                            display: "flex",
                            justifyContent: "space-around",
                        }
                    }>
                        {
                            0 < props.currentAttempt ?
                            <ButtonA text={"Предыдущая"} onClick={
                                () => {props.setCurrentAttempt(props.currentAttempt - 1); props.refresh();}
                            }/> : null
                        }
                        {
                            props.attempts > 2 ? createPagination() : null
                        }
                        {
                            props.attempts.length-1 > props.currentAttempt ?
                            <ButtonA text={"Следующая"} onClick={
                                () => {props.setCurrentAttempt(props.currentAttempt + 1); props.refresh();}
                            }/> : null
                        }
                    </div>
                </Row>
                <Row>
                    {
                        <VDash
                            allTime={props.allTime}
                            clicks={props.clicks}
                            answers={props.answers}
                            timelines={props.timelines}
                        />
                    }
                </Row>
            </Container>
        </Back>
    )
}