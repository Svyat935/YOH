import {AdminNav} from "../../components/navigate/NavPanel/Admin/AdminNav";
import Modal from "react-bootstrap/Modal";
import {ButtonB} from "../../components/buttons/ButtonB/ButtonB";
import {Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {SortBlock} from "../../components/sortBlock/SortBlock";
import {ButtonA} from "../../components/buttons/ButtonA/ButtonA";
import {SearchInput} from "../../components/searchInput/SearchInput";
import {Back} from "../../components/back/Back";
import React from "react";
import {SwitchInput} from "../../components/switch/SwitchInput";

export function SearchFrame(props) {
    return (
        <Back navPanel={props.navPanel}>
            <Modal
                show={props.modalShow}
                backdrop={true}
                keyboard={true}
                onHide={props.modalOnHide}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{props.modalTitle}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {props.modalBody}
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
                        {props.modalFooter}
                    </div>
                </Modal.Footer>
            </Modal>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{marginBottom: 20}}>{props.title}</h1>
                    <Col md={4} style={
                        {
                            display: "flex",
                            justifyContent: "flex-start",
                            alignItems: "center",
                            flexDirection: "column"
                        }
                    }>
                        {props.filterList ? <SortBlock sorts={props.filterList}/> : null}
                        {
                            props.switch ?
                            <div style={
                                {
                                    display: "flex",
                                    justifyContent: "space-evenly",
                                    alignItems: "center",
                                    width: 300
                                }
                            }>
                                <span>{props.switch.title}</span>
                                <SwitchInput options={props.switch}/>
                            </div> : null
                        }
                        {props.buttons}
                    </Col>
                    <Col md={8}>
                        <SearchInput
                            id={"searchInput"}
                            onKeyDown={(e) => {if (e.key === "Enter") {props.searchInputOnKeyDown()}}}
                            onBlur={props.searchInputOnBlur}
                            onClick={props.searchInputOnClick}
                        />
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
                                    {props.content}
                                </Col>
                            </Row>
                            <Row style={
                                {
                                    display: "flex",
                                    flexDirection: "row",
                                    justifyContent: "space-around",
                                    marginTop: 20
                                }
                            }>
                                {props.contentFooter}
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        </Back>
    )
}
