import React, {useContext} from "react";
import {UserContext} from "../../../context/userContext";
import {VAuth} from "./VAuth";

export function CAuth() {
    const userContext = useContext(UserContext);

    const requestAuth = async (credentials, password) => {
        return await fetch("/users/authorization", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                credentials: credentials,
                password: password,
            })
        }).then((response) => {
            if (response.status === 200) { return response.json(); }
            else if (response.status === 401) { return response.json(); }
            else { return null; }
        });
    }

    const authorize = async (credentials, password) => {
        let response = await requestAuth(credentials, password);

        let role = response["role"],
            token = response["token"];

        if (role === undefined){
            return null;
        } else {
            userContext.login(token, role);
            return role;
        }
    }

    return <VAuth auth={authorize}/>
}