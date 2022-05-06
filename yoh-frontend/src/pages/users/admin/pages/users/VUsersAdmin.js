import {Back} from "../../../../../components/back/Back";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import React from "react";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";
import {SearchInput} from "../../../../../components/searchInput/SearchInput";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import {Button} from "../../../../../components/buttons/button";
import {Modal} from "../../../../../components/modal/Modal";

export function VUsersAdmin() {
    const filterList = [
        {
            "text": "По алфавиту",
            "value": 1
        },
        {
            "text": "По дате",
            "value": 2
        },
        {
            "text": "Пилигрим",
            "value": 3
        },
        {
            "text": "Тьютор",
            "value": 4
        },
        {
            "text": "Пациент",
            "value": 5
        },
        {
            "text": "Исследователь",
            "value": 6
        },
    ]

    return (
        <Back navPanel={<AdminNav/>}>
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
                        <Button width={300} text={"Добавить +"}/>
                        <Button width={300} text={"Удалить -"}/>
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
                                    <Modal/>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                    <InfoBlock text={"test"}>
                                        <div>
                                            <img style={{width: "100%"}} src={profileStub} alt={"profileStub"}/>
                                        </div>
                                    </InfoBlock>
                                </Col>
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}