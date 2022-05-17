import {VComponents} from "./VComponents";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CComponents() {
    const context = useContext(UserContext);
    const [games, setGames] = useState([]);
    const [_, rerun] = useState(new class{});

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

    const requestSendGames = async (formData) => {
        return await fetch("/admins/upload/games/", {
            method: 'POST',
            headers: {'token': context.token},
            body: formData
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    const requestRemoveGame = async (game_id) => {
        return await fetch("/games/removing/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({game_id: game_id})
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    const requestChangeGame = async (body) => {
        /*
        game_id: required
        name: Optional,
        description: Optional
        */
        return await fetch("/games/changing/", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
            let responseGames = await requestGames();

            if (responseGames !== null){
                responseGames = responseGames["jsonObject"]["games"];
                if (responseGames !== undefined) setGames(responseGames);
            }
        }
    }, [context, _])

    return <VComponents
        games={games}
        refresh={() => rerun(new class{})}
        sendGames={requestSendGames}
        removeGame={requestRemoveGame}
        changeGame={requestChangeGame}
        context={context}
    />
}