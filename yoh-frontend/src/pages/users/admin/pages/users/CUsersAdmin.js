import React, {useContext, useEffect, useState} from "react";
import {VUsersAdmin} from "./VUsersAdmin";
import {UserContext} from "../../../../../context/userContext";

export function CUsersAdmin() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);
    const [_, rerun] = useState(new class{});

    const requestUsers = async () => {
        return await fetch("/admins/users/all", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    const requestCreateUser = async (login, email, password) => {
        return await fetch("/users/registration", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                login: login,
                email: email,
                password: password,
            })
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    const requestRemoveUser = async () => {
        return null
    }

    useEffect(async () => {
        if (context.token){
            let responseUsers = await requestUsers();

            if (responseUsers !== null){
                responseUsers = responseUsers["jsonObject"]["userList"];
                setUsers(responseUsers);
            }
        }
    }, [context, _])

    return <VUsersAdmin users={users} createUser={requestCreateUser} refresh={() => rerun(new class{})}/>
}