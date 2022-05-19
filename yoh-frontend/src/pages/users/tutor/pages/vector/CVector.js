import {VVector} from "./VVector";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VComponents} from "../../../admin/pages/components/VComponents";

export function CVector() {
    const context = useContext(UserContext);
    const [userId, setUserId] = useState(null);
    const [games, setGames] = useState([]);
    const [_, rerun] = useState(new class{});

    const requestAddingGameForPatient = async (game_id) => {
        return await fetch("/tutor/patients/games/adding", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({game_id: game_id, patient_id: userId})
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    const requestGetGames = async () => {
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
            setUserId(context.info.patient["id"]);
            let responseGames = await requestGetGames();

            if (responseGames !== null) {
                responseGames = responseGames["jsonObject"]["results"];
                setGames(responseGames);
            }
        }
    }, [context, _])


    return <VVector
        context={context}
        games={games}
        addGame={requestAddingGameForPatient}
        refresh={() => rerun(new class{})}
    />
}