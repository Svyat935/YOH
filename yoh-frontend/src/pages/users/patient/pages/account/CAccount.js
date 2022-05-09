import {VAccount} from "./VAccount";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CAccount() {
    const context = useContext(UserContext);
    const [accountInfo, setAccountInfo] = useState(null);
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

    const requestAccountImage = async () => {
        return await fetch("?????????", {
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

    useEffect(async () => {
        if (context.token){
           let accountInfo = await requestAccountInfo();

           if (accountInfo !== null){
               accountInfo = accountInfo["jsonObject"];
               setAccountInfo(accountInfo);
           }
        }
    }, [context, _])

    return <VAccount
        accountInfo={accountInfo}
        refresh={() => rerun(new class{})}
    />
}