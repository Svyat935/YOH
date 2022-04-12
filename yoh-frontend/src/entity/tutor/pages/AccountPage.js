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
            <Row>Organization: {props.organization}</Row>
        </Container>
    )
}

export function AccountPage() {
    const context = useContext(UserContext);
    const [view, setView] = useState(null);

    const requestAccount = async () => {
        return await fetch("/tutor/account", {
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

    useEffect(async () => {
        if (context.token !== null) {
            let response = await requestAccount(),
                account = response["jsonObject"];
            setView(
                <AccountPageView
                    name={account.name}
                    surname={account.surname}
                    secondName={account.secondName}
                    organization={account.organization}
                />
            )
        }
    }, [context])

    return (
        <div>
            <h1>Account Page</h1>
            <Link to={"/user/tutor/"}>
                <button>To Back</button>
            </Link>
            {view}
        </div>
    )
}