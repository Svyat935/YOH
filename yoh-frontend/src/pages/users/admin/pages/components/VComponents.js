import React, {useContext, useState} from "react";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {Back} from "../../../../../components/back/Back";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";
import Modal from "react-bootstrap/Modal";
import {Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import gameStub from "../../../../../assets/gameStub.jpg";
import {useNavigate} from "react-router-dom";

export function VComponents(props) {
    const filterList = [
        {"text": "По алфавиту", "value": 1},
        {"text": "По дате", "value": 2},
        {"text": "По описанию", "value": 3},
        {"text": "По типу", "value": 4},
    ]
    const router = useNavigate();
    const [show, setShow] = useState(false);
    //TODO: replace the int type with something better.
    //Note: Adding Game - 0, Removing Game - 1, Confirm Removing - 2, Changing Game - 3, Look At Game - 4.
    const [buttonStatus, setButtonStatus] = useState(0);
    const [gameForRemoving, setRemovingGame] = useState(null);
    const [gameForChanging, setChangingGame] = useState(null);

    const createBasicViewGames = () => {
        let games = props.games,
            view = [];
        if (games.length > 0) {
            games.forEach((game) => {
                view.push(
                    <InfoBlock key={game["id"]} text={game["name"]} onClick={
                        () => {
                            setButtonStatus(3);
                            setChangingGame(game);
                            setShow(true);
                        }
                    }>
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
                    <h3>Игры в системе отсутствуют.</h3>
                </div>
            )
        }
        return view;
    }

    const createRemovingViewGames = () => {
        let games = props.games,
            view = [];
        if (games.length > 0) {
            games.forEach((game) => {
                view.push(
                    <div style={
                        {
                            borderRadius: 40,
                            color: "#FFFFFF",
                            background: "#6A6DCD",
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                            marginBottom: "20px"
                        }
                    }>
                        <p>Name: {game["name"]}; Description: {game["description"]}</p>
                        <ButtonB text={"Удалить"} fontSize={"medium"} onClick={
                            () => {
                                setRemovingGame(game);
                                setButtonStatus(2);
                            }
                        }/>
                    </div>
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

        return (
            <div style={{display: "flex", flexDirection: "column"}}>
                {view}
            </div>
        );
    }

    const createShowViewGames = () => {
        let games = props.games,
            view = [];

        if (games.length > 0) {
            games.forEach((game) => {
                view.push(
                    <div style={
                        {
                            borderRadius: 40,
                            color: "#FFFFFF",
                            background: "#6A6DCD",
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                            marginBottom: "20px"
                        }
                    }>
                        <p><b>Name:</b> {game["name"]}; <b>Description:</b> {game["description"]}</p>
                        <ButtonB text={"Посмотреть"} fontSize={"medium"} onClick={
                            () => {
                                let url = "http://" + game["url"] + "?token=" + props.context.token;
                                props.context.addInfo(url);
                                router("/user/admin/game/");
                            }
                        }/>
                    </div>
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

        return (
            <div style={{display: "flex", flexDirection: "column"}}>
                {view}
            </div>
        );
    }

    const addGame = async () => {
        let fName = document.getElementById("name"),
            fDescription = document.getElementById("description"),
            fFile = document.getElementById("file");

        let formData = new FormData();
        formData.append("name", fName.value);
        formData.append("description", fDescription.value);
        formData.append("file", fFile.files[0]);
        let response = await props.sendGames(formData);
        //TODO: Validate.
        props.refresh();
        setShow(false);
    }

    const removeGame = () => {
        //TODO: Validate.
        let response = props.removeGame(gameForRemoving["id"]);
        if (response !== null){
            props.refresh();
            setShow(false);
        }
    }

    const changeGame = async () => {
        // TODO: Validate
        let fName = document.getElementById("name"),
            fDescription = document.getElementById("description");

        let body = {}
        if (fName.value) body["name"] = fName.value;
        if (fDescription.value) body["description"] = fDescription.value;

        if (JSON.stringify(body) !== '{}'){
            body["game_id"] = gameForChanging["id"];
            let response = await props.changeGame(body);
            props.refresh();
        }

        setShow(false);
    }

    const changingView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
                <label>Новое Название: </label>
                <input id={"name"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p>Текущее название: {gameForChanging["name"]}</p>
                <p id={"name-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Описание: </label>
                <input id={"description"} type={"email"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p>Текущее описание: {gameForChanging["description"]}</p>
                <p id={"description-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
            </div>
        )
    }

    const createGamesView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
                <label>Название: </label>
                <input id={"name"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"name-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Описание: </label>
                <input id={"description"} type={"email"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"description-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Файл: </label>
                <input id={"file"} type={"file"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                } required/>
                <p id={"file-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
            </div>
        )
    }

    return (
        <Back navPanel={<AdminNav/>}>
            <Modal
                show={show}
                backdrop={true}
                keyboard={true}
                onHide={() => setShow(false)}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{
                        buttonStatus === 0 ? "Добавление Игры" :
                            buttonStatus === 1 || buttonStatus === 2 ? "Удаление Игры" :
                                buttonStatus === 3 ? "Изменение Игры" : "Просмотр Игры"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus === 0 ?
                            createGamesView() : buttonStatus === 1 ?
                            createRemovingViewGames() : buttonStatus === 2 ?
                                "Вы уверен что хотите удалить игру с таким названием: '" + gameForRemoving["name"] + "' ?" :
                                buttonStatus === 3 ? changingView() : createShowViewGames()
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
                            buttonStatus === 0 ?
                                <ButtonB text={"Добавить"} onClick={addGame}/> :
                                buttonStatus === 2 ?
                                    <ButtonB text={"Удалить"} onClick={removeGame}/> :
                                    buttonStatus === 3 ?
                                        <ButtonB text={"Изменить"} onClick={changeGame}/> : null
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
                        <ButtonA width={300} text={"Посмотреть θ"} onClick={() => {
                            setButtonStatus(4);
                            setShow(true);
                        }}/>
                        <ButtonA width={300} text={"Добавить +"} onClick={() => {
                            setButtonStatus(0);
                            setShow(true);
                        }}/>
                        <ButtonA width={300} text={"Удалить -"} onClick={() => {
                            setButtonStatus(1);
                            setShow(true);
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