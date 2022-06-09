import {TutorNav} from "../../../../../components/navigate/NavPanel/Tutor/TutorNav";
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
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";

export function VDetailInfo(props) {
    const router = useNavigate();
    const [show, setShow] = useState(false);
    const [gameForDelete, setGameForDelete] = useState(null);
    const allGames = props.status !== null ? props.status["Done"] + props.status["Assigned"] + props.status["Started"] : 0,
        statDone = allGames !== 0 ? Math.round(props.status["Done"] / allGames * 100) : 0,
        statAssigned = allGames !== 0 ? Math.round(props.status["Assigned"] / allGames * 100) : 0,
        statStarted = allGames !== 0 ? Math.round(props.status["Started"] / allGames * 100) : 0;

    const createGamesView = () => {
        let games = props.games,
            view = [];

        if (games !== null && games !== undefined && games.length > 0) {
            games = games.filter((game) => game["active"] !== "DELETED");
            if (games !== null && games !== undefined && games.length > 0){
                games.forEach((game) => {
                    let status = game["status"] === "ASSIGNED" ? "Назначена" :
                        game["status"] === "DONE" ? "Выполнено" :
                            game["status"] === "STARTED" ? "Начата" : "Неудачная";
                    let image = game["image"] !== null ? "https://mobile.itkostroma.ru/images/" + game["image"]
                        : gameStub;

                    view.push(
                        <InfoBlock
                            key={game["id"]}
                            text={"Название: " + game["name"]}
                            addText={"Cтатус: " + status}
                            onClick={() => {
                                let info = props.context.info ? props.context.info : {};
                                info["gamePatientId"] = game["gamePatientId"];
                                info["gameName"] = game["name"];
                                props.context.addInfo(info);
                                router("/user/tutor/stat");
                            }}
                            onClickClose={() => {
                                setGameForDelete(game);
                                setShow(true);
                            }}>
                            <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}}
                                 src={image} alt={'game'}/>
                        </InfoBlock>
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
        } else {
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
        <Back navLeft={
            <ButtonA style={{boxShadow: "none"}}
                     text={"Вернуться"} onClick={() => router("/user/tutor/patients/")}/>
        }
              navPanel={<TutorNav context={props.context}/>}>
            <Modal
                show={show}
                backdrop={true}
                keyboard={true}
                onHide={() => {setShow(false); setGameForDelete(null)}}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{
                        gameForDelete !== null ? "Удаление игры" : "Открепить Пользователя"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        gameForDelete !== null ?
                                "Вы уверены, что хотите удалить игру '" + gameForDelete["name"] +"' у пользователя?" :
                            "Вы уверены, что хотите открепить пользователя: '" +
                                    props.user["surname"] + " " +props.user["name"] + "' ?"

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
                            gameForDelete !== null ?
                                <ButtonB text={"Удалить"} onClick={() => {
                                    props.deleteGame(gameForDelete["id"], props.user["id"]);
                                    props.refresh();
                                    setShow(false);
                                }}/> :
                            <ButtonB text={"Открепить"} onClick={() => {
                                props.detach(props.user["id"]);
                                router("/user/tutor/patients/");
                            }}/>
                        }
                    </div>
                </Modal.Footer>
            </Modal>
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
                            props.user["name"] && props.user["surname"] && props.user["secondName"]?
                                props.user["surname"] + " " + props.user["name"] + " " + props.user['secondName'] :
                                "Отсутствуют данные"
                        }
                        >
                            <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}} src={
                                props.user["image"] ? "https://mobile.itkostroma.ru/images/" + props.user["image"] :
                                    profileStub
                            } alt={'game'}/>
                        </InfoBlockStatic>
                        <p>Пол: {props.user["gender"] ? props.user["gender"] : null}</p>
                        <p>Телефон: {props.user["numberPhone"] ? props.user["numberPhone"] : "Отсутствует"}</p>
                        <p>Эл.почта: {props.user["email"] ? props.user["email"] : "Отсутствует"}</p>
                        <p>Дата Рождения: {props.user["birthDate"] ? props.user["birthDate"].slice(0, 10) : "Не указана"}</p>
                        <p>Адрес: {props.user["address"] ? props.user["address"] : "Не указан"}</p>
                        <ButtonA width={300} text={"Добавить игру"} onClick={() => {
                            router("/user/tutor/vector/");
                        }}/>
                        {/*<ButtonA width={300} text={"Удалить игру"} onClick={() => {*/}
                        {/*    router("/user/tutor/delete/");*/}
                        {/*}}/>*/}
                        <ButtonA width={300} text={"Открепить"} onClick={() => {
                            setShow(true);
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
                                <label>В ожидании: </label>
                                <ProgressBar percent={statAssigned}/>
                                <label>Завершенных: </label>
                                <ProgressBar percent={statDone}/>
                                <label>Начатые: </label>
                                <ProgressBar percent={statStarted}/>
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