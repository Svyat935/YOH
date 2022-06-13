import {Back} from "../../../../../components/back/Back";
import {AdminNav} from "../../../../../components/navigate/NavPanel/Admin/AdminNav";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import React, {useEffect, useState} from "react";
import {SortBlock} from "../../../../../components/sortBlock/SortBlock";
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
import {SearchFrame} from "../../../../../frame/SearchFrame/SearchFrame";
import {Pagination} from "../../../../../components/pagination/Pagination";
import {BsSortAlphaUp, BsSortAlphaDown, BsSortNumericDown, BsSortNumericUp} from "react-icons/bs";

export function VUsersAdmin(props) {
    const filterList = [
        {
            "text": "По алфавиту", "icon": <BsSortAlphaDown size={"1.3em"}/>, "defaultChecked": true, "value": 1, "onClick": () => {
                props.setRole(-1);
                props.setOrder(1);
                props.refresh();
            }
        },
        {
            "text": "По алфавиту", "icon": <BsSortAlphaUp size={"1.3em"}/>, "value": -1, "onClick": () => {
                props.setRole(-1);
                props.setOrder(-1);
                props.refresh();
            }
        },
        {
            "text": "По дате регистрации", "icon": <BsSortNumericDown size={"1.3em"}/>, "value": 2, "onClick": () => {
                props.setRole(-1);
                props.setOrder(2);
                props.refresh();
            }
        },
        {
            "text": "По дате регистрации", "icon": <BsSortNumericUp size={"1.3em"}/>, "value": -2, "onClick": () => {
                props.setRole(-1);
                props.setOrder(-2);
                props.refresh();
            }
        },
        {
            "text": "Без роли", "value": 3, "onClick": () => {
                props.setStart(0);
                props.setRole(4);
                props.setOrder(1);
                props.refresh();
            }
        },
        {
            "text": "Тьютор", "value": 4, "onClick": () => {
                props.setStart(0);
                props.setRole(3);
                props.setOrder(1);
                props.refresh();
            }
        },
        {
            "text": "Наблюдаемый", "value": 5, "onClick": () => {
                props.setStart(0);
                props.setRole(1);
                props.setOrder(1);
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
    const [password, setPassword] = useState(null);
    const [userForChanging, setChangingUser] = useState({});
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
        setChangingUser({});
        setPassword(null);
    }

    const createBasicViewUsers = () => {
        let users = props.users.slice(0, 9),
            view = [];

        if (users.length > 0) {
            users.forEach((user) => {
                let role = user["role"] === 0 ? "Администратор" :
                    user["role"] === 1 ? "Наблюдаемый" :
                        user["role"] === 2 ? "Исследователь" :
                            user["role"] === 3 ? "Тьютор" : "Без Роли"
                let image = user["image"] ? "https://mobile.itkostroma.ru/images/" + user["image"] :
                    profileStub;
                view.push(
                    <InfoBlock onClick={
                        async () => {
                            let userInfo = await props.getInfoUser(user["id"]);
                            if (userInfo !== null) {
                                setUserInfo(userInfo);
                            }

                            setChangingUser(user);
                            setButtonStatus(3);
                            setShow(true);
                        }
                    } ikey={user["id"]} text={user["login"]} addText={role}>
                        <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}} src={image}
                             alt={'profile'}/>
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

    const createUserPasswordView = (
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

    const changeUserPassword = () => {
        let fPassword = document.getElementById("password"),
            fConfirmPassword = document.getElementById("confirmPassword");
        let vPassword = document.getElementById("password-validate");
        let validStatus = true;

        if (!fPassword.value.trim()) {
            fPassword.style.border = validStyle;
            validStatus = false;
        }
        if (!fConfirmPassword.value.trim()) {
            fConfirmPassword.style.border = validStyle;
            validStatus = false;
        }

        if (fPassword.value.includes(" ")) {
            fPassword.style.border = validStyle;
            vPassword.textContent = "В пароле не должны присутствовать пробелы.";
            vPassword.style.marginBottom = "20px";
            validStatus = false;
        } else if (fPassword.value.length < 6) {
            fPassword.style.border = validStyle;
            vPassword.textContent = "Пароль должен иметь минимум 6 символов.";
            vPassword.style.marginBottom = "20px";
            validStatus = false;
        }else if (fPassword.value !== fConfirmPassword.value) {
            fPassword.style.border = validStyle;
            fConfirmPassword.style.border = validStyle;
            vPassword.textContent = "Пароли не совпадают."
            vPassword.style.marginBottom = "20px";
            validStatus = false;
        }

        if (validStatus) {
            setPassword(fPassword.value);
            setButtonStatus(2);
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


        if (fLogin.value.trim().length < 6) {
            fLogin.style.border = validStyle;
            vLogin.textContent = "Логин должен минимум 6 символов"
            vLogin.style.marginBottom = "20px";
            validStatus = false;
        }
        if (fEmail.value && validateEmail(fEmail.value) === null) {
            fEmail.style.border = validStyle;
            vEmail.textContent = "Электронная почта имеет неправильный формат.";
            vEmail.style.marginBottom = "20px";
            validStatus = false;
        }
        if (fPassword.value.trim().length < 6) {
            fPassword.style.border = validStyle;
            fConfirmPassword.style.border = validStyle;
            vPassword.textContent = "Пароль должен иметь минимум 6 символов."
            vPassword.style.marginBottom = "20px";
            validStatus = false;
        } else if (fPassword.value !== fConfirmPassword.value) {
            fPassword.style.border = validStyle;
            fConfirmPassword.style.border = validStyle;
            vPassword.textContent = "Пароли не совпадают."
            vPassword.style.marginBottom = "20px";
            validStatus = false;
        }

        if (validStatus) {
            let response = await props.createUser(fLogin.value, fEmail.value, fPassword.value);
            if (response["message"] !== "User was created") {
                let message = response["message"];
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

        if (validStatus) {
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

        if (!fName.value.trim()) {
            fName.style.border = validStyle;
            validStatus = false;
        }
        if (!fSurname.value.trim()) {
            fSurname.style.border = validStyle;
            validStatus = false;
        }
        if (!fSecondName.value.trim()) {
            fSecondName.style.border = validStyle;
            validStatus = false;
        }

        if (validStatus) {
            let body = {};
            body["name"] = fName.value.trim();
            body["surname"] = fSurname.value.trim();
            body["secondName"] = fSecondName.value.trim();
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

        if (!fName.value.trim()) {
            fName.style.border = validStyle;
            validStatus = false;
        }
        if (!fSurname.value.trim()) {
            fSurname.style.border = validStyle;
            validStatus = false;
        }
        if (!fSecondName.value.trim()) {
            fSecondName.style.border = validStyle;
            validStatus = false;
        }
        if (fPhone.value !== "+7(___)___-__-__" && fPhone.value.includes("_")) {
            fPhone.style.border = validStyle;
            validStatus = false;
        }

        if (validStatus) {
            let body = {};
            body["name"] = fName.value.trim();
            body["surname"] = fSurname.value.trim();
            body["secondName"] = fSecondName.value.trim();
            body["id"] = user.id;
            if (fPhone.value && fPhone.value !== "+7(___)___-__-__") body["numberPhone"] = fPhone.value;
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

    const chooseChangeAction = (
        <div style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center"
        }}>
            <h3>Выберите действие: </h3>
            {
                userForChanging["role"] === 4 ? <ButtonB width={"70%"} text={"Изменить роль"} onClick={
                    () => {
                        setButtonStatus(4)
                    }
                }
                /> : null
            }
            {
                userForChanging["role"] !== 4 ? <ButtonB width={"70%"} text={"Изменить данные"} onClick={
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

    const changingRoleView = (
        <div style={
            {
                display: "flex",
                flexDirection: "column",
                alignItems: "center"
            }
        }>
            <h3>Выберите роль: </h3>
            <ButtonB text={"Наблюдаемый"} onClick={
                async () => {
                    let id = null;
                    if (currentUserLogin !== null) {
                        let users = await props.getUser(currentUserLogin);
                        users = users['results'];
                        id = users[0]["id"];
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
                        let users = await props.getUser(currentUserLogin);
                        users = users['results'];
                        id = users[0]["id"];
                    } else {
                        id = userForChanging["id"];
                    }

                    await props.assignRole(3, id);
                    props.refresh();
                    setRole(3);
                    setButtonStatus(5);
                }
            }/>
        </div>
    )


    const createUserView = (
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

    const fillPatientInfoView = (
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
            } required defaultValue={userInfo["name"] ? userInfo["name"] : null}/>
            <label>Фамилия: </label>
            <input id={"surname"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required defaultValue={userInfo["surname"] ? userInfo["surname"] : null}/>
            <label>Отчество: </label>
            <input id={"secondName"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required defaultValue={userInfo["secondName"] ? userInfo["secondName"] : null}/>
            <label>День рождения: </label>
            <InputBirthday defaultValue={userInfo["birthDate"] ? userInfo["birthDate"].slice(0, 10) : null}/>
            <label>Пол: </label>
            <InputGender defaultValue={userInfo["gender"]}/>
            <label>Телефон: </label>
            <InputPhone id={"phone"} defaultValue={userInfo["numberPhone"]}/>
            <label>Адрес: </label>
            <input id={"address"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } defaultValue={userInfo["address"] ? userInfo["address"] : null}/>
            <label>Организация: </label>
            <InputOrganization organizations={props.organizations} defaultValue={userInfo["organization"]}/>
        </div>
    )

    const fillTutorInfoView = (
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
            } required defaultValue={userInfo["name"] ? userInfo["name"] : null}/>
            <label>Фамилия: </label>
            <input id={"surname"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required defaultValue={userInfo["surname"] ? userInfo["surname"] : null}/>
            <label>Отчество: </label>
            <input id={"secondName"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required defaultValue={userInfo["secondName"] ? userInfo["secondName"] : null}/>
            <label>Организация: </label>
            <InputOrganization defaultValue={userInfo["organizationString"]} organizations={props.organizations}/>
        </div>
    )

    const fillUser = () => {
        return role === 1 ? fillPatientInfoView :
            role === 3 ? fillTutorInfoView : null;
    }

    const searchUser = () => {
        let searchValue = document.getElementById("searchInput").value;
        props.setRegex(searchValue);
        props.refresh();
    }

    const buttons = (
        <ButtonA width={300} text={"Добавить +"} onClick={() => {
            setButtonStatus(0);
            setShow(true);
        }}/>
    )

    const paginationButtons = () => {
        // return <Pagination
        //     count={props.size}
        //     start={props.start}
        //     limit={props.limit}
        //     setStart={props.setStart}
        //     refresh={props.refresh}
        // />
        let view = [];
        if (props.start)
            view.push(
                <ButtonA width={300} text={"Предыдущая страница"} onClick={() => {
                    props.setStart(props.start - 9);
                    props.refresh();
                }}/>
            )

        if (props.users.length === 10) {
            view.push(
                <ButtonA width={300} text={"Следующая страница"} onClick={() => {
                    props.setStart(props.start + 9);
                    props.refresh();
                }}/>
            )
        }
        return view;
    }

    const modalBody = () => {
        if (buttonStatus === 0) {
            return createUserView;
        } else if (buttonStatus === 1) {
            return createUserPasswordView;
        } else if (buttonStatus === 2) {
            return <p>
                {
                    "Вы уверен что хотите изменить пароль " +
                    "у пользователя c Логиным: " + userForChanging["login"] + "?"
                }
            </p>
        } else if (buttonStatus === 3) {
            return chooseChangeAction;
        } else if (buttonStatus === 4) {
            return changingRoleView;
        } else if (buttonStatus === 5) {
            return fillUser();
        }
    }

    const modalTitle = () => {
        if (buttonStatus === 0) {
            return "Добавление пользователя";
        } else if (buttonStatus === 1 || buttonStatus === 2) {
            return "Изменить пароль";
        } else if (buttonStatus === 3) {
            return "Изменение пользователя";
        } else if (buttonStatus === 4) {
            return "Изменение роли";
        } else if (buttonStatus === 5) {
            return "Изменение данных";
        }
        return null;
    }

    const modalFooter = () => {
        let view = [<ButtonB text={"Отмена"} onClick={clearAll}/>];
        if (buttonStatus === 0) {
            view.push(<ButtonB text={"Добавить"} onClick={addUser}/>);
        } else if (buttonStatus === 1) {
            view.push(<ButtonB text={"Изменить"} onClick={changeUserPassword}/>)
        } else if (buttonStatus === 2) {
            view.push(<ButtonB text={"Изменить"} onClick={() => {
                props.changePassword(userForChanging["id"], password);
                setShow(false);
            }}/>);
        } else if (buttonStatus === 5) {
            view.push(<ButtonB text={"Изменить информацию"}
                               onClick={() => role === 1 ? addInfoForPatient() : addInfoForTutor()}/>)
        }

        return (
            <div style={{
                height: "25%",
                width: "100%",
                display: "flex",
                justifyContent: "space-between"
            }}>
                {view}
            </div>
        )
    }

    return <SearchFrame
        navPanel={<AdminNav context={props.context}/>}
        filterList={filterList}
        modalShow={show}
        modalOnHide={clearAll}
        modalTitle={modalTitle()}
        modalBody={modalBody()}
        modalFooter={modalFooter()}
        title={"Список пользователей"}
        buttons={buttons}
        searchInputOnKeyDown={searchUser}
        searchInputOnBlur={searchUser}
        searchInputOnClick={searchUser}
        content={createBasicViewUsers()}
        contentFooter={paginationButtons()}
    />
}