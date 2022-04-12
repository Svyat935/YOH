import React from "react";
import {Link} from "react-router-dom";

function ViewComponentsPage(){
    return (
        <h1>LOL</h1>
    );
}

export function ComponentsPage(props) {
    const token = props.token;

    return (
        <div>
            <Link to={"/user/admin/"}>
                <button>To Back</button>
            </Link>
            <ViewComponentsPage/>
        </div>
    );
}