import {VComponents} from "./VComponents";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CComponents() {
    const context = useContext(UserContext);
    const [games, setGames] = useState([]);
    const [_, rerun] = useState(new class{});

    const requestGames = async () => {
        return await fetch("/games/all", {
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
            let responseGames = await requestGames();

            console.log(responseGames["jsonObject"]["games"]);
            if (responseGames !== null){
                responseGames = responseGames["jsonObject"]["games"];
                setGames(responseGames);
            }
        }
    }, [context, _])

    return <VComponents
        games={games}
        refresh={() => rerun(new class{})}
    />
}