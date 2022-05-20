import {VDelete} from "./VDelete";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VComponents} from "../../../admin/pages/components/VComponents";

export function CDelete() {
    const context = useContext(UserContext);
    const [userId, setUserId] = useState(null);
    const [games, setGames] = useState([]);
    const [_, rerun] = useState(new class{});

    const requestDeleteGameForPatient = async (game_id) => {
        return await fetch("/tutor/patients/games/removing", {
            method: 'DELETE',
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

    const requestAllInfo = async (patient_id) => {
        return await fetch("/tutor/patients/getting/one?" +
            "patientID=" + encodeURIComponent(patient_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
            setUserId(context.info.patient["id"]);
            let info = await requestAllInfo(userId);
            let gamesResponse = info["jsonObject"]["games"];
            if (gamesResponse) setGames(gamesResponse);
            else setGames([]);
        }
    }, [context, _])


    return <VDelete
        context={context}
        games={games}
        deleteGame={requestDeleteGameForPatient}
        refresh={() => rerun(new class{})}
    />
}