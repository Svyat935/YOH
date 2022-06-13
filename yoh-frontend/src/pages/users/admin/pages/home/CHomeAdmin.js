import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VHomeAdmin} from "./VHomeAdmin";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CHomeAdmin() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);
    const [games, setGames] = useState([]);
    const [load, setLoad] = useState(true);

    const requestUsers = async () => {
        return await fetch("/admins/users/all?" +
            "start=" + encodeURIComponent(0) + "&" +
            "limit=" + encodeURIComponent(5), {
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
                responseUsers = responseUsers["results"];
                if (responseUsers !== undefined) setUsers(responseUsers);
            }

            if (responseGames !== null){
                responseGames = responseGames["results"];
                if (responseGames !== undefined) setGames(responseGames);
            }

            if (load === true) setLoad(false);
        }
    }, [context])

    return (
        <LoadPage status={load}>
            <VHomeAdmin
                context={context}
                users={users}
                games={games}
            />
        </LoadPage>
    )
}