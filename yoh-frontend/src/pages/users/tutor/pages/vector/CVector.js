import {VVector} from "./VVector";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VComponents} from "../../../admin/pages/components/VComponents";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CVector() {
    const context = useContext(UserContext);
    const [userId, setUserId] = useState(null);
    const [games, setGames] = useState([]);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

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

    const requestGetGames = async (patient_id) => {
        return await fetch("/games/all?" +
            "regex=" + encodeURIComponent(regex) + "&" +
            "start=" + encodeURIComponent(start) + "&" +
            "limit=" + encodeURIComponent(limit) + "&" +
            "patientID" + encodeURIComponent(patient_id), {
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

            if (load === true) setLoad(false);
        }
    }, [context, _])


    return (
        <LoadPage status={load}>
            <VVector
                context={context}
                games={games}
                setRegex={setRegex}
                setStart={setStart}
                start={start}
                addGame={requestAddingGameForPatient}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}