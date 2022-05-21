import {VAccount} from "./VAccount";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CAccount() {
    const context = useContext(UserContext);
    const [accountInfo, setAccountInfo] = useState(null);
    const [statistics, setStatistics] = useState({});
    const [_, rerun] = useState(new class{});

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

    const requestChangeAccountInfo = async (body) => {
        return await fetch("/patient/account/changing", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        });
    }

    const requestChangeAccountImage = async (formData) => {
        return await fetch("/patient/account/image/add", {
            method: 'POST',
            headers: {"token": context.token},
            body: formData
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    const requestStatistics = async () => {
        return await fetch("/patient/games/status/statistic", {
            method: 'GET',
            headers: {"token": context.token},
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
           let accountInfo = await requestAccountInfo();
           let statistics = await requestStatistics();

           if (accountInfo !== null){
               accountInfo = accountInfo["jsonObject"];
               setAccountInfo(accountInfo);
           }
           if (statistics !== null){
               statistics = statistics["jsonObject"];
               setStatistics(statistics);
           }
        }
    }, [context, _])

    return <VAccount
        context={context}
        accountInfo={accountInfo}
        changeImage={requestChangeAccountImage}
        changeInfo={requestChangeAccountInfo}
        statistics={statistics}
        refresh={() => rerun(new class{})}
    />
}