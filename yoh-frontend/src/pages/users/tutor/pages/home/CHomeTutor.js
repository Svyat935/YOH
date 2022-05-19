import {VHomeTutor} from "./VHomeTutor";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CHomeTutor() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);

    const requestAttachingUser = async () => {
        return await fetch("/tutor/patients/getting", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token !== null){
            let response = await requestAttachingUser();

            if (response !== null){
                let users = response["jsonObject"]["patientList"];
                setUsers(users);
            }
        }
    }, [context])

    return <VHomeTutor users={users}/>
}