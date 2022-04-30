import './Back.css';
import React from "react";

export function Back(props) {
    return (
        <div>
            <div id={"hat"}>{props.nav}</div>
            <div id={"bin"} style={{height: 80}}/>
            {props.children}
            <div id={"footer"}/>
        </div>
    )
}