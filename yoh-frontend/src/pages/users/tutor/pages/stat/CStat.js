import {VStat} from "./VStat";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CStat() {
    const context = useContext(UserContext);
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

    const requestGetAttemptsGames = async (game_id) => {
        return await fetch("https://mobile.itkostroma.ru/api/statistic_pagination?" +
            "gp_id=" + encodeURIComponent(game_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Access-Control-Allow-Origin": "*",
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
            let response = await requestGetAttemptsGames(context.info.gamePatientID);
            console.log(response);

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VStat
                context={context}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}