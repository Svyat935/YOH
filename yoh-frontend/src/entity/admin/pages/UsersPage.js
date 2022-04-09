import React from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";

function ViewUserPage(props){
    const createUsersView = (users) => {
        users.forEach((user) => {
            console.log(user);
        })
    }

    return (
        <Container>
            <Row>

            </Row>
        </Container>
    );
}

export function UsersPage(props) {
    const token = props.token;

    const requestUsers = async () => {
        return await fetch("/admins/users", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "token": token
            },
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        });
    }

    const getUsers = async () => {
        let response = await requestUsers();
        console.log(response);
    }

    getUsers();
    return (
        <div>
            <h1>UsersPage</h1>
            <ViewUserPage/>
        </div>
    );
}