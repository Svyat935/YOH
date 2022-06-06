import {VDetailInfo} from "./VDetailInfo";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CDetailInfo() {
    const context = useContext(UserContext);
    const [user, setUser] = useState({});
    const [games, setGames] = useState([]);
    const [status, setStatus] = useState(null);
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

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

    const requestGetInfoForUser = async (patientId) => {
        return await fetch("/tutor/patients/getting/one?" +
            "patientID=" + encodeURIComponent(patientId), {
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

    const requestGetStatisticsForUser = async (patientId) => {
        return await fetch("/tutor/patients/getStatusStatistic?" +
            "patientID=" + encodeURIComponent(patientId), {
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

    const requestDetachPatient = async (id) => {
        return await fetch("/tutor/patients/detaching", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({patient: id})
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    const requestDeleteGameForPatient = async (game_id, user_id) => {
        return await fetch("/tutor/patients/games/removing", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({game_id: game_id, patient_id: user_id})
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
            let responseUser = await requestGetInfoForUser(context.info.patient["id"]);

            if (responseUser !== null) {
                responseUser = responseUser;
                setUser(responseUser);
            }

            let responseGames = await requestGetGamesForUser(context.info.patient["id"]);

            if (responseGames !== null){
                responseGames = responseGames["results"];
                setGames(responseGames);
            }

            let responseStatus = await requestGetStatisticsForUser(context.info.patient["id"]);

            if (responseStatus !== null){
                setStatus(responseStatus);
            }

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VDetailInfo
                context={context}
                user={user}
                games={games}
                status={status}
                detach={requestDetachPatient}
                deleteGame={requestDeleteGameForPatient}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )

}