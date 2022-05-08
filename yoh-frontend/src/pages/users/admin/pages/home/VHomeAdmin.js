import {Back} from "../../../../../components/back/Back";
import React from "react";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import gameStub from "../../../../../assets/gameStub.jpg";
import {RightArrow} from "../../../../../components/arrows/RightArrow";
import "./HomeAdmin.css";
import {useNavigate} from "react-router-dom";


export function VHomeAdmin(props) {
    const router = useNavigate();

    const createViewUsers = () => {
        let users = props.users.slice(-5),
            view = [];

        if (users.length > 0){
            users.forEach((user) => {
                view.push(
                    <InfoBlock key={user["id"]} text={user["login"]}>
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
                    <h3>Пользователей в системе отсутствует.</h3>
                </div>
            )
        }

        return (
            <div>
                <h3 style={{marginTop: 20}}>Последние зарегистрированные пользователи:</h3>
                <div style={{display: "flex", justifyContent: "space-evenly"}}>
                    {view}
                    {view.length === 5 ? <RightArrow onClick={() => router("/user/admin/users/")}/> : null}
                </div>
            </div>
        )
    }

    const createViewGames = () => {
        let games = props.games.slice(-5),
            view = [];

        if (games.length > 0){
            games.forEach((game) => {
                view.push(
                    <InfoBlock key={game["id"]} text={game["name"]}>
                        <div>
                            <img style={{width: "100%"}} src={gameStub} alt={'game'}/>
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
                    <h3>Игры в системе отсутствуют.</h3>
                </div>
            )
        }

        return (
            <div>
                <h3 style={{marginTop: 20}}>Последние игры в системе:</h3>
                <div style={{display: "flex", justifyContent: "space-evenly"}}>
                    {view}
                    {view.length === 5 ? <RightArrow onClick={() => router("/admin/components_page/")}/> : null}
                </div>
            </div>
        )
    }

    return (
        <Back navPanel={<AdminNav/>}>
            <Container style={{marginTop: 20}}>
                <Col>
                    <Row>
                        <h2 style={{fontWeight: "bold"}}>Добрый день, Администратор!</h2>
                        {createViewUsers()}
                        {createViewGames()}
                    </Row>
                </Col>
            </Container>
        </Back>
    )
}