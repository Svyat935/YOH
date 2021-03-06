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
import {SortBlock} from "../../../../../components/sortBlock/SortBlock";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import {UserContext} from "../../../../../context/userContext";
import {TutorNav} from "../../../../../components/navigate/NavPanel/Tutor/TutorNav";
import {BsSortAlphaUp, BsSortAlphaDown, BsSortNumericDown, BsSortNumericUp} from "react-icons/bs";

export function VVector(props) {
    const filterList = [
        {
            "text": "По алфавиту", "icon": <BsSortAlphaDown size={"1.3em"}/>, "defaultChecked": true, "value": 1, "onClick": () => {
                props.setOrder(1)
                props.refresh();
            }
        },
        {
            "text": "По алфавиту", "icon": <BsSortAlphaUp size={"1.3em"}/>, "value": -1, "onClick": () => {
                props.setOrder(-1)
                props.refresh();
            }
        },
        {
            "text": "По дате добавления", "icon": <BsSortNumericDown size={"1.3em"}/>, "value": 2, "onClick": () => {
                props.setOrder(2)
                props.refresh();
            }
        },
        {
            "text": "По дате добавления", "icon": <BsSortNumericUp size={"1.3em"}/>, "value": -2, "onClick": () => {
                props.setOrder(-2)
                props.refresh();
            }
        },
        {
            "text": "По типу", "icon": <BsSortAlphaDown size={"1.3em"}/>, "value": 3, "onClick": () => {
                props.setOrder(3)
                props.refresh();
            }
        },
        {
            "text": "По типу", "icon": <BsSortAlphaUp size={"1.3em"}/>, "value": -3, "onClick": () => {
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

    const [game, setGame] = useState(null);

    const createBasicViewGames = () => {
        let games = props.games.slice(0, 9),
            view = [];

        if (games.length > 0) {
            games.forEach((game) => {
                let image = game["image"] !== null ? "https://mobile.itkostroma.ru/images/" + game["image"]
                    : gameStub;

                view.push(
                    <InfoBlock
                        key={game["id"]}
                        text={game["name"]}
                        addText={"Тип: " + game["type"]}
                        onClick={
                        () => {
                            setButtonStatus(0);
                            setGame(game);
                            setShow(true);
                        }
                    }>
                        <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}}
                             src={image} alt={'game'}/>
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
            <>
                <div>
                    <p><b>Название</b>: {game ? game["name"]: null}</p>
                    <p><b>Тип</b>: {game ? game["type"] : null}</p>
                    <p><b>Описание</b>: {game ? game["description"] : null}</p>
                </div>
                <div style={{width: "100%", height: "1px", border: "1px solid #FFFFFF"}}/>
                <div style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    flexDirection: "column"
                }}>
                    <ButtonB width={"75%"} text={"Показать игру"} onClick={() => {
                        let url = "https://" + game["url"] + "?" +
                            "token=" + props.context.token + "&" +
                            "use_statistics=" + game["useStatistic"];
                        let patient = props.context.info.patient;
                        props.context.addInfo({url: url, patient: patient});
                        window.open("/user/tutor/game/", "_blank");
                        setShow(false);
                    }}/>
                    <ButtonB width={"75%"} text={"Добавить игру"} onClick={() => {
                        setButtonStatus(1);
                    }}/>
                </div>
            </>
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
                        <SortBlock sorts={filterList}/>
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