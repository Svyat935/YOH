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

export function VVector(props) {
    const filterList = [
        {
            "text": "По алфавиту (возрастание)", "defaultChecked": true, "value": 1, "onClick": () => {
                props.setOrder(1)
                props.refresh();
            }
        },
        {
            "text": "По алфавиту (убывание)", "value": -1, "onClick": () => {
                props.setOrder(-1)
                props.refresh();
            }
        },
        {
            "text": "По дате (возрастание)", "value": 2, "onClick": () => {
                props.setOrder(2)
                props.refresh();
            }
        },
        {
            "text": "По дате (убывание)", "value": -2, "onClick": () => {
                props.setOrder(-2)
                props.refresh();
            }
        },
        {
            "text": "По типу (возрастание)", "value": 3, "onClick": () => {
                props.setOrder(3)
                props.refresh();
            }
        },
        {
            "text": "По типу (убывание)", "value": -3, "onClick": () => {
                props.setOrder(-3)
                props.refresh();
            }
        },
    ]
    const router = useNavigate();
    const [show, setShow] = useState(false);
    //TODO: replace the int type with something better.
    //Note: Choose action - 0; Confirm Add Game - 1;
    const [buttonStatus, setButtonStatus] = useState(0);
    const [filterStatus, setFilterStatus] = useState(0);

    const [game, setGame] = useState(null);

    const createBasicViewGames = () => {
        let games = props.games.slice(0, 9),
            view = [];

        if (filterStatus === 1) {
            games = games.sort((a, b) => {
                if (a["name"] > b["name"]) return 1;
                else if (a["name"] < b["name"]) return -1;
                else return 0;
            });
        } else if (filterStatus === 2) {
            games = games.sort((a, b) => {
                if (a["dateAdding"] > b["dateAdding"]) return 1;
                else if (a["dateAdding"] < b["dateAdding"]) return -1;
                else return 0;
            })
        } else if (filterStatus === 3){
            games = games.sort((a, b) => {
                if (a["description"] > b["description"]) return 1;
                else if (a["description"] < b["description"]) return -1;
                else return 0;
            })
        }

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
                        <div style={{width: "100%"}}>
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
                    let url = "https://" + game["url"] + "?" +
                        "token=" + props.context.token + "&" +
                        "use_statistics=" + game["useStatistic"];
                    let patient = props.context.info.patient;
                    props.context.addInfo({url: url, patient: patient});
                    window.open("/user/tutor/game/", "_blank");
                    setShow(false);
                }}/>
                <ButtonB text={"Добавить игру"} onClick={() => {
                    setButtonStatus(1);
                }}/>
            </div>
        )
    }

    const searchGame = () => {
        let searchValue = document.getElementById("searchInput").value;
        props.setRegex(searchValue);
        props.refresh();
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
                        buttonStatus === 0 ? "Выберите действие" : "Подтвердить добавление"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus === 0 ? chooseActionView() :
                            <p>Вы точно хотите добавить игру '{game["name"]}' для пользователя ?</p>
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
                                    await props.addGame(game["id"]);
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
                            router("/user/tutor/detail")
                        }}/>
                    </Col>
                    <Col md={8}>
                        <SearchInput
                            id={"searchInput"}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") {
                                    searchGame()
                                }
                            }}
                            onBlur={searchGame}
                            onClick={searchGame}
                        />
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
                            <Row style={
                                {
                                    display: "flex",
                                    flexDirection: "row",
                                    justifyContent: "space-around",
                                    marginTop: 20
                                }
                            }>
                                {props.start ? <ButtonA width={300} text={"Предыдущая страница"} onClick={() => {
                                    props.setStart(props.start - 9);
                                    props.refresh();
                                }}/> : null}
                                {props.games.length === 10 ? <ButtonA width={300} text={"Следующая страница"} onClick={() => {
                                    props.setStart(props.start + 9);
                                    props.refresh();
                                }}/>: null}
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}