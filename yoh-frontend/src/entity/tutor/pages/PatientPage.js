import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../authentication/userContext";
import {Link} from "react-router-dom";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";

function PatientPageView(props) {
    const detachPatient = (user_id) => {
        props.detach(user_id);
    }

    const createPatientsView = () => {
        let view = []

        let users = props.users;
        if (users !== undefined) {
            users = users["patientList"];
            users.forEach((user) => {
                view.push(
                    <Row key={user.id} style={{"background": "wheat"}}>
                        <p>Id {user.id}; name {user.name}; surname {user.surname}
                            <button onClick={() => detachPatient(user.id)}>Detach</button>
                        </p>
                    </Row>
                )
            })
        }
        return view
    }

    return (
        <Container>
            <h3>Patients who is attached on tutor.</h3>
            {createPatientsView()}
        </Container>
    )
}

function PatientAllPageView(props) {
    const attachPatient = (user_id) => {
        props.attach(user_id);
    }

    const createAllPatientsView = () => {
        let view = []

        let users = props.users;
        if (users !== undefined) {
            users = users["patientList"];
            users.forEach((user) => {
                view.push(
                    <Row key={user.id} style={{"background": "wheat"}}>
                        <p>Id {user.id}; name {user.name}; surname {user.surname}
                            <button onClick={() => attachPatient(user.id)}>Attach</button>
                        </p>
                    </Row>
                )
            })
        }
        return view
    }

    return (
        <Container>
            <h3>All Patients from organizations</h3>
            {createAllPatientsView()}
        </Container>
    )
}

export function PatientPage() {
    const context = useContext(UserContext);
    const [viewUser, setViewUser] = useState(null);
    const [viewAllUser, setViewAllUser] = useState(null);
    const [reset, executeReset] = useState(false);

    const requestAttachUser = async (user_id) => {
        return await fetch("/tutor/patients/attaching", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
            body: JSON.stringify({patient: user_id})
        }).then((response) => {
            if (response.status === 200) {
                if (reset === false) {
                    executeReset(true);
                } else {
                    executeReset(false);
                }
            }
        });
    }

    const requestDetachUser = async (user_id) => {
        return await fetch("/tutor/patients/detaching", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
            body: JSON.stringify({patient: user_id})
        }).then((response) => {
            if (response.status === 200) {
                if (reset === false) {
                    executeReset(true);
                } else {
                    executeReset(false);
                }
            }
        });
    }

    const requestGetUser = async () => {
        return await fetch("/tutor/patients/getting", {
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

    const requestGetAllUser = async () => {
        return await fetch("/tutor/patients/getting/all", {
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

    useEffect(async () => {
        if (context.token !== null) {
            let users = await requestGetUser();
            users = users["jsonObject"];

            if (users["patientList"].length > 0) setViewUser(<PatientPageView users={users}
                                                                              detach={requestDetachUser}/>)
            else setViewUser(null);

            users = await requestGetAllUser();
            users = users["jsonObject"];

            if (users["patientList"].length > 0) setViewAllUser(<PatientAllPageView users={users}
                                                                                    attach={requestAttachUser}/>)
            else setViewAllUser(null);
        }
    }, [context, reset])

    return (
        <div>
            <h1>Patient Page</h1>
            <Link to={"/user/tutor/"}>
                <button>To Back</button>
            </Link>
            {viewUser}
            {viewAllUser}
        </div>
    )
}