import React, {useState} from "react";
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

export function VComponents(props) {
    const filterList = [
        {"text": "По алфавиту", "value": 1},
        {"text": "По дате", "value": 2},
        {"text": "По описанию", "value": 3},
        {"text": "По типу", "value": 4},
    ]
    const [show, setShow] = useState(false);
    //TODO: replace the int type with something better.
    //Note: Adding Game - 0, Removing Game - 1, Confirm Removing - 2, Changing Game - 3
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

    const addGame = async () => {
        let fName = document.getElementById("name"),
            fDescription = document.getElementById("description"),
            fFile = document.getElementById("file");

        console.log(fName.value, fDescription.value, fFile.files[0]);
    }

    const removeGame = () => {
        //TODO: When we're adding a route.
        alert("Remove!");
        setShow(false);
    }

    const changingView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center"
                }
            }>
                <h3>Выберите роль: </h3>
                {/*<ButtonB text={"Пациент"} onClick={*/}
                {/*    async () => {*/}
                {/*        await props.assignRole(1, userForChanging["id"])*/}
                {/*        props.refresh();*/}
                {/*        setShow(false);*/}
                {/*    }*/}
                {/*}/>*/}
                {/*<ButtonB text={"Исследователь"} onClick={*/}
                {/*    async () => {*/}
                {/*        await props.assignRole(2, userForChanging["id"])*/}
                {/*        props.refresh();*/}
                {/*        setShow(false);*/}
                {/*    }*/}
                {/*}/>*/}
                {/*<ButtonB text={"Тьютор"} onClick={*/}
                {/*    async () => {*/}
                {/*        await props.assignRole(3, userForChanging["id"])*/}
                {/*        props.refresh();*/}
                {/*        setShow(false);*/}
                {/*    }*/}
                {/*}/>*/}
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
                        gameForChanging !== null ? "Изменение Игры" :
                            buttonStatus ? "Удаление Игры" :
                                "Добавление Игры"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus === 0 ?
                            createGamesView() : buttonStatus === 1 ?
                            createRemovingViewGames() : buttonStatus === 2 ?
                                "Вы уверен что хотите удалить игру с таким названием:" + gameForRemoving["name"] + "?"
                                : changingView()
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
                                    <ButtonB text={"Удалить"} onClick={removeGame}/> : null
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