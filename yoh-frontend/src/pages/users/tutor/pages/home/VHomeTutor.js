import React from "react";
import {TutorNav} from "../../../../../components/navigate/Tutor/TutorNav";
import {Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import {Back} from "../../../../../components/back/Back";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import {RightArrow} from "../../../../../components/arrows/RightArrow";
import {useNavigate} from "react-router-dom";
import {ProgressBar} from "../../../../../components/progressBar/ProgressBar";

export function VHomeTutor(props) {
    const router = useNavigate();

    const createViewUsers = () => {
        let users = props.users.slice(-5),
            view = [];

        if (users.length > 0){
            users.forEach((user) => {
                view.push(
                    <InfoBlock key={user["id"]} text={
                        user["surname"] && user["name"] ?
                            user["surname"] + user["name"] : "Отсутствует ФИО"
                    }>
                        <div>
                            <img style={{width: "100%"}} src={profileStub} alt={'profile'}/>
                        </div>
                    </InfoBlock>
                )
            })
        }else{
            view.push(
                <div style={
                    {
                        height: 387,
                        width: 936,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center"
                    }
                }>
                    <h3>Пользователи в системе отсутствует.</h3>
                </div>
            )
        }

        return (
            <div>
                <h3 style={{marginTop: 20}}>Последняя активность пользователей:</h3>
                <div style={{display: "flex", justifyContent: "space-evenly"}}>
                    {view}
                    {view.length === 5 ? <RightArrow onClick={() => router("/user/tutor/patients/")}/> : null}
                </div>
            </div>
        )
    }

    return (
        <Back navPanel={<TutorNav/>}>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{fontWeight: "bold"}}>Добрый день!</h1>
                    {createViewUsers()}
                </Row>
            </Container>
            <Container>
                <Row>
                    <div style={
                        {
                            marginTop: 30,
                            marginBottom: 20,
                            background: "#F5F5F5",
                            boxShadow: "-10px 10px 30px rgba(0, 0, 0, 0.32)",
                            borderRadius: 40,
                        }
                    }>
                        <h3 style={{margin: "20px 50px"}}>Отслеживание успехов</h3>
                        <div style={
                            {
                                padding: "10px 20px 30px 20px"
                            }
                        }>
                            <label>Пользователь 1: </label>
                            <ProgressBar percent={60}/>
                            <label>Пользователь 2: </label>
                            <ProgressBar percent={70}/>
                            <label>Пользователь 3: </label>
                            <ProgressBar percent={85}/>
                        </div>
                    </div>
                </Row>
            </Container>
        </Back>
    )
}