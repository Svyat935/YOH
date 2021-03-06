import React, {useContext, useEffect, useState} from "react";
import {VUsersAdmin} from "./VUsersAdmin";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CUsersAdmin() {
    const context = useContext(UserContext);
    const [users, setUsers] = useState([]);
    const [organizations, setOrganizations] = useState([]);
    const [order, setOrder] = useState(1);
    const [regex, setRegex] = useState("");
    const [start, setStart] = useState(0);
    const [limit, setLimit] = useState(10);
    const [size, setSize] = useState(0);
    const [role, setRole] = useState(-1);
    const [_, rerun] = useState(new class{});
    const [load, setLoad] = useState(true);

    const requestGetImageTutor = async (user_id) => {
        return await fetch("/admins/users/tutor/image?"+
            "userID=" + encodeURIComponent(user_id), {
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

    const requestGetImagePatient = async (user_id) => {
        return await fetch("/admins/users/patient/image?"+
            "userID=" + encodeURIComponent(user_id), {
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

    const requestUsers = async () => {
        return await fetch("/admins/users/all?" +
            "regex=" + encodeURIComponent(regex) + "&" +
            "role=" + encodeURIComponent(role) + "&" +
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

    const requestUser = async (specRegex) => {
        return await fetch("/admins/users/all?" +
            "regex=" + encodeURIComponent(specRegex) + "&" +
            "start=" + encodeURIComponent(0) + "&" +
            "limit=" + encodeURIComponent(10), {
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

    const requestChangePassword = async (user_id, password) => {
        return await fetch("/admins/user/password/edit", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({
                password: password,
                user_id: user_id
            })
        }).then((response) => {
            if (response.status === 200) return response.json();
            else return null
        });
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
        return await fetch("/admins/organizations/all?" +
            "start=" + encodeURIComponent(0) + "&" +
            "limit=" + encodeURIComponent(100), {
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
                setSize(responseUsers["size"]);

                responseUsers = responseUsers["results"];
                for (let user of responseUsers){
                    if (user["role"] === 1){
                        let image = await requestGetImagePatient(user["id"]);
                        if (image["image"]) user["image"] = image["image"];
                        else user["image"] = null;
                    } else if (user["role"] === 3){
                        let image = await requestGetImageTutor(user["id"]);
                        if (image["image"]) user["image"] = image["image"];
                        else user["image"] = null;
                    }
                }
                if (responseUsers !== undefined) setUsers(responseUsers);
            }

            if (responseOrganizations !== null){
                responseOrganizations = responseOrganizations["results"];
                if (responseUsers !== undefined) setOrganizations(responseOrganizations);
            }

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VUsersAdmin
                context={context}
                users={users}
                organizations={organizations}
                getImagePatient={requestGetImagePatient}
                getImageTutor={requestGetImageTutor}
                createUser={requestCreateUser}
                assignRole={requestAssignRole}
                editPatientInfo={requestEditPatientInfo}
                editTutorInfo={requestEditTutorInfo}
                getInfoUser={requestDetailUserInfo}
                getUser={requestUser}
                changePassword={requestChangePassword}
                setOrder={setOrder}
                setRegex={setRegex}
                setRole={setRole}
                setStart={setStart}
                start={start}
                limit={limit}
                size={size}
                refresh={() => rerun(new class{})}
            />
        </LoadPage>
    )
}