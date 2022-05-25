import {VHomeTutor} from "./VHomeTutor";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CHomeTutor() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);
    const [status, setStatus] = useState([]);
    const [account, setAccount] = useState(null);
    const [load, setLoad] = useState(true);

    const requestAccountInfo = async () => {
        return await fetch("/tutor/account", {
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

    const requestAttachingUser = async () => {
        return await fetch("/tutor/patients/getting?" +
            "start=" + encodeURIComponent(0) + "&" +
            "limit=" + encodeURIComponent(5), {
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
        if (context.token !== null){
            let response = await requestAttachingUser();

            let usersFirst;
            if (response !== null){
                usersFirst = response["jsonObject"]["results"];

                let stat = [];
                for (let user of usersFirst){
                    let statistics = await requestGetStatisticsForUser(user["id"]);
                    stat.push({statistics: statistics["jsonObject"], user: user});
                }
                setStatus(stat);
                setUsers(usersFirst);
            }

            let responseAccount = await requestAccountInfo();
            responseAccount = responseAccount['jsonObject'];
            setAccount(responseAccount);

            if (load === true) setLoad(false);
        }
    }, [context])

    return (
        <LoadPage status={load}>
            <VHomeTutor
                context={context}
                users={users}
                status={status}
                account={account}
                getStatus={requestGetStatisticsForUser}
            />
        </LoadPage>
    )
}