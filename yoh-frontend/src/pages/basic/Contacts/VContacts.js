import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import React, {useState} from "react";
import {Back} from "../../../components/back/Back";
import {BasicNav} from "../../../components/navigate/Basic/BasicNav";

export function VContacts() {

    return (
        <Back navLeft={<BasicNav/>}>
            <Container>
                <Row>
                    <h2 style={{marginTop: 20}}>За вопросами обращаться:</h2>
                    <Col style={{fontSize: "large", height: "68.6vh"}}>
                        <p>1. FrontEnd: Святослав Котяхов</p>
                        <p>2. Mobile: Максим Лютый</p>
                        <p>3. Backend: Егор Коренухин</p>
                        <p>4. Wrapper: Никита Лукьянов</p>
                        <p>5. Игра 1: Максим Расторгуев</p>
                        <p>6. Игра 2: Роман Зеленский</p>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}