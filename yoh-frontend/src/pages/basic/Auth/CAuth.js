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
            else {return null;}
        });
    }

    const authorize = async (credentials, password) => {
        let response = await requestAuth(credentials, password);

        if (response === null){
            return undefined;
        }

        if (response.code === 401){
            return null;
        }

        let role = response["jsonObject"]["role"],
            token = response["jsonObject"]["token"];

        if (role === null){
            return -1;
        } else {
            userContext.login(token, role);
            return role;
        }
    }

    return <VAuth auth={authorize}/>
}