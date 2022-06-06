import {VDelete} from "./VDelete";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VComponents} from "../../../admin/pages/components/VComponents";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CDelete() {
    const context = useContext(UserContext);
    const [userId, setUserId] = useState(null);
    const [games, setGames] = useState([]);
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

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

    const requestGetGamesForUser = async (patientId) => {
        return await fetch("/tutor/patients/getting/one/games?" +
            "patientID=" + encodeURIComponent(patientId) + "&" +
            "limit=100" + "&" +
            "start=0", {
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

    useEffect(async () => {
        if (context.token){
            setUserId(context.info.patient["id"]);
            let gamesResponse = await requestGetGamesForUser(context.info.patient["id"]);
            gamesResponse = gamesResponse["results"];
            if (gamesResponse) setGames(gamesResponse);
            else setGames([]);

            if (load === true) setLoad(false);
        }
    }, [context, _])


    return (
        <LoadPage status={load}>
            <VDelete
                context={context}
                games={games}
                deleteGame={requestDeleteGameForPatient}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}