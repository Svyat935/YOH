import {Back} from "../../../../../components/back/Back";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import React, {useEffect, useState} from "react";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import Modal from "react-bootstrap/Modal";
import {InputGender} from "../../../../../components/inputGender/InputGender";
import {InputPhone} from "../../../../../components/inputPhone/InputPhone";
import {InputBirthday} from "../../../../../components/inputBirthday/InputBirthday";
import {InputOrganization} from "../../../../../components/InputOrganization/InputOrganization";

export function VUsersAdmin(props) {
    const filterList = [
        {
            "text": "По алфавиту", "value": 1, "onClick": () => {
                props.setRole(-1);
                setFilterStatus(1);
                props.refresh();
            }
        },
        {
            "text": "По дате", "value": 2, "onClick": () => {
                props.setRole(-1);
                setFilterStatus(2);
                props.refresh();
            }
        },
        {
            "text": "Пилигрим", "value": 3, "onClick": () => {
                props.setRole(4);
                setFilterStatus(3);
                props.refresh();
            }
        },
        {
            "text": "Тьютор", "value": 4, "onClick": () => {
                props.setRole(3);
                setFilterStatus(4);
                props.refresh();
            }
        },
        {
            "text": "Пациент", "value": 5, "onClick": () => {
                props.setRole(1);
                setFilterStatus(5);
                props.refresh();
            }
        },
        // {"text": "Исследователь", "value": 6, "onClick": null},
    ]
    const [show, setShow] = useState(false);
    //TODO: replace the int type with something better.
    //Note: Adding user - 0, Removing user - 1, Confirm Removing - 2, Changing user - 3
    //Choose role - 4, Add Info - 5
    //TODO: And I think we can remove some the states.
    const [buttonStatus, setButtonStatus] = useState(0);
    //Note: FilterStatus is FilterList value.
    const [filterStatus, setFilterStatus] = useState(0);

    const [userForRemoving, setRemovingUser] = useState(null);
    const [userForChanging, setChangingUser] = useState(null);
    const [userInfo, setUserInfo] = useState({});
    const [currentUserLogin, setCurrentUserLogin] = useState(null);
    const [role, setRole] = useState(null);
    const validStyle = "1px red solid";
    const removeBorderStyle = "none";

    const clearAll = () => {
        setShow(false);
        setRole(null);
        setCurrentUserLogin(null);
        setUserInfo({});
        setChangingUser(null);
        setRemovingUser(null);
        setButtonStatus(0);
    }

    const createBasicViewUsers = () => {
        let users = props.users.slice(0, 9),
            view = [];

        if (users.length > 0) {
            if (filterStatus === 1) {
                users = users.sort((a, b) => {
                    if (a["login"] > b["login"]) return 1;
                    else if (a["login"] < b["login"]) return -1;
                    else return 0;
                });
            } else if (filterStatus === 2) {
                users = users.sort((a, b) => {
                    if (a["dateRegistration"] > b["dateRegistration"]) return 1;
                    else if (a["dateRegistration"] < b["dateRegistration"]) return -1;
                    else return 0;
                })
            }

            users.forEach((user) => {
                let role = user["role"] === 0 ? "Администратор" :
                    user["role"] === 1 ? "Наблюдаемый" :
                        user["role"] === 2 ? "Исследователь" :
                            user["role"] === 3 ? "Тьютор" : "Без Роли"
                view.push(
                    <InfoBlock onClick={
                        async () => {
                            let userInfo = await props.getInfoUser(user["id"]);
                            if (userInfo !== null) {
                                userInfo = userInfo["jsonObject"];
                                setUserInfo(userInfo);
                            }

                            setChangingUser(user);
                            setButtonStatus(3);
                            setShow(true);
                        }
                    } ikey={user["id"]} text={user["login"]} addText={role}>
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

    const createUserPasswordView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
                <label>Пароль: </label>
                <input id={"password"} type={"password"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                } required/>
                <p id={"password-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Подтвердить пароль: </label>
                <input id={"confirmPassword"} type={"password"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                } required/>
                <p id={"confirmPassword-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
            </div>
        )
    }

    const changeUserPassword = () => {
        let fPassword = document.getElementById("password"),
            fConfirmPassword = document.getElementById("confirmPassword");
        let vPassword = document.getElementById("password-validate");
        let validStatus = true;

        if (!fPassword.value) {
            fPassword.style.border = validStyle;
            validStatus = false;
        }
        if (!fConfirmPassword.value) {
            fConfirmPassword.style.border = validStyle;
            validStatus = false;
        }

        if (fPassword.value !== fConfirmPassword.value) {
            fPassword.style.border = validStyle;
            fConfirmPassword.style.border = validStyle;
            vPassword.textContent = "Пароли не совпадают."
            vPassword.style.marginBottom = "20px";
            validStatus = false;
        }

        if (validStatus){
            //TODO Add route
        }
    }

    const validateEmail = (email) => {
        return String(email)
            .toLowerCase()
            .match(
                /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            );
    };

    const addUser = async () => {
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
            setCurrentUserLogin(fLogin.value);
            setButtonStatus(4);
        }
    }

    const addInfoForTutor = async () => {
        let validStatus = true;
        let fName = document.getElementById("name"),
            fSurname = document.getElementById("surname"),
            fSecondName = document.getElementById("secondName"),
            fOrganization = document.getElementById("input-organizations");
        let user = props.users.filter(user => user.login === currentUserLogin)[0];
        if (!user) user = userForChanging;

        if (!fName.value) {
            fName.style.border = validStyle;
            validStatus = false;
        }
        if (!fSurname.value) {
            fSurname.style.border = validStyle;
            validStatus = false;
        }
        if (!fSecondName.value) {
            fSecondName.style.border = validStyle;
            validStatus = false;
        }

        if (validStatus) {
            let body = {};
            body["name"] = fName.value;
            body["surname"] = fSurname.value;
            body["secondName"] = fSecondName.value;
            body["id"] = user.id;
            body["organization"] = fOrganization.value;

            let response = await props.editTutorInfo(body);

            if (response) {
                if (response.code === 401) {
                    console.log(response);
                    validStatus = false;
                }
            }
        }

        if (validStatus) {
            props.refresh();
            clearAll();
        }
    }

    const addInfoForPatient = async () => {
        let validStatus = true;
        let fName = document.getElementById("name"),
            fSurname = document.getElementById("surname"),
            fSecondName = document.getElementById("secondName"),
            fOrganization = document.getElementById("input-organizations"),
            fAddress = document.getElementById("address"),
            fPhone = document.getElementById("phone"),
            fGender = document.getElementById("gender-input"),
            fBirthday = document.getElementById("input-birthday");
        let user = props.users.filter(user => user.login === currentUserLogin)[0];
        if (!user) user = userForChanging;

        if (!fName.value) {
            fName.style.border = validStyle;
            validStatus = false;
        }
        if (!fSurname.value) {
            fSurname.style.border = validStyle;
            validStatus = false;
        }
        if (!fSecondName.value) {
            fSecondName.style.border = validStyle;
            validStatus = false;
        }

        if (validStatus) {
            let body = {};
            body["name"] = fName.value;
            body["surname"] = fSurname.value;
            body["secondName"] = fSecondName.value;
            body["id"] = user.id;
            if (fPhone.value) body["numberPhone"] = fPhone.value;
            if (fAddress.value) body["address"] = fAddress.value;
            if (fBirthday.value) body["birthDate"] = fBirthday.value;
            if (fOrganization.value) body["organization"] = fOrganization.value;
            if (fGender.value) body["gender"] = fGender.value;

            let response = await props.editPatientInfo(body);

            if (response) {
                if (response.code === 401) {
                    console.log(response);
                    validStatus = false;
                }
            }
        }

        if (validStatus) {
            props.refresh();
            clearAll();
        }
    }

    const chooseChangeAction = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center"
                }
            }>
                <h3>Выберите действие: </h3>
                <ButtonB width={"70%"} text={"Изменить роль"} onClick={
                    () => {
                        setButtonStatus(4);
                    }
                }/>
                {
                    userForChanging["role"] !== 4 ?
                    <ButtonB width={"70%"} text={"Изменить данные"} onClick={
                        () => {
                            setRole(userForChanging["role"]);
                            setButtonStatus(5);
                        }
                    }/> : null
                }
                <ButtonB width={"70%"} text={"Изменить пароль"} onClick={() => {
                    setButtonStatus(1);
                }}/>
            </div>
        )
    }

    const changingRoleView = () => {
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
                        let id = null;
                        if (currentUserLogin !== null) {
                            let user = props.users.filter(
                                (user_) => user_.login === currentUserLogin
                            )[0];
                            id = user["id"];
                        } else {
                            id = userForChanging["id"];
                        }

                        await props.assignRole(1, id);
                        props.refresh();
                        setRole(1);
                        setButtonStatus(5);
                    }
                }/>
                <ButtonB text={"Тьютор"} onClick={
                    async () => {
                        let id = null;
                        if (currentUserLogin !== null) {
                            let user = props.users.filter(
                                (user_) => user_.login === currentUserLogin
                            )[0];
                            id = user["id"];
                        } else {
                            id = userForChanging["id"];
                        }

                        await props.assignRole(3, id);
                        props.refresh();
                        setRole(3);
                        setButtonStatus(5);
                    }
                }/>
                {/*<ButtonB text={"Исследователь"} onClick={*/}
                {/*    async () => {*/}
                {/*        await props.assignRole(2, userForChanging["id"]);*/}
                {/*        setRole(2);*/}
                {/*        setButtonStatus(5);*/}
                {/*    }*/}
                {/*}/>*/}
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

    const fillPatientInfoView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
                <label>Имя: </label>
                <input id={"name"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"name-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Имя: {userInfo["name"]}
                </p>
                <label>Фамилия: </label>
                <input id={"surname"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"surname-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Фамилия: {userInfo["surname"]}
                </p>
                <label>Отчество: </label>
                <input id={"secondName"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"secondName-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Отчество: {userInfo["secondName"]}
                </p>
                <label>День рождения: </label>
                <InputBirthday/>
                <p id={"birthday-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    День рождения: {userInfo["birthDate"] ? userInfo["birthDate"].slice(0, 10) : null}
                </p>
                <label>Пол: </label>
                <InputGender/>
                <p id={"inputGender-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Пол: {userInfo["gender"]}
                </p>
                <label>Телефон: </label>
                <InputPhone id={"phone"}/>
                <p id={"inputPhone-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Телефон: {userInfo["numberPhone"]}
                </p>
                <label>Адрес: </label>
                <input id={"address"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                }/>
                <p id={"address-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Адрес: {userInfo["address"]}
                </p>
                <label>Организация: </label>
                <InputOrganization organizations={props.organizations}/>
                <p id={"inputOrganization-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Организация: {userInfo["organizationString"]}
                </p>
            </div>
        )
    }

    const fillTutorInfoView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
                <label>Имя: </label>
                <input id={"name"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"name-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Имя: {userInfo["name"]}
                </p>
                <label>Фамилия: </label>
                <input id={"surname"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"surname-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Фамилия: {userInfo["surname"]}
                </p>
                <label>Отчество: </label>
                <input id={"secondName"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required/>
                <p id={"secondName-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Отчество: {userInfo["secondName"]}
                </p>
                <label>Организация: </label>
                <InputOrganization organizations={props.organizations}/>
                <p id={"inputOrganization-before"} style={{margin: "5px 0", textDecoration: "underline"}}>
                    Организация: {userInfo["organizationString"]}
                </p>
            </div>
        )
    }

    const fillUser = () => {
        return role === 1 ? fillPatientInfoView() :
            role === 3 ? fillTutorInfoView() : null;
    }

    const searchUser = () => {
        let searchValue = document.getElementById("searchInput").value;
        props.setRegex(searchValue);
        props.refresh();
    }
    console.log(buttonStatus);
    return (
        <Back navPanel={<AdminNav context={props.context}/>}>
            <Modal
                show={show}
                backdrop={true}
                keyboard={true}
                onHide={clearAll}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{
                        buttonStatus === 0 || buttonStatus === 4 ? "Добавление пользователя" :
                            buttonStatus === 1 || buttonStatus === 2 ? "Изменить пароль" :
                                buttonStatus === 3 || buttonStatus === 5 ? "Изменение пользователя" : "???"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus === 0 ?
                            createUserView() : buttonStatus === 1 ?
                            createUserPasswordView() : buttonStatus === 2 ?
                                "Вы уверен что хотите изменить пароль у пользователя c Логиным:" + userForRemoving["login"] + "?" :
                                buttonStatus === 3 ? chooseChangeAction() :
                                    buttonStatus === 4 ? changingRoleView() :
                                        buttonStatus === 5 ? fillUser() : null
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
                        <ButtonB text={"Отмена"} onClick={clearAll}/>
                        {
                            buttonStatus === 0 ?
                                <ButtonB text={"Добавить"} onClick={addUser}/> :
                                buttonStatus === 2 ?
                                    <ButtonB text={"Изменить"} onClick={changeUserPassword}/> :
                                    buttonStatus === 5 ?
                                        <ButtonB text={"Добавить информацию"}
                                                 onClick={
                                                     () => role === 1 ?
                                                         addInfoForPatient() : addInfoForTutor()}
                                        /> : null
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
                    </Col>
                    <Col md={8}>
                        <SearchInput
                            id={"searchInput"}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") {
                                    searchUser()
                                }
                            }}
                            onBlur={searchUser}
                            onClick={searchUser}
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
                                    {createBasicViewUsers()}
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
                                {props.users.length === 10 ? <ButtonA width={300} text={"Следующая страница"} onClick={() => {
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