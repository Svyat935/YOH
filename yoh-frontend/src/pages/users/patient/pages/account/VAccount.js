import React from "react";
import {TutorNav} from "../../../../../components/navigate/Patient/TutorNav";
import {Back} from "../../../../../components/back/Back";
import Row from "react-bootstrap/Row";
import {Col, Container} from "react-bootstrap";
import profileStub from "../../../../../assets/profileStub.jpg";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {InfoBlockStatic} from "../../../../../components/infoBlockStatic/InfoBlockStatic";
import "./AccountInfo.css";
import {ProgressBar} from "../../../../../components/progressBar/ProgressBar";

export function VAccount(props) {
    const createInfoView = () => {
        const missing = "отсутствует";
        const account = props.accountInfo ? props.accountInfo : {};

        let name = account["name"],
            surname = account["surname"],
            secondName = account["secondName"];

        let fio = "";
        if (!surname) fio += surname + " ";
        if (!name) fio += name + " ";
        if (!secondName) fio += secondName;
        if (fio !== ""){
            fio = "Отсутствует ФИО";
        }

        let phone = account["numberPhone"];
        phone = phone ? phone : missing;

        let email = account["email"];
        email = email ? email : missing;

        let gender = account["gender"];
        gender = gender ? gender : missing;

        let birthDate = account["birthDate"];
        birthDate = birthDate ? birthDate : missing;

        let address = account["address"];
        address = address ? address : missing;

        return (
            <div className={"account-info"}>
                <h2>{fio}</h2>
                <p>Телефон: {phone}</p>
                <p>Электронная почта: {email}</p>
                <p>Адрес: {address}</p>
                <p>Пол: {gender}</p>
                <p>Дата Рождения: {birthDate}</p>
            </div>
        )
    }

    const createImageAccount = () => {
        //TODO Adding
        return (
            <InfoBlockStatic>
                <div>
                    <img style={{width: "100%"}} src={profileStub} alt={'game'}/>
                </div>
            </InfoBlockStatic>
        )
    }

    return (
        <Back navPanel={<TutorNav/>}>
            <Container>
                <Row>
                    <Col md={4} style={
                        {
                            display: "flex",
                            justifyContent: "flex-start",
                            alignItems: "center",
                            flexDirection: "column"
                        }
                    }>
                        {createImageAccount()}
                        <ButtonA text={"Сменить фото"}/>
                    </Col>
                    <Col md={8}>
                        {createInfoView()}
                        <div style={{marginTop: 20}}>
                            <ButtonA text={"Редактировать"}/>
                        </div>
                        <div style={
                            {
                                marginTop: 30,
                                marginBottom: 20,
                                background: "#F5F5F5",
                                boxShadow: "-10px 10px 30px rgba(0, 0, 0, 0.32)",
                                borderRadius: 40,
                            }
                        }>
                            <h3 style={{margin: "0px 50px"}}>Отслеживание успехов</h3>
                            <div style={
                                {
                                    padding: "10px 20px 30px 20px"
                                }
                            }>
                                <label>Успешных: </label>
                                <ProgressBar percent={30}/>
                                <label>В ожидании: </label>
                                <ProgressBar percent={70}/>
                                <label>Неудачных: </label>
                                <ProgressBar percent={10}/>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}