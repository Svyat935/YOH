import React, {useContext, useEffect, useState} from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import {UserContext} from "../../../authentication/userContext";
import {ViewRegPage} from "../../../basicEntity/Reg/RegPage";
import {Link} from "react-router-dom";

function ViewUserPage(props){
    const changeRole = (role, user_id) => {
        props.requestChangeRole(role, user_id);
    }

    const createUsersView = (users) => {
        let view = [];
        users["userList"].forEach((user) => {
            view.push(
                <Row key={user.id} style={{"background": "wheat", "mapping": "10px"}}>
                    <p>id: {user.id}; login: {user.login}; email: {user.login}; role: {user.role}</p>
                        <p>
                            <button onClick={() => {changeRole(3, user.id)}}>To Tutor</button>
                            <button onClick={() => {changeRole(2, user.id)}}>To Researcher</button>
                            <button onClick={() => {changeRole(1, user.id)}}>To Patient</button>
                        </p>
                </Row>
            )
        })
        return view;
    }

    return (
        <Container style={{"margin": "10px"}}>
            <p>Users:</p>
            {createUsersView(props.users)}
        </Container>
    );
}

export function UsersPage(props) {
    const context = useContext(UserContext);
    const [viewUsers, setViewUsers] = useState(null);

    const requestUsers = async () => {
        return await fetch("/admins/users/all", {
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

    const requestCreateUser = async (login, email, password) => {
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
                setViewUsers(null);
            }
        });
    }

    const requestAssignRole = async (role, user_id) => {
        return await fetch("/admins/assign/role", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({
                role: role,
                user: user_id
            })
        }).then((response) => {
            if (response.status === 200) {
                setViewUsers(null);
            }
        });
    }

    useEffect(async () => {
        if (context.token !== null){
            let response = await requestUsers(),
                users = response["jsonObject"];
            setViewUsers(<ViewUserPage users={users} requestChangeRole={requestAssignRole}/>);
        }
    }, [context, viewUsers])

    return (
        <div>
            <h1>UsersPage</h1>
            <Link to={"/user/admin/"}>
                <button>To Back</button>
            </Link>
            <ViewRegPage reg={requestCreateUser}/>
            {viewUsers}
        </div>
    );
}