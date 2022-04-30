import React from "react";
import {Back} from "../../../components/back/Back";
import {BasicNavigate} from "../../../components/navigate/BasicNav";
import {Container, Row, Col} from "react-bootstrap";
import homeImages from "../../../assets/homeImages.jpg";

export function VHome() {
    return (
        <Back nav={<BasicNavigate/>}>
            <Container>
                <Row style={{height: "78vh"}} className={"align-items-center"}>
                    <Col>
                        <h2>Добро пожаловать в Систему Дистанционой Социальной Адаптации</h2>
                    </Col>
                    <Col className={"column"}>
                        <div style={{width: "100%"}}>
                            <img style={{width: "100%", height: "100%", }} src={homeImages}/>
                        </div>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}