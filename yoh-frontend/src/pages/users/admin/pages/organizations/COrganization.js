import {VOrganization} from "./VOrganization";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function COrganization() {
    const context = useContext(UserContext);
    const [organizations, setOrganizations] = useState([]);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

    const requestOrganization = async () => {
        return await fetch("/admins/organizations/all?", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token){
            let responseOrganizations = await requestOrganization();

            if (responseOrganizations !== null){
                responseOrganizations = responseOrganizations["jsonObject"];
                if (responseOrganizations !== undefined) setOrganizations(responseOrganizations);
                if (responseOrganizations.length === 0) setOrganizations([]);
            }

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VOrganization
                organizations={organizations}
                refresh={() => rerun(new class{})}
                context={context}
            />
        </LoadPage>
    )
}