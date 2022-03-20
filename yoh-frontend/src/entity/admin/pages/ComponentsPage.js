import React from "react";

function ViewComponentsPage(){
    return (
        <h1>LOL</h1>
    );
}

export function ComponentsPage(props) {
    const token = props.token;

    return (
        <div>
            <h1></h1>
            <ViewComponentsPage/>
        </div>
    );
}