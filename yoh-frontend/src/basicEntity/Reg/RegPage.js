import "./RegPage.css"
import React from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Link} from "react-router-dom";

export function ViewRegPage(props) {

    const register = () => {
        let email = document.getElementById("email").value,
            login = document.getElementById("login").value,
            password = document.getElementById("password").value,
            confirm_password = document.getElementById("confirm_password").value;
        console.log(email, login, password, confirm_password);
        if (password === confirm_password){
            props.reg(login, email, password);
        }else{
            alert("Passwords don't match.")
        }
    }

    return (
        <Container>
            <Row>
                <Col/>
                <Col style={{"background": "wheat",}}>
                    <Container>
                        <Row style={{"background": "wheat", "fontWeight": "bold"}}>
                            <p style={{"textAlign": "center"}}>
                                Registration
                            </p>
                        </Row>
                        <Row style={{"textAlign": "center", "margin": "10px"}}>
                            <label htmlFor={"email"}>Gmail<br/>
                                <input type={"email"} placeholder={"email"} id={"email"}/>
                            </label>
                            <label htmlFor={"login"}>Login<br/>
                                <input type={"text"} placeholder={"login"} id={"login"}/>
                            </label>
                            <label htmlFor={"password"}>Password<br/>
                                <input type={"password"} placeholder={"password"} id={"password"}/>
                            </label>
                            <label htmlFor={"confirm_password"}>Confirm password<br/>
                                <input type={"password"} placeholder={"password"} id={"confirm_password"}/>
                            </label>
                        </Row>
                        <Row style={{"textAlign": "center", "margin": "10px"}}>
                            <button onClick={() => register()}>Submit</button>
                        </Row>
                    </Container>
                </Col>
                <Col/>
            </Row>
        </Container>
    )
}

export function RegPage() {

    const register = async (login, email, password) => {
        return await fetch("/users/registration", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                login: login,
                email: email,
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

    const registerUser = async (login, email, password) => {
        let response = await register(login, email, password);
        console.log(response);
    }

    return (
        <div>
            <ViewRegPage reg={registerUser}/>
            <Container>
                <Row>
                    <Col/>
                    <Col style={{"background": "BurlyWood", "margin": "10px", "textAlign": "center"}}>
                        <Link to={"/auth/"}><button>Authentication</button></Link>
                    </Col>
                    <Col/>
                </Row>
            </Container>
        </div>
    )
}