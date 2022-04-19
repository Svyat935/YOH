import "./AuthPage.css"
import React, {useContext} from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Link} from "react-router-dom";
import {UserContext} from "../../authentication/userContext";
import {useNavigate} from "react-router-dom";

export function ViewAuthPage(props) {

    const authorize = () => {
        let credentials = document.getElementById("credentials").value,
            password = document.getElementById("password").value;
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
            <Row>
                <Col>
                    <Container>
                        <Row>
                            <Col/>
                            <Col style={{"background": "BurlyWood", "margin": "10px", "textAlign": "center"}}>
                                <Link to={"/reg/"}>
                                    <button>Registration</button>
                                </Link>
                            </Col>
                            <Col/>
                        </Row>
                    </Container>
                </Col>
            </Row>
        </Container>
    )
}

export function AuthPage() {
    const userContext = useContext(UserContext);
    const routing = useNavigate();

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
        let data = response["jsonObject"],
            role = data["role"],
            token = data["token"];

        if (token == null){
            alert("Sorry, invalid password and login.");
        }else if (role === null) {
            alert("Role is null. Sorry, but I can't let you in.");
        } else {
            userContext.login(token, role);

            switch (role) {
                case 0:
                    routing("/user/admin/");
                    break;
                case 1:
                    routing("/user/patient/");
                    break;
                case 2:
                    routing("/user/researcher/");
                    break;
                case 3:
                    routing("/user/tutor");
                    break;
                default:
                    alert("Role is undefined. Sorry, but I can't let you in.")
            }
        }
    }

    return <ViewAuthPage auth={authenticationUser}/>;
}