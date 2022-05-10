import {VAccount} from "./VAccount";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CAccount() {
    const context = useContext(UserContext);
    const [accountInfo, setAccountInfo] = useState(null);
    const [image, setImage] = useState(null);
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
        return await fetch("/patient/account/image", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
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

    useEffect(async () => {
        if (context.token){
           let accountInfo = await requestAccountInfo();
           let accountImage = await requestAccountImage();

           if (accountInfo !== null){
               accountInfo = accountInfo["jsonObject"];
               setAccountInfo(accountInfo);
           }
           if (accountImage !== null){
               if (accountImage["message"] === undefined){
                   setImage(accountImage["jsonObject"]["image"]);
               }
           }
        }
    }, [context, _])

    return <VAccount
        accountInfo={accountInfo}
        changeImage={requestChangeAccountImage}
        image={image}
        refresh={() => rerun(new class{})}
    />
}