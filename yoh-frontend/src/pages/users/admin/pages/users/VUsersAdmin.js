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
    //TODO: replace the int type with something better.
    //Note: Adding user - 0, Removing user - 1, Confirm Removing - 2, Changing user - 3
    const [buttonStatus, setButtonStatus] = useState(0);
    const [userForRemoving, setRemovingUser] = useState(null);
    const [userForChanging, setChangingUser] = useState(null);

    const createBasicViewUsers = () => {
        let users = props.users,
            view = [];
        if (users.length > 0) {
            users.forEach((user) => {
                let role = user["role"] === 0 ? "Администратор":
                    user["role"] === 1 ? "Наблюдаемый" :
                        user["role"] === 2 ? "Исследователь" :
                            user["role"] === 3 ? "Тьютор" : "Без Роли"
                view.push(
                    <InfoBlock onClick={
                        () => {
                            setChangingUser(user);
                            setButtonStatus(3);
                            setShow(true);
                        }
                    } key={user["id"]} text={user["login"]} addText={role}>
                        <div>
                            <img style={{width: "100%"}} src={profileStub} alt={'profile'}/>
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
                    <h3>Пользователи в системе отсутствуют.</h3>
                </div>
            )
        }
        return view;
    }

    const createRemovingViewUsers = () => {
        let users = props.users,
            view = [];

        if (users.length > 0) {
            users.forEach((user) => {
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
                        <p>Login: {user["login"]}; Email: {user["email"]}</p>
                        <ButtonB text={"Удалить"} fontSize={"medium"} onClick={
                            () => {
                                setRemovingUser(user);
                                setButtonStatus(2);
                            }
                        }/>
                    </div>
                )
            })
        } else {
            view.push(
                <div style={{display: "flex", justifyContent: "center", alignItems: "center"}}>
                    <h3>Пользователей в системе отсутствует.</h3>
                </div>
            )
        }

        return (
            <div style={{display: "flex", flexDirection: "column"}}>
                {view}
            </div>
        );
    }

    const validateEmail = (email) => {
        return String(email)
            .toLowerCase()
            .match(
                /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            );
    };

    const addUser = async () => {
        const validStyle = "1px red solid",
            removeBorderStyle = "none";
        let fLogin = document.getElementById("login"),
            fEmail = document.getElementById("email"),
            fPassword = document.getElementById("password"),
            fConfirmPassword = document.getElementById("confirmPassword");
        let vLogin = document.getElementById("login-validate"),
            vEmail = document.getElementById("email-validate"),
            vPassword = document.getElementById("password-validate");
        let validStatus = true;

        vEmail.textContent = "";
        vEmail.style.marginBottom = "0";
        vPassword.textContent = "";
        vPassword.style.marginBottom = "0";
        vLogin.textContent = "";
        vLogin.style.marginBottom = "0";
        fLogin.style.border = removeBorderStyle;
        fEmail.style.border = removeBorderStyle;
        fPassword.style.border = removeBorderStyle;
        fConfirmPassword.style.border = removeBorderStyle;

        if (!fLogin.value) {
            fLogin.style.border = validStyle;
            validStatus = false;
        }
        if (!fEmail.value) {
            fEmail.style.border = validStyle;
            validStatus = false;
        }
        if (!fPassword.value) {
            fPassword.style.border = validStyle;
            validStatus = false;
        }
        if (!fConfirmPassword.value) {
            fConfirmPassword.style.border = validStyle;
            validStatus = false;
        }


        if (fEmail.value && validateEmail(fEmail.value) === null) {
            fEmail.style.border = validStyle;
            vEmail.textContent = "Электронная почта имеет неправильный формат.";
            vEmail.style.marginBottom = "20px";
            validStatus = false;
        }
        if (fPassword.value !== fConfirmPassword.value) {
            fPassword.style.border = validStyle;
            fConfirmPassword.style.border = validStyle;
            vPassword.textContent = "Пароли не совпадают."
            vPassword.style.marginBottom = "20px";
            validStatus = false;
        }

        if (validStatus === true) {
            let response = await props.createUser(fLogin.value, fEmail.value, fPassword.value);
            if (response.code === 401) {
                let message = response['jsonObject']["message"];
                validStatus = false;

                if (message.indexOf('email') !== -1) {
                    vEmail.textContent = "Данная электронная почта уже существует.";
                    vEmail.style.marginBottom = "20px";
                    fEmail.style.border = validStyle;
                }

                if (message.indexOf('login') !== -1) {
                    vLogin.textContent = "Данный логин уже существует.";
                    vLogin.style.marginBottom = "20px";
                    fLogin.style.border = validStyle;
                }
            }
        }

        if (validStatus === true) {
            props.refresh();
            setShow(false);
        }
    }

    const removeUser = () => {
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
                <ButtonB text={"Пациент"} onClick={
                    async () => {
                        await props.assignRole(1, userForChanging["id"])
                        props.refresh();
                        setShow(false);
                    }
                }/>
                <ButtonB text={"Исследователь"} onClick={
                    async () => {
                        await props.assignRole(2, userForChanging["id"])
                        props.refresh();
                        setShow(false);
                    }
                }/>
                <ButtonB text={"Тьютор"} onClick={
                    async () => {
                        await props.assignRole(3, userForChanging["id"])
                        props.refresh();
                        setShow(false);
                    }
                }/>
            </div>
        )
    }

    const createUserView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
                <label>Логин: </label>
                <input id={"login"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"login-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Электронная почта: </label>
                <input id={"email"} type={"email"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"email-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Пароль: </label>
                <input id={"password"} type={"password"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                } required/>
                <p id={"password-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Подтвердить пароль: </label>
                <input id={"confirmPassword"} type={"password"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                } required/>
                <p id={"confirmPassword-validate"}
                   style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
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
                        userForChanging !== null ? "Изменение пользователя" :
                            buttonStatus ? "Удаление пользователя" :
                                "Добавление пользователя"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus === 0 ?
                            createUserView() : buttonStatus === 1 ?
                                createRemovingViewUsers() : buttonStatus === 2 ?
                                "Вы уверен что хотите удалить пользователя c Логиным:" + userForRemoving["login"] + "?"
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
                                <ButtonB text={"Добавить"} onClick={addUser}/> :
                                    buttonStatus === 2 ?
                                        <ButtonB text={"Удалить"} onClick={removeUser}/> : null
                        }
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
                                    {createBasicViewUsers()}
                                </Col>
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}