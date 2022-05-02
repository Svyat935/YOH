import {Back} from "../../../../../components/back/Back";
import React from "react";
import {Container} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";

export function VHomeAdmin() {
    return (
        <Back navPanel={<AdminNav/>}>
            <Container style={{marginTop: 20}}>
                <h2>Добрый день, Администратор!</h2>
                <Col>
                    <Row>123</Row>
                </Col>
            </Container>
        </Back>
    )
}