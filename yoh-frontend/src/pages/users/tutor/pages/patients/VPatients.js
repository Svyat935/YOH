import {TutorNav} from "../../../../../components/navigate/NavPanel/Tutor/TutorNav";
import {Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import {Back} from "../../../../../components/back/Back";
import React, {useState} from "react";
import Modal from "react-bootstrap/Modal";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import Col from "react-bootstrap/Col";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import profileStub from "../../../../../assets/profileStub.jpg";
import {useNavigate} from "react-router-dom";
import {ProgressBarCircular} from "../../../../../components/progressBarCircular/progressBarCircular";
import {InfoBlockStatic} from "../../../../../components/infoBlockStatic/InfoBlockStatic";
import {InfoBlockStat} from "../../../../../components/InfoBlockStat/InfoBlockStat";

export function VPatients(props) {
    const filterList = [
        {"text": "По алфавиту", "value": 1, "onClick": () => {
                setFilterStatus(1);
                props.refresh();
            }
        },
        {"text": "По дате", "value": 2, "onClick": () => {
                setFilterStatus(2);
                props.refresh();
            }
        },
    ]
    const router = useNavigate();
    const [show, setShow] = useState(false);
    const [currentPatient, setCurrentPatient] = useState(null);
    const [filterStatus, setFilterStatus] = useState(0);

    const clearAll = () => {
        setCurrentPatient(null);
        setShow(false);
    }

    const createPatientsView = () => {
        let patients = props.attachedPatients.slice(0, 9),
            view = [];

        if (filterStatus === 1) {
            patients = patients.sort((a, b) => {
                if (a["login"] > b["login"]) return 1;
                else if (a["login"] < b["login"]) return -1;
                else return 0;
            });
        } else if (filterStatus === 2) {
            patients = patients.sort((a, b) => {
                if (a["dateRegistration"] > b["dateRegistration"]) return 1;
                else if (a["dateRegistration"] < b["dateRegistration"]) return -1;
                else return 0;
            })
        }

        if (patients.length > 0) {
            patients.forEach((patient) => {
                let fio = patient["surname"] + " " + patient["name"];
                let imageSrc = patient["image"] ? "https://mobile.itkostroma.ru/images/" + patient["image"] : profileStub;
                let status = patient["statusInfo"],
                    all = status["Done"] + status["Failed"] + status["Assigned"] + status["Started"],
                    done = all !== 0 ? status["Done"] / all * 100 : 0,
                    failed = all !== 0 ? status["Failed"] / all * 100 : 0,
                    assigned = all !== 0 ? status["Assigned"] / all * 100 : 0;

                view.push(
                    <InfoBlockStat onClick={
                        async () => {
                            props.saveUser({patient: patient});
                            router("/user/tutor/detail");
                        }
                    }>
                        <InfoBlockStatic key={patient["id"]} text={fio}>
                            <div>
                                <img style={{width: "100%"}} src={imageSrc} alt={'profile'}/>
                            </div>
                        </InfoBlockStatic>
                        <ProgressBarCircular length={done} content={"Завершенные"}/>
                        <ProgressBarCircular length={failed} content={"В ожидании"}/>
                        <ProgressBarCircular length={assigned} content={"Неудачные"}/>
                    </InfoBlockStat>
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
                    <h3>Наблюдаемые пациенты отсутствуют.</h3>
                </div>
            )
        }
        return view;
    }

    const searchPatients = () => {
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
                onHide={clearAll}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{null}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div style={
                        {
                            display: "flex",
                            justifyContent: "center",
                            flexDirection: "column"
                        }
                    }>
                        {null}
                    </div>
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
                        <ButtonB text={"Отмена"} onClick={clearAll}/>
                    </div>
                </Modal.Footer>
            </Modal>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{marginBottom: 20}}>Список наблюдаемых пациентов</h1>
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
                                    searchPatients()
                                }
                            }}
                            onBlur={searchPatients}
                            onClick={searchPatients}
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
                                    {createPatientsView()}
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
                                {props.attachedPatients.length === 10 ? <ButtonA width={300} text={"Следующая страница"} onClick={() => {
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