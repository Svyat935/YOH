import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {Back} from "../../../components/back/Back";
import {BasicNav} from "../../../components/navigate/Basic/BasicNav";
import "./Auth.css";
import {PopUp} from "../../../components/popup/PopUp";

export function VAuth(props) {
    const [textPopUp, setTextPopUp] = useState("");
    const router = useNavigate();

    const popUpMessage = (text) => {
        setTextPopUp(text);
        throwPopUp();
        setTimeout(takeAwayPopUp, 2500);
    }

    const throwPopUp = () => {
        let element = document.querySelector(".popup");
        element.style.right = 0;
    }

    const takeAwayPopUp = () => {
        let element = document.querySelector(".popup");
        element.style.right = "-25%";
    }

    const authorize = async () => {
        let credentials = document.getElementById("credentials").value,
            password = document.getElementById("password").value;

        if (!(credentials && password)){
            popUpMessage("Пожалуйста заполните все поля.");
            return null;
        }

        let role = await props.auth(credentials, password);
        if (role === null){
            popUpMessage("Пользователь с указанным логиным и паролем отсутствует.");
        }else if (role === undefined){
            popUpMessage("Извините. Произошла ошибка на сервере.");
        } else if (role === 4){
            popUpMessage("Ваш пользователь не имеет роли. Пожалуйста обратитесь к администратору.");
        } else {
            switch (role) {
                case 0:
                    router("/user/admin/");
                    break;
                case 1:
                    router("/user/patient/");
                    break;
                case 2:
                    router("/user/researcher/");
                    break;
                case 3:
                    router("/user/tutor");
                    break;
            }
        }
    }


    return (
        <Back nav={<BasicNav/>}>
            <PopUp text={textPopUp}/>
            <Container>
                <Row>
                    <Col
                        style={{height: "78vh"}}
                        className={"d-flex align-items-center justify-content-center"}
                    >
                        <div className={"authBlock"}>
                            <div className={"authBlockInput"}>
                                <input
                                    id={"credentials"}
                                    className={"authInput"}
                                    type={"text"}
                                    placeholder={"Электронная почта / Логин"}
                                    required={true}
                                    onKeyDown={(e) => {
                                        if (e.key === "Enter"){
                                            authorize();
                                        }
                                    }}
                                />
                                <input
                                    id={"password"}
                                    className={"authInput"}
                                    type={"password"}
                                    placeholder={"Пароль"}
                                    required={true}
                                    onKeyDown={(e) => {
                                        if (e.key === "Enter"){
                                            authorize();
                                        }
                                    }}
                                />
                            </div>
                            <button className={"authButton"} onClick={authorize}>Войти</button>
                        </div>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}