import {TutorNav} from "../../../../../components/navigate/Tutor/TutorNav";
import Modal from "react-bootstrap/Modal";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {Col, Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {Back} from "../../../../../components/back/Back";
import React, {useState} from "react";
import {InfoBlockStatic} from "../../../../../components/infoBlockStatic/InfoBlockStatic";
import {ProgressBar} from "../../../../../components/progressBar/ProgressBar";
import gameStub from "../../../../../assets/gameStub.jpg";
import {useNavigate} from "react-router-dom";
import profileStub from "../../../../../assets/profileStub.jpg";

export function VDetailInfo(props) {
    const router = useNavigate();

    const createGamesView = () => {
        let games = props.user.games,
            view = [];

        if (games !== null && games !== undefined && games.length > 0){
            games.forEach((game) => {
                view.push(
                    <InfoBlockStatic key={game["id"]} text={game["name"]}>
                        <div>
                            <img style={{width: "100%"}} src={gameStub} alt={'game'}/>
                        </div>
                    </InfoBlockStatic>
                )
            })
            view = (
                <div style={{
                    display: "flex",
                    justifyContent: "space-around",
                    flexWrap: "wrap"
                }}>{view}</div>
            )
        }else{
            view.push(
                <div style={
                    {
                        height: 200,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center"
                    }
                }>
                    <h2>Игры ещё не назначены.</h2>
                </div>
            )
        }
        return view;
    }

    return (
        <Back navPanel={<TutorNav context={props.context}/>}>
            <Container>
                <Row>
                    <Col md={4} style={
                        {
                            display: "flex",
                            justifyContent: "flex-start",
                            alignItems: "center",
                            flexDirection: "column"
                        }
                    }>
                        <InfoBlockStatic title={
                            props.user["name"] && props.user["surname"] ?
                                props.user["surname"] + " " + props.user["name"] :
                                "Отсутствуют данные"
                            }
                        >
                            <div>
                                <img style={{width: "100%"}} src={
                                    props.user["image"] ? props.user["image"] : profileStub
                                } alt={'game'}/>
                            </div>
                        </InfoBlockStatic>
                        <p>Телефон: {props.user["phone"] ? props.user["phone"] : "Отсутствует"}</p>
                        <p>Эл.почта: {props.user["email"] ? props.user["email"] : "Отсутствует"}</p>
                        <ButtonA width={300} text={"Добавить траекторию"} onClick={() => {
                            router("/user/tutor/vector/");
                        }}/>
                        <ButtonA width={300} text={"Удалить траекторию"} onClick={() => {
                            router("/user/tutor/delete/");
                        }}/>
                    </Col>
                    <Col md={8}>
                        <h2>Статистика успеваемости: </h2>
                        <div style={{marginTop: 20}}>
                            <div style={
                                {
                                    padding: "20px 30px 20px 30px",
                                    background: "#F5F5F5",
                                    boxShadow: "-10px 10px 30px rgba(0, 0, 0, 0.32)",
                                    borderRadius: 40,
                                    marginBottom: 20
                                }
                            }>
                                <label>Готово: </label>
                                <ProgressBar percent={60}/>
                                <label>Не готово: </label>
                                <ProgressBar percent={70}/>
                                <label>Не успешно: </label>
                                <ProgressBar percent={85}/>
                                <div style={{marginTop: 10}}>
                                    <ButtonA text={"Расширенная статистика"} onClick={() => {alert("Move to Nikita!")}}/>
                                </div>
                            </div>
                        </div>
                        <h2>Назначенные игры: </h2>
                        <div style={{marginTop: 20}}>
                            {createGamesView()}
                        </div>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}