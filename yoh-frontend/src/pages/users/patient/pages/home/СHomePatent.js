import {VHomePatient} from "./VHomePatient";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CHomePatient() {
    const context = useContext(UserContext);
    const [games, setGames] = useState([]);
    const [_, rerun] = useState(new class{});

    const requestGames = async () => {
        return await fetch("/patient/games/getting", {
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
            let responseGames = await requestGames();

            if (responseGames !== null){
                responseGames = responseGames["jsonObject"]["gamesArray"];
                setGames(responseGames);
            }
        }
    }, [context, _])

    return <VHomePatient
        games={games}
        refresh={() => rerun(new class{})}
    />
}