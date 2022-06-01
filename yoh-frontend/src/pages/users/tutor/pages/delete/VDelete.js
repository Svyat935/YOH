import React, {useContext, useState} from "react";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import gameStub from "../../../../../assets/gameStub.jpg";
import {useNavigate} from "react-router-dom";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {Back} from "../../../../../components/back/Back";
import Modal from "react-bootstrap/Modal";
import {Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import {UserContext} from "../../../../../context/userContext";
import {TutorNav} from "../../../../../components/navigate/NavPanel/Tutor/TutorNav";

export function VDelete(props) {
    const filterList = [
        {"text": "По алфавиту", "value": 1},
        {"text": "По дате", "value": 2},
        {"text": "По описанию", "value": 3},
        {"text": "По типу", "value": 4},
    ]
    const router = useNavigate();
    const [show, setShow] = useState(false);
    //TODO: replace the int type with something better.
    //Note: Choose action - 0; Confirm Add Game - 1;
    const [buttonStatus, setButtonStatus] = useState(0);
    const [game, setGame] = useState(null);

    const createBasicViewGames = () => {
        let games = props.games,
            view = [];
        games = games.filter((game) => game["active"] !== "DELETED");

        if (games.length > 0) {
            games.forEach((game) => {
                view.push(
                    <InfoBlock key={game["id"]} text={game["name"]} onClick={
                        () => {
                            setButtonStatus(0);
                            setGame(game);
                            setShow(true);
                        }
                    }>
                        <div>
                            <img style={{width: "100%", borderRadius: 40}} src={gameStub} alt={'game'}/>
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
                    <h3>Игры в системе отсутствуют.</h3>
                </div>
            )
        }
        return view;
    }

    const chooseActionView = () => {
        return (
            <div style={{
                display: "flex",
                justifyContent: "center"
            }}>
                <ButtonB text={"Показать игру"} onClick={() => {
                    let url = "http://" + game["url"] + "?token=" + props.context.token;
                    let patient = props.context.info.patient;
                    props.context.addInfo({url: url, patient: patient});
                    window.open("/user/tutor/game/", "_blank");
                    setShow(false);
                }}/>
                <ButtonB text={"Удалить из вектора"} onClick={() => {
                    setButtonStatus(1);
                }}/>
            </div>
        )
    }

    return (
        <Back navPanel={<TutorNav context={props.context}/>}>
            <Modal
                show={show}
                backdrop={true}
                keyboard={true}
                onHide={() => setShow(false)}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{
                        buttonStatus === 0 ? "Выберите действие" : "Подтвердить удаление"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus === 0 ? chooseActionView() :
                            <p>Вы точно хотите удалить игру '{game["name"]}' в векторе пользователя ?</p>
                    }
                </Modal.Body>
                <Modal.Footer>
                    <div style={
                        {
                            height: "25%",
                            width: "100%",
                            display: "flex",
                            justifyContent: "space-between"
                        }
                    }>
                        <ButtonB text={"Отмена"} onClick={() => setShow(false)}/>
                        {
                            buttonStatus === 0 ? null :
                                <ButtonB text={"Подтвердить"} onClick={async () => {
                                    await props.deleteGame(game["id"]);
                                    props.refresh();
                                    setShow(false);
                                }}/>
                        }
                    </div>
                </Modal.Footer>
            </Modal>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{marginBottom: 20}}>Список компонентов: </h1>
                    <Col md={4} style={
                        {
                            display: "flex",
                            justifyContent: "flex-start",
                            alignItems: "center",
                            flexDirection: "column"
                        }
                    }>
                        <FilterBlock filters={filterList}/>
                        <ButtonA width={300} text={"Вернуться"} onClick={() => {
                            router("/user/tutor/detail");
                        }}/>
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