import {Col, Container, Row} from "react-bootstrap";
import React, {useContext} from "react";
import {ChatContainerSend} from "../../../../../components/chatContainer/chatContainerSend/ChatContainerSend";
import {Back} from "../../../../../components/back/Back";
import ImageStub from "../../../../../assets/news1.png";
import {ChatContainerReceive} from "../../../../../components/chatContainer/chatContainerReceive/ChatContainerReceive";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {TutorNav} from "../../../../../components/navigate/Tutor/TutorNav";
import {UserContext} from "../../../../../context/userContext";

export function VChat() {
    let context = useContext(UserContext);

    return (
        <Back navPanel={<TutorNav context={context}/>}>
            <Container style={{border: "1px solid #6A6DCD", borderRadius: 40, padding: 20}}>
                <Row>
                    <Col>
                        <ChatContainerSend text={"Привет"} date={"11:00"} image={ImageStub}/>
                        <ChatContainerReceive text={"Привет"} date={"11:01"} image={ImageStub}/>
                        <ChatContainerSend text={"Это просто для теста"} date={"11:00"} image={ImageStub}/>
                        <ChatContainerSend
                            text={"Большой нерабочий тест получился."}
                            date={"11:00"}
                            image={ImageStub}
                        />
                        <ChatContainerReceive
                            text={"Очень нерабочий тест получился."}
                            date={"11:01"}
                            image={ImageStub}
                        />
                    </Col>
                </Row>
            </Container>
            <Container>
                <div style={
                    {
                        padding: "20px 10px",
                        background: "#6A6DCD",
                        borderRadius: 40,
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        margin: "10px 40px"
                    }
                }>
                    <input type={"text"} style={
                        {
                            border: "none",
                            background: "#FFFFFF",
                            width: "90%",
                            height: 50,
                            borderRadius: 40,
                            padding: "10px 20px"
                        }
                    }/>
                    <ButtonB text={"Отправить"}/>
                </div>
            </Container>
        </Back>
    )
}