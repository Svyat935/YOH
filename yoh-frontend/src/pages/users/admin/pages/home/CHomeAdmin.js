import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VHomeAdmin} from "./VHomeAdmin";

export function CHomeAdmin() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);
    const [games, setGames] = useState([]);

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

    const requestGames = async () => {
        return await fetch("/games/all", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
            let responseUsers = await requestUsers();
            let responseGames = await requestGames();

            if (responseUsers !== null){
                responseUsers = responseUsers["jsonObject"]["userList"];
                setUsers(responseUsers);
            }

            if (responseGames !== null){
                responseGames = responseGames["jsonObject"]["games"];
                setGames(responseGames);
            }
        }
    }, [context])

    return <VHomeAdmin users={users} games={games}/>
}