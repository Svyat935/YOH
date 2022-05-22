import {VDetailInfo} from "./VDetailInfo";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CDetailInfo() {
    const context = useContext(UserContext);
    const [user, setUser] = useState({});
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

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

    useEffect(async () => {
        if (context.token){
            let responseUser = await requestGetInfoForUser(context.info.patient["id"]);

            if (responseUser !== null) {
                responseUser = responseUser["jsonObject"];
                setUser(responseUser);
            }

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VDetailInfo
                context={context}
                user={user}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )

}