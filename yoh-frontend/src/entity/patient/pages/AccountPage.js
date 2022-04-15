import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../authentication/userContext";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import {Link} from "react-router-dom";

function AccountPageView(props) {
    const changeField = (name_field) => {
        let value = document.getElementById(name_field).value;
        if (value !== ""){
            let body = {}
            body[name_field] = value;
            props.changeAccount(body);
        }
    }

    return (
        <Container style={{"background": "wheat"}}>
            <Row>
                <p>Name: {props.name} ;
                    <input type={"text"} id={"name"}/>
                    <button onClick={() => changeField("name")}>Change</button>
                </p>
            </Row>
            <Row>
                <p>Surname: {props.surname} ;
                    <input type={"text"} id={"surname"}/>
                    <button onClick={() => changeField("surname")}>Change</button>
                </p>
            </Row>
            <Row>
                <p>SecondName: {props.secondName} ;
                    <input type={"text"} id={"secondName"}/>
                    <button onClick={() => changeField("secondName")}>Change</button>
                </p>
            </Row>
            <Row>
                <p>Gender: {props.gender} ;
                    <select id={"gender"}>
                        <option defaultValue={true} value={"Мужской"}>Мужской</option>
                        <option value={"Женский"}>Женский</option>
                    </select>
                    <button onClick={() => changeField("gender")}>Change</button>
                </p>
            </Row>
            <Row>
                <p>Organization: {props.organization}</p>
            </Row>
            <Row>
                <p>BirthDate: {props.birthDate} ;
                    <input type={"text"} id={"birthDate"}/>
                    <button onClick={() => changeField("birthDate")}>Change</button>
                </p>
            </Row>
            <Row>
                <p>NumberPhone: {props.numberPhone} ;
                    <input type={"text"} id={"numberPhone"}/>
                    <button onClick={() => changeField("numberPhone")}>Change</button>
                </p>
            </Row>
            <Row>
                <p>Address: {props.address} ;
                    <input type={"text"} id={"address"}/>
                    <button onClick={() => changeField("address")}>Change</button>
                </p>
            </Row>
            {props.tutor !== undefined ?
                <Container style={{"background": "wheat"}}>
                    <Row>Tutor name: {props.tutor["name"]}</Row>
                    <Row>Tutor surname: {props.tutor["surname"]}</Row>
                    <Row>Tutor secondName: {props.tutor["secondName"]}</Row>
                    <Row>Tutor organization: {props.tutor["organization"]}</Row>
                </Container> : null
            }
        </Container>
    )
}

export function AccountPage() {
    const context = useContext(UserContext);
    const [view, setView] = useState(null);
    const [reset, executeReset] = useState(false);

    const requestAccount = async () => {
        return await fetch("/patient/account", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        });
    }

    const requestChangeAccount = async (body) => {
        return await fetch("/patient/account/changing", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 200) {
                if (reset === true){
                    executeReset(false);
                }else{
                    executeReset(true);
                }
            }
        });
    }

    useEffect(async () => {
        if (context.token !== null){
            let response = await requestAccount(),
                account = response["jsonObject"];
            console.log(response);
            setView(
                <AccountPageView
                    name={account.name}
                    surname={account.surname}
                    secondName={account.secondName}
                    gender={account.gender}
                    organization={account.organization}
                    birthDate={account.birthDate}
                    numberPhone={account.numberPhone}
                    address={account.address}
                    tutor={account.tutor}
                    changeAccount={requestChangeAccount}
                />
            )
        }
    }, [context, reset])

    return (
        <div>
            <h1>Account Page</h1>
            <Link to={"/user/patient/"}>
                <button>To Back</button>
            </Link>
            {view}
        </div>
    )
}