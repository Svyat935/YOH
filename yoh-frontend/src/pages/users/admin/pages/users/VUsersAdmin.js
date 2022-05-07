import {Back} from "../../../../../components/back/Back";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import React, {useState} from "react";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import Modal from "react-bootstrap/Modal";

export function VUsersAdmin(props) {
    const filterList = [
        {"text": "По алфавиту", "value": 1},
        {"text": "По дате", "value": 2},
        {"text": "Пилигрим", "value": 3},
        {"text": "Тьютор", "value": 4},
        {"text": "Пациент", "value": 5},
        {"text": "Исследователь", "value": 6},
    ]
    const [show, setShow] = useState(false);
    //TODO: replace the bool type with something better.
    //Note: Adding user - false, Removing user - true
    const [buttonStatus, setButtonStatus] = useState(false);

    const createViewUsers = () => {
        let users = props.users,
            view = [];

        if (users.length > 0){
            users.forEach((user) => {
                view.push(
                    <InfoBlock key={user["id"]} text={user["login"]}>
                        <div>
                            <img style={{width: "100%"}} src={profileStub} alt={'profile'}/>
                        </div>
                    </InfoBlock>
                )
            })
        }else{
            view.push(
                <div style={
                    {
                        height: 387,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center"
                    }
                }>
                    <h3>Пользователей в системе отсутствует.</h3>
                </div>
            )
        }
        return view;
    }

    const addUser = async () => {
        let login = document.getElementById("login").value,
            email = document.getElementById("email").value,
            password = document.getElementById("password").value,
            confirmPassword = document.getElementById("confirmPassword").value;

        //TODO: Validation

        let response = await props.createUser(login, email, password);
        console.log(response);
        props.refresh();
        setShow(false);
    }

    const removeUser = () => {
        alert("Remove!");
        setShow(false);
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
                    <Modal.Title>{buttonStatus ? "Удаление пользователя" : "Добавление пользователя"}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus ? (
                            <p> Удаление </p>
                        ) : (
                            <div style={
                                {
                                    display: "flex",
                                    flexDirection: "column"
                                }
                            }>
                                <label>Логин: </label>
                                <input id={"login"} type={"text"} style={
                                    {borderRadius: 40, border: "none", padding: "5px 15px"}
                                }/>
                                <label>Электронная почта: </label>
                                <input id={"email"} type={"email"} style={
                                    {borderRadius: 40, border: "none", padding: "5px 15px"}
                                }/>
                                <label>Пароль: </label>
                                <input id={"password"} type={"password"} style={
                                    {borderRadius: 40, border: "none", padding: "5px 15px"}
                                }/>
                                <label>Подтвердить пароль: </label>
                                <input id={"confirmPassword"} type={"password"} style={
                                    {borderRadius: 40, border: "none", padding: "5px 15px"}
                                }/>
                            </div>
                        )
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
                        <ButtonB text={"Закрыть"} onClick={() => setShow(false)}/>
                        <ButtonB text={buttonStatus ? "Удалить" : "Добавить"} onClick={buttonStatus ? removeUser : addUser}/>
                    </div>
                </Modal.Footer>
            </Modal>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{marginBottom: 20}}>Список пользователей</h1>
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
                            setButtonStatus(false); setShow(true);
                        }}/>
                        <ButtonA width={300} text={"Удалить -"} onClick={() => {
                            setButtonStatus(true); setShow(true);
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
                                    {createViewUsers()}
                                </Col>
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}