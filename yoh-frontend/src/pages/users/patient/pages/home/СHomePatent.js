import {VHomePatient} from "./VHomePatient";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VUsersAdmin} from "../../../admin/pages/users/VUsersAdmin";

export function CHomePatient() {
    const context = useContext(UserContext);
    const [games, setGames] = useState([]);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [_, rerun] = useState(new class{});

    const requestGames = async () => {
        return await fetch("/patient/games/getting?" +
            "regex=" + encodeURIComponent(regex) + "&" +
            "start=" + encodeURIComponent(start) + "&" +
            "limit=" + encodeURIComponent(limit), {
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
                responseGames = responseGames["jsonObject"]["results"];
                setGames(responseGames);
            }
        }
    }, [context, _])

    return <VHomePatient
        context={context ? context : {}}
        games={games}
        setRegex={setRegex}
        setStart={setStart}
        start={start}
        refresh={() => rerun(new class{})}
    />
}