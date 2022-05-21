import React, {useContext, useEffect, useState} from "react";
import {VUsersAdmin} from "./VUsersAdmin";
import {UserContext} from "../../../../../context/userContext";

export function CUsersAdmin() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);
    const [organizations, setOrganizations] = useState([]);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [role, setRole] = useState(-1);
    const [_, rerun] = useState(new class{});

    const requestUsers = async () => {
        return await fetch("/admins/users/all?" +
            "regex=" + encodeURIComponent(regex) + "&" +
            "role=" + encodeURIComponent(role) + "&" +
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

    const requestCreateUser = async (login, email, password) => {
        return await fetch("/users/registration", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                login: login,
                email: email,
                password: password,
            })
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null;
        });
    }

    const requestRemoveUser = async () => {
        return null
    }

    const requestAssignRole = async (role, user_id) => {
        return await fetch("/admins/assign/role", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({
                role: role,
                user: user_id
            })
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null
        });
    }

    const requestGetAllOrganizations = async () => {
        return await fetch("/admins/organizations/all", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null
        });
    }

    const requestEditPatientInfo = async (body) => {
        return await fetch("/admins/users/patient/editing", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null
        });
    }

    const requestDetailUserInfo = async (user_id) => {
        return await fetch("/admins/users/get?id=" + encodeURIComponent(user_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null
        });
    }

    const requestEditTutorInfo = async (body) => {
        return await fetch("/admins/users/tutor/editing", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null
        });
    }

    useEffect(async () => {
        if (context.token){
            let responseUsers = await requestUsers();
            let responseOrganizations = await requestGetAllOrganizations();

            if (responseUsers !== null){
                responseUsers = responseUsers["jsonObject"]["results"];
                if (responseUsers !== undefined) setUsers(responseUsers);
            }

            if (responseOrganizations !== null){
                responseOrganizations = responseOrganizations["jsonObject"]["organizationList"];
                if (responseUsers !== undefined) setOrganizations(responseOrganizations);
            }
        }
    }, [context, _])

    return <VUsersAdmin
        context={context}
        users={users}
        organizations={organizations}
        createUser={requestCreateUser}
        assignRole={requestAssignRole}
        editPatientInfo={requestEditPatientInfo}
        editTutorInfo={requestEditTutorInfo}
        getInfoUser={requestDetailUserInfo}
        setRegex={setRegex}
        setRole={setRole}
        setStart={setStart}
        start={start}
        refresh={() => rerun(new class{})}
    />
}