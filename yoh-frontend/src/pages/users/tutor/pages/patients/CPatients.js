import React, {useContext, useEffect, useState} from "react";
import {VPatients} from "./VPatients";
import {UserContext} from "../../../../../context/userContext";

export function CPatients() {
    const context = useContext(UserContext);
    const [attachedPatients, setAttachedPatients] = useState([]);
    const [_, rerun] = useState(new class {});

    const requestAttachedPatients = async (start, limit) => {
        return await fetch("/tutor/patients/getting?" +
            "start=" + encodeURIComponent(start) + "&" +
            "limit=" + encodeURIComponent(limit), {
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
        if (context.token) {
            let responseAttachedPatients = await requestAttachedPatients(0, 100);

            if (responseAttachedPatients !== null) {
                responseAttachedPatients = responseAttachedPatients["jsonObject"]["results"];
                if (responseAttachedPatients !== undefined) setAttachedPatients(responseAttachedPatients);
                else setAttachedPatients([]);
            }
        }
    }, [context, _])

    return <VPatients
        context={context}
        saveUser={(info) => context.addInfo(info)}
        attachedPatients={attachedPatients}
        refresh={() => rerun(new class{})}
    />
}