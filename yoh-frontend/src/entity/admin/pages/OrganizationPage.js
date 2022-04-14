import React, {useContext, useEffect, useState} from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {UserContext} from "../../../authentication/userContext";
import {Link} from "react-router-dom";

function ViewOrganizations(props) {
    const createOrganizationView = (organizations) => {
        let view = [];
        if (organizations !== null) {
            organizations["organizationList"].forEach((organization) => {
                view.push(
                    <Container key={organization.id} style={{"mapping": "10"}}>
                        <Row style={{"background": "wheat"}}>
                            <Col>id: {organization.id}; name: {organization.name};
                                address: {organization.address}; email: {organization.email};
                                phone: {organization.phone}; website: {organization.website};
                            </Col>
                        </Row>
                    </Container>
                )
            })
        }
        return view;
    }

    return (
        <Container>
            <p>All Organizations</p>
            <Row>
                {createOrganizationView(props.orgs)}
            </Row>
        </Container>
    );
}

function ViewFormCreateOrganizations(props) {
    const createOrg = () => {
        let name = document.getElementById("name").value,
            address = document.getElementById("address").value,
            email = document.getElementById("email").value,
            phone = document.getElementById("phone").value,
            website = document.getElementById("website").value;
        name = name === "" ? null : name;
        address = address === "" ? null : address;
        email = email === "" ? null : email;
        phone = phone === "" ? null : phone;
        website = website === "" ? null : website;

        let body = {"name": name};
        if (address !== null) body["address"] = address;
        if (phone !== null) body["phone"] = phone;
        if (email !== null) body["email"] = email;
        if (website !== null) body["website"] = website;

        props.createOrg(body);
    }

    return (
        <Container>
            <Row>
                <h4>Create organizations</h4><p>
                <label htmlFor={"name"}>Name of organization<br/>
                    <input type={"text"} placeholder={"name"} id={"name"} required={true}/>
                </label>
                <label htmlFor={"address"}><br/>
                    <input type={"text"} placeholder={"address"} id={"address"}/>
                </label>
                <label htmlFor={"email"}><br/>
                    <input type={"email"} placeholder={"email"} id={"email"}/>
                </label>
                <label htmlFor={"phone"}><br/>
                    <input type={"phone"} placeholder={"phone"} id={"phone"}/>
                </label>
                <label htmlFor={"website"}><br/>
                    <input type={"website"} placeholder={"website"} id={"website"}/>
                </label>
                <button onClick={() => createOrg()}>Отправить</button></p>
            </Row>
        </Container>
    );
}

export function OrganizationPage() {
    const context = useContext(UserContext);
    const [view, setView] = useState(null);
    const [reset, executeReset] = useState(false);

    const requestGetOrganizations = async () => {
        return await fetch("/admins/organizations/all", {
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
        })
    }

    const getOrganizations = async () => {
        let response = null;
        if (context.token !== null) {
            response = await requestGetOrganizations();
        }
        return response !== null ? response["jsonObject"] : null;
    }

    const requestCreateOrganization = async (body) => {
        return await fetch("/admins/organizations/add", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 200) {
                if (reset === true){
                    executeReset(false);
                }else{
                    executeReset(true);
                }
            }
        });
    }

    useEffect(() => {
        (async () => {
            let organizations = await getOrganizations();
            setView(<ViewOrganizations orgs={organizations}/>)
        })()
    }, [context, reset])

    return (
        <div>
            <h1>Organization Page</h1>
            <Link to={"/user/admin/"}>
                <button>To Back</button>
            </Link>
            <ViewFormCreateOrganizations createOrg={requestCreateOrganization}/>
            {view}
        </div>
    );
}