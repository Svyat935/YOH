import React, {useState} from "react";
import {Back} from "../../../../../components/back/Back";
import Row from "react-bootstrap/Row";
import {Col, Container} from "react-bootstrap";
import profileStub from "../../../../../assets/profileStub.jpg";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {InfoBlockStatic} from "../../../../../components/infoBlockStatic/InfoBlockStatic";
import "./AccountInfo.css";
import {ProgressBar} from "../../../../../components/progressBar/ProgressBar";
import {PatientNav} from "../../../../../components/navigate/Patient/PatientNav";
import Modal from "react-bootstrap/Modal";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {InputPhone} from "../../../../../components/inputPhone/InputPhone";
import {InputBirthday} from "../../../../../components/inputBirthday/InputBirthday";
import {InputGender} from "../../../../../components/inputGender/InputGender";

export function VAccount(props) {
    // ChangePhoto - 0, ChangeInfo - 1;
    const [buttonStatus, setButtonStatus] = useState(0);
    const [show, setShow] = useState(false);

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

        let email = account["email"];
        email = email ? email : missing;

        let org = account["organizationString"];
        org = org ? org : missing;

        return (
            <div className={"account-info"}>
                <h2>{fio}</h2>
                <p>Электронная почта: {email}</p>
                <p>Организация: {org}</p>
            </div>
        )
    }

    const createImageAccount = () => {
        let imageClass = new Image();
        if (props.image !== null){
            let base64 = props.image;
            imageClass.src = 'data:image/jpg;base64,' + base64;
        }

        return (
            <InfoBlockStatic>
                <div>
                    <img style={{width: "100%"}} src={imageClass.src ? imageClass.src : profileStub} alt={'game'}/>
                </div>
            </InfoBlockStatic>
        )
    }

    return (
        <Back navPanel={<PatientNav/>}>
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
                        <div style={{height: 295}}/>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}