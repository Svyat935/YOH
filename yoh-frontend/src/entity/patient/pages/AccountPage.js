import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../authentication/userContext";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import {Link} from "react-router-dom";

function AccountPageView(props) {
    return (
        <Container style={{"background": "wheat"}}>
            <Row>Name: {props.name}</Row>
            <Row>Surname: {props.surname}</Row>
            <Row>SecondName: {props.secondName}</Row>
            <Row>Gender: {props.gender}</Row>
            <Row>Organization: {props.organization}</Row>
            <Row>BirthDate: {props.birthDate}</Row>
            <Row>NumberPhone: {props.numberPhone}</Row>
            <Row>Address: {props.address}</Row>
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
                return response.json();
            } else {
                return null;
            }
        });
    }

    useEffect(async () => {
        if (context.token !== null){
            let response = await requestAccount(),
                account = response["jsonObject"];
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
                />
            )
        }
    }, [context])

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