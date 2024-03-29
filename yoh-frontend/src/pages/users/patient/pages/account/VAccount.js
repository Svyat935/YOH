import React, {useState} from "react";
import {Back} from "../../../../../components/back/Back";
import Row from "react-bootstrap/Row";
import {Col, Container} from "react-bootstrap";
import profileStub from "../../../../../assets/profileStub.jpg";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {InfoBlockStatic} from "../../../../../components/infoBlockStatic/InfoBlockStatic";
import "./AccountInfo.css";
import {ProgressBar} from "../../../../../components/progressBar/ProgressBar";
import {PatientNav} from "../../../../../components/navigate/NavPanel/Patient/PatientNav";
import Modal from "react-bootstrap/Modal";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {InputPhone} from "../../../../../components/inputPhone/InputPhone";
import {InputBirthday} from "../../../../../components/inputBirthday/InputBirthday";
import {InputGender} from "../../../../../components/inputGender/InputGender";

export function VAccount(props) {
    // ChangePhoto - 0, ChangeInfo - 1;
    const [buttonStatus, setButtonStatus] = useState(0);
    const [show, setShow] = useState(false);
    const allGames = props.statistics["Done"] + props.statistics["Assigned"] + props.statistics["Failed"];
    const statDone = allGames !== 0 ? Math.round(props.statistics["Done"] / allGames * 100) : 0;
    const statAssigned = allGames !== 0 ? Math.round(props.statistics["Assigned"] / allGames * 100) : 0;
    const statStarted = allGames !== 0 ? Math.round(props.statistics["Started"] / allGames * 100) : 0;

    const changeImage = async () => {
        let fImage = document.getElementById("fileImage");

        // TODO: Validate
        if (fImage.files[0]){
            let formData = new FormData();
            formData.append("image", fImage.files[0]);
            let response = await props.changeImage(formData);
            props.refresh();
        }
        setShow(false);
    }

    const changeInfo = async () => {
        let fName = document.getElementById("name"),
            fSurname = document.getElementById("surname"),
            fSecondName = document.getElementById("secondName"),
            fAddress = document.getElementById("address"),
            fPhone = document.getElementById("phone"),
            fGender = document.getElementById("gender-input"),
            fBirthday = document.getElementById("input-birthday");

        let body = {};
        body["id"] = props.accountInfo.id;
        if (fName.value) body["name"] = fName.value;
        if (fSurname.value) body["surname"] = fSurname.value;
        if (fSecondName.value) body["secondName"] = fSecondName.value;
        if (fPhone.value) body["numberPhone"] = fPhone.value;
        if (fAddress.value) body["address"] = fAddress.value;
        if (fBirthday.value) body["birthDate"] = fBirthday.value;
        if (fGender.value) body["gender"] = fGender.value;

        let response = await props.changeInfo(body);

        if (response){
            if (response.code === 401) {
                console.log(response);
            }else{
                props.refresh();
                setShow(false);
            }
        }
    }

    const changeView = () => {
        let userInfo = props.accountInfo;

        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
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
            </div>
        )
    }

    const createInfoView = () => {
        const missing = "отсутствует";
        const account = props.accountInfo ? props.accountInfo : {};

        let name = account["name"],
            surname = account["surname"],
            secondName = account["secondName"];

        let fio = "";
        if (surname) fio += surname + " ";
        if (name) fio += name + " ";
        if (secondName) fio += secondName;
        if (fio === ""){
            fio = "Отсутствует ФИО";
        }

        let phone = account["numberPhone"];
        phone = phone ? phone : missing;

        let email = account["email"];
        email = email ? email : missing;

        let gender = account["gender"];
        gender = gender ? gender : missing;

        let birthDate = account["birthDate"];
        birthDate = birthDate ? birthDate.slice(0, 10) : missing;

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
        let imageClass = new Image();
        if (props.accountInfo !== null && props.accountInfo["image"] !== null){
            //TODO: Add site url.
            imageClass.src = "https://mobile.itkostroma.ru/images/"+props.accountInfo["image"];
        }

        return (
            <InfoBlockStatic>
                <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}}
                     src={imageClass.src ? imageClass.src : profileStub} alt={'game'}/>
            </InfoBlockStatic>
        )
    }

    return (
        <Back navPanel={<PatientNav context={props.context}/>}>
            <Modal
                show={show}
                backdrop={true}
                keyboard={true}
                onHide={() => setShow(false)}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{
                        buttonStatus === 0 ? "Изменение Фото" : "Изменение Данных Аккаунта"
                    }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {
                        buttonStatus === 0 ?
                        <div style={
                            {
                                display: "flex",
                                flexDirection: "column",
                                padding: "0 10px"
                            }
                        }>
                            <input id={"fileImage"} type={"file"} style={
                                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                            } required/>
                        </div> : changeView()
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
                            <ButtonB text={"Изменить"} onClick={
                                buttonStatus === 0 ? changeImage : changeInfo
                            }/>
                        }
                    </div>
                </Modal.Footer>
            </Modal>
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
                        <ButtonA text={"Сменить фото"} onClick={() => {
                            setButtonStatus(0);
                            setShow(true);
                        }}/>
                    </Col>
                    <Col md={8}>
                        {createInfoView()}
                        <div style={{marginTop: 20}}>
                            <ButtonA text={"Редактировать"} onClick={() => {
                                setButtonStatus(1);
                                setShow(true);
                            }}/>
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
                                <label>В ожидании: </label>
                                <ProgressBar percent={statAssigned}/>
                                <label>Завершенных: </label>
                                <ProgressBar percent={statDone}/>
                                <label>Начатые: </label>
                                <ProgressBar percent={statStarted}/>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}