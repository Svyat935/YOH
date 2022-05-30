import React, {useContext, useEffect, useState} from "react";
import {VAllPatients} from "./VAllPatients";
import {UserContext} from "../../../../../context/userContext";
import {VUsersAdmin} from "../../../admin/pages/users/VUsersAdmin";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CAllPatients() {
    const context = useContext(UserContext);
    const [patients, setPatients] = useState([]);
    const [attachedPatients, setAttachedPatients] = useState([]);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [order, setOrder] = useState(1);
    const [_, rerun] = useState(new class {});
    const [load, setLoad] = useState(true);

    const requestPatients = async () => {
        return await fetch("/tutor/patients/getting/all?" +
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

    const requestAttachPatient = async (id) => {
        return await fetch("/tutor/patients/attaching", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({patient: id})
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    const requestDetachPatient = async (id) => {
        return await fetch("/tutor/patients/detaching", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({patient: id})
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token) {
            let responsePatients = await requestPatients();
            let responseAttachedPatients = await requestAttachedPatients(0, 100);

            if (responsePatients !== null) {
                responsePatients = responsePatients["jsonObject"]["results"];
                if (responsePatients !== undefined) setPatients(responsePatients);
            }

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
            <VAllPatients
                context={context}
                patients={patients}
                attachedPatients={attachedPatients}
                setRegex={setRegex}
                setStart={setStart}
                setOrder={setOrder}
                start={start}
                attach={requestAttachPatient}
                detach={requestDetachPatient}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}