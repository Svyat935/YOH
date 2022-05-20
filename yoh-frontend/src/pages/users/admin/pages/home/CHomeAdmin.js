import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VHomeAdmin} from "./VHomeAdmin";

export function CHomeAdmin() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);
    const [games, setGames] = useState([]);

    const requestUsers = async () => {
        return await fetch("/admins/users/all?" +
            "start=" + encodeURIComponent(0) + "&" +
            "limit=" + encodeURIComponent(100), {
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

    const requestGames = async () => {
        return await fetch("/games/all?" +
            "start=" + encodeURIComponent(0) + "&" +
            "limit=" + encodeURIComponent(100), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
            let responseUsers = await requestUsers();
            let responseGames = await requestGames();

            if (responseUsers !== null){
                responseUsers = responseUsers["jsonObject"]["results"];
                if (responseUsers !== undefined) setUsers(responseUsers);
            }

            if (responseGames !== null){
                responseGames = responseGames["jsonObject"]["results"];
                if (responseGames !== undefined) setGames(responseGames);
            }
        }
    }, [context])

    return <VHomeAdmin
        context={context}
        users={users}
        games={games}
    />
}