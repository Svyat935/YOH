import {TutorNav} from "../../../../../components/navigate/Tutor/TutorNav";
import Modal from "react-bootstrap/Modal";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {Col, Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {Back} from "../../../../../components/back/Back";
import React from "react";

export function VDetailInfo() {
    return (
        <Back navPanel={<TutorNav/>}>
            <Modal
                // show={show}
                backdrop={true}
                keyboard={true}
                // onHide={() => setShow(false)}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{null}</Modal.Title>
                </Modal.Header>
                <Modal.Body>

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
                        {/*<ButtonB text={"Отмена"} onClick={() => setShow(false)}/>*/}

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
                    </Col>
                    <Col md={8}>
                        <div style={{marginTop: 20}}>

                        </div>
                        <div style={{height: 295}}/>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}