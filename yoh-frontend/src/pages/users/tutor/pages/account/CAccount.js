import {VAccount} from "./VAccount";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CAccount() {
    const context = useContext(UserContext);
    const [accountInfo, setAccountInfo] = useState(null);
    const [_, rerun] = useState(new class{});
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

    const requestChangeAccountInfo = async (body) => {
        return await fetch("/tutor/account/changing", {
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
        return await fetch("/tutor/account/image/add", {
            method: 'POST',
            headers: {"token": context.token},
            body: formData
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
           let accountInfo = await requestAccountInfo();

           if (accountInfo !== null){
               setAccountInfo(accountInfo);
           }

           if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VAccount
                accountInfo={accountInfo}
                changeImage={requestChangeAccountImage}
                changeInfo={requestChangeAccountInfo}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}