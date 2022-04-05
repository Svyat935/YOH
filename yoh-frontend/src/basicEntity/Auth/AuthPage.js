import "./AuthPage.css"
import React from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

export function ViewAuthPage(props) {

    const authorize = () => {
        let credentials = document.getElementById("credentials").value,
            password = document.getElementById("password").value;
        console.log(credentials, password);
        props.auth(credentials, password);
    }

    return (
        <Container>
            <Row>
                <Col/>
                <Col style={{"background": "wheat",}}>
                    <Container>
                        <Row style={{"background": "wheat", "fontWeight": "bold"}}>
                            <p style={{"textAlign": "center"}}>
                                Authentication
                            </p>
                        </Row>
                        <Row style={{"textAlign": "center", "margin": "10px"}}>
                            <label htmlFor={"credentials"}>Credentials<br/>
                                <input type={"text"} placeholder={"credentials"} id={"credentials"}/>
                            </label>
                            <label htmlFor={"password"}>Password<br/>
                                <input type={"password"} placeholder={"password"} id={"password"}/>
                            </label>
                        </Row>
                        <Row style={{"textAlign": "center", "margin": "10px"}}>
                            <button onClick={() => authorize()}>Submit</button>
                        </Row>
                    </Container>
                </Col>
                <Col/>
            </Row>
        </Container>
    )
}

export function AuthPage() {

    const authorize = async (credentials, password) => {
        return await fetch("/users/authorization", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                credentials: credentials,
                password: password,
            })
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        });
    }

    const authenticationUser = async (credentials, password) => {
        let response = await authorize(credentials, password);
        console.log(response);
    }

    return <ViewAuthPage auth={authenticationUser}/>;
}