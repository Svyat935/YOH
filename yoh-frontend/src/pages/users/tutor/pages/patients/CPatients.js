import React, {useContext, useEffect, useState} from "react";
import {VPatients} from "./VPatients";
import {UserContext} from "../../../../../context/userContext";
import {VUsersAdmin} from "../../../admin/pages/users/VUsersAdmin";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CPatients() {
    const context = useContext(UserContext);
    const [attachedPatients, setAttachedPatients] = useState([]);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [order, setOrder] = useState(1);
    const [_, rerun] = useState(new class {});
    const [load, setLoad] = useState(true);

    const requestAttachedPatients = async () => {
        return await fetch("/tutor/patients/getting?" +
            "regex=" + encodeURIComponent(regex) + "&" +
            "start=" + encodeURIComponent(start) + "&" +
            "limit=" + encodeURIComponent(limit) + "&" +
            "order=" + encodeURIComponent(order), {
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

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VPatients
                context={context}
                setRegex={setRegex}
                setStart={setStart}
                start={start}
                setOrder={setOrder}
                saveUser={(info) => context.addInfo(info)}
                attachedPatients={attachedPatients}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}