import React from "react";

function ViewUserPage(){
    return (
        <h1>LOL</h1>
    );
}

export function UsersPage(props) {
    const token = props.token;


    return (
        <div>
            <h1>UsersPage</h1>
            <ViewUserPage/>
        </div>
    );
}