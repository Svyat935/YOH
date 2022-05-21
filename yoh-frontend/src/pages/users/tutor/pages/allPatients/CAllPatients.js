import React, {useContext, useEffect, useState} from "react";
import {VAllPatients} from "./VAllPatients";
import {UserContext} from "../../../../../context/userContext";

export function CAllPatients() {
    const context = useContext(UserContext);
    const [patients, setPatients] = useState([]);
    const [attachedPatients, setAttachedPatients] = useState([]);
    const [_, rerun] = useState(new class {});

    const requestPatients = async (start, limit) => {
        return await fetch("/tutor/patients/getting/all?" +
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
            let responsePatients = await requestPatients(0, 100);
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
        }
    }, [context, _])

    return <VAllPatients
        context={context}
        patients={patients}
        attachedPatients={attachedPatients}
        attach={requestAttachPatient}
        detach={requestDetachPatient}
        refresh={() => rerun(new class{})}
    />
}