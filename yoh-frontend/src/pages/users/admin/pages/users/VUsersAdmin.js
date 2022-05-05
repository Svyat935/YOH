import {Back} from "../../../../../components/back/Back";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import React from "react";
import {FilterBlock} from "../../../../../components/filterBlock/FilterBlock";

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
                <Col>
                    <Row>
                        <h2>Список пользователей</h2>
                        <FilterBlock filters={filterList}/>
                    </Row>
                </Col>
            </Container>
        </Back>
    )
}