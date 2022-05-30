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
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";

export function VAllPatients(props) {
    const filterList = [
        {"text": "По алфавиту (возрастание)", "defaultChecked": true, "value": 1, "onClick": () => {
                props.setOrder(1);
                props.refresh();
            }
        },
        {"text": "По алфавиту (убывание)", "value": -1, "onClick": () => {
                props.setOrder(-1);
                props.refresh();
            }
        },
        {"text": "По дате (возрастание)", "value": 2, "onClick": () => {
                props.setOrder(2);
                props.refresh();
            }
        },
        {"text": "По дате (убывание)", "value": -2, "onClick": () => {
                props.setOrder(-2);
                props.refresh();
            }
        },
    ]
    const [show, setShow] = useState(false);
    const [currentPatient, setCurrentPatient] = useState(null);

    const clearAll = () => {
        setCurrentPatient(null);
        setShow(false);
    }

    const createPatientsView = () => {
        let patients = props.patients.slice(0, 9),
            view = [];
        if (patients.length > 0) {

            patients.forEach((patient) => {
                let fio = patient["surname"] && patient["name"] ?
                    patient["surname"] + " " + patient["name"] :
                    "ФИО отсутствует";
                let imageSrc = patient["image"] ?
                    "https://mobile.itkostroma.ru/images/" + patient["image"] :
                    profileStub;
                let attachStatus = props.attachedPatients.some((attachedPatient) => {
                    if (attachedPatient["login"] === patient["login"]) return true;
                });

                view.push(
                    <InfoBlock onClick={
                        async () => {
                            setCurrentPatient(patient);
                            setShow(true);
                        }
                    } key={patient["id"]} text={fio}>
                        <div>
                            <img style={{width: "100%"}} src={imageSrc} alt={'profile'}/>
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
                    <h3>Доступные пациенты в организации отсутствуют.</h3>
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
                    <Modal.Title>Выбрать пользователя</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div style={
                        {
                            display: "flex",
                            justifyContent: "center",
                            flexDirection: "column"
                        }
                    }>
                        <ButtonB text={"Взять под наблюдение"} onClick={async () => {
                            await props.attach(currentPatient["id"]);
                            props.refresh();
                            setShow(false);
                        }}/>
                        {/*<ButtonB text={"Убрать из под наблюдения"} onClick={async () => {*/}
                        {/*    await props.detach(currentPatient["id"]);*/}
                        {/*    props.refresh();*/}
                        {/*    setShow(false);*/}
                        {/*}}/>*/}
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
                    <h1 style={{marginBottom: 20}}>Список пациентов организации</h1>
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
                                {props.patients.length === 10 ? <ButtonA width={300} text={"Следующая страница"} onClick={() => {
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