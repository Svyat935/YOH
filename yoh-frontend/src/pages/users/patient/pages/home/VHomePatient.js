import React from "react";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import {Back} from "../../../../../components/back/Back";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import gameStub from "../../../../../assets/gameStub.jpg";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import {PatientNav} from "../../../../../components/navigate/Patient/PatientNav";
import {useNavigate} from "react-router-dom";

export function VHomePatient(props) {
    let filterList =[
        {"text": "По названию", "value": 1},
        {"text": "По описанию", "value": 2},
        {"text": "По статусу", "value": 3},
    ]
    const router = useNavigate();
    const createBasicViewGames = () => {
        let games = props.games,
            view = [];
        if (games.length > 0) {
            games.forEach((game) => {
                view.push(
                    <InfoBlock key={game["id"]} text={game["name"]} onClick={() => {
                        let info = props.context.info;
                        info["url"] = "http://" + game["url"] + "?token=" + props.context.token;
                        props.context.addInfo(info);
                        router("/user/patient/game");
                    }}>
                        <div>
                            <img style={{width: "100%"}} src={gameStub} alt={'game'}/>
                        </div>
                    </InfoBlock>
                )
            })
        } else {
            view.push(
                <div style={
                    {
                        height: 387,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center"
                    }
                }>
                    <h3>Вам ещё не назначили игры, ожидайте.</h3>
                </div>
            )
        }
        return view;
    }

    return (
        <Back navPanel={<PatientNav/>}>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{fontWeight: "bold"}}>Добрый день!</h1>
                    <h2 style={{marginBottom: 20}}>Ваши текущие игры и тесты: </h2>
                    <Col md={4} style={
                        {
                            display: "flex",
                            justifyContent: "flex-start",
                            alignItems: "center",
                            flexDirection: "column"
                        }
                    }>
                        <FilterBlock filters={filterList}/>
                    </Col>
                    <Col md={8}>
                        <SearchInput onClick={() => console.log("onClick is waiting")}/>
                        <Container>
                            <Row>
                                <Col style={
                                    {
                                        display: "flex",
                                        flexDirection: "row",
                                        flexWrap: "wrap",
                                        justifyContent: "space-evenly"
                                    }
                                }>
                                    {createBasicViewGames()}
                                </Col>
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}