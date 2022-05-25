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

    useEffect(async () => {
        if (context.token){
            let responseUser = await requestGetInfoForUser(context.info.patient["id"]);

            if (responseUser !== null) {
                responseUser = responseUser["jsonObject"];
                setUser(responseUser);
            }

            let responseGames = await requestGetGamesForUser(context.info.patient["id"]);

            if (responseGames !== null){
                responseGames = responseGames["jsonObject"]["results"];
                setGames(responseGames);
            }

            let responseStatus = await requestGetStatisticsForUser(context.info.patient["id"]);

            if (responseStatus !== null){
                responseStatus = responseStatus['jsonObject'];
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
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )

}