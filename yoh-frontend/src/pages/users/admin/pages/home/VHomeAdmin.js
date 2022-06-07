import {Back} from "../../../../../components/back/Back";
import React from "react";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import {AdminNav} from "../../../../../components/navigate/NavPanel/Admin/AdminNav";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import gameStub from "../../../../../assets/gameStub.jpg";
import {RightArrow} from "../../../../../components/arrows/RightArrow/RightArrow";
import {useNavigate} from "react-router-dom";
import {Slider} from "../../../../../components/slider/Slider";


export function VHomeAdmin(props) {
    const router = useNavigate();

    const createViewUsers = () => {
        let users = props.users.slice(-10),
            view = [];

        if (users.length > 0){
            users.forEach((user) => {
                view.push(
                    <InfoBlock ikey={user["id"]} text={user["login"]} onClick={() => router("/user/admin/users/")}>
                        <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}} src={profileStub} alt={'profile'}/>
                    </InfoBlock>
                )
            })
        }

        return view;
    }

    const createViewGames = () => {
        let games = props.games.slice(-10),
            view = [];

        if (games.length > 0){
            games.forEach((game) => {
                view.push(
                    <InfoBlock ikey={game["id"]} text={game["name"]} onClick={() => router("/user/admin/components/")}>
                        <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}} src={gameStub} alt={'game'}/>
                    </InfoBlock>
                )
            })
        }

        return view;
    }

    return (
        <Back navPanel={<AdminNav context={props.context}/>}>
            <Container style={{marginTop: 20}}>
                <Col>
                    <Row>
                        <h2 style={{fontWeight: "bold"}}>Добрый день, Администратор!</h2>
                        <div>
                            <h3 style={{marginTop: 20}}>Последние зарегистрированные пользователи:</h3>
                            {
                                props.users.length !== 0 ?
                                    <Slider max={4}>
                                        {createViewUsers()}
                                    </Slider> :
                                    <div style={
                                        {
                                            height: 387,
                                            width: "100%",
                                            display: "flex",
                                            justifyContent: "center",
                                            alignItems: "center"
                                        }
                                    }>
                                        <h3>Пользователи в системе отсутствуют.</h3>
                                    </div>
                            }
                        </div>
                        <div>
                            <h3 style={{marginTop: 20}}>Последние игры в системе:</h3>
                            {
                                props.games.length !== 0 ?
                                    <Slider max={4}>
                                        {createViewGames()}
                                    </Slider> :
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
                            }
                        </div>
                    </Row>
                </Col>
            </Container>
        </Back>
    )
}