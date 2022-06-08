import React, {useState} from "react";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import {Back} from "../../../../../components/back/Back";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import gameStub from "../../../../../assets/gameStub.jpg";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import {PatientNav} from "../../../../../components/navigate/NavPanel/Patient/PatientNav";
import {useNavigate} from "react-router-dom";

export function VHomePatient(props) {
    let filterList =[
        {"text": "По названию (возрастание)", "defaultChecked": true, "value": 1, "onClick": () => {
                setFilterStatus(1);
                props.refresh();
            }
        },
        {"text": "По названию (убывание)", "value": -1, "onClick": () => {
                setFilterStatus(-1);
                props.refresh();
            }
        },
        {"text": "По типу (возрастание)", "value": 2, "onClick": () => {
                setFilterStatus(2);
                props.refresh();
            }
        },
        {"text": "По типу (убывание)", "value": -2, "onClick": () => {
                setFilterStatus(-2);
                props.refresh();
            }
        },
        {"text": "По статусу (возрастание)", "value": 3, "onClick": () => {
                setFilterStatus(3);
                props.refresh();
            }
        },
        {"text": "По статусу (убывание)", "value": -3, "onClick": () => {
                setFilterStatus(-3);
                props.refresh();
            }
        },
    ]
    const router = useNavigate();
    const [filterStatus, setFilterStatus] = useState(0);

    const createBasicViewGames = () => {
        let games = props.games.slice(0, 9),
            view = [];
        if (games.length > 0) {

            games.forEach((game) => {
                if (game["status"] !== "DONE") {

                    let image = game["image"] !== null ? "https://mobile.itkostroma.ru/images/" + game["image"]
                        : gameStub;

                    view.push(
                        <InfoBlock key={game["id"]} text={game["name"]} onClick={() => {
                            props.context.addInfo(
                                {
                                    "url": "https://" + game["url"] + "?" +
                                        "token=" + props.context.token + "&" +
                                        "use_statistics=" + game["useStatistics"]
                                }
                            );
                            router("/user/patient/game");
                        }}>
                            <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}}
                                 src={image} alt={'game'}/>
                        </InfoBlock>
                    )
                }
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

    const searchGame = () => {
        let searchValue = document.getElementById("searchInput").value;
        props.setRegex(searchValue);
        props.refresh();
    }

    return (
        <Back navPanel={<PatientNav context={props.context}/>}>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{fontWeight: "bold"}}>{
                        props.account !== null && props.account["name"] ?
                            "Добрый день, " + props.account["name"] + "!" : "Добрый день!"
                    }</h1>
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