import {VHomePatient} from "./VHomePatient";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {VUsersAdmin} from "../../../admin/pages/users/VUsersAdmin";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CHomePatient() {
    const context = useContext(UserContext);
    const [games, setGames] = useState([]);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [account, setAccount] = useState(null);
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

    const requestAccountInfo = async () => {
        return await fetch("/patient/account", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        });
    }

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

            let responseAccount = await requestAccountInfo();
            responseAccount = responseAccount['jsonObject'];
            setAccount(responseAccount);

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VHomePatient
                context={context ? context : {}}
                games={games}
                setRegex={setRegex}
                setStart={setStart}
                start={start}
                account={account}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}