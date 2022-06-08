import React from "react";
import "./InputOrganizations.css";

export function InputOrganization(props) {
    const createOptions = () => {
        let orgs = props.organizations,
            view = [];

        orgs.forEach((org) => {
            view.push(
                <option selected={props.defaultValue === org.name} value={org.id}>{org.name}</option>
            )
        })

        return view;
    }

    return (
        <select id={"input-organizations"} className={"input-organizations"}>
            {createOptions()}
        </select>
    )
}