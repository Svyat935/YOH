import './Back.css';
import React from "react";

export function Back(props) {
    return (
        <div>
            {props.navPanel}
            <div id={"content"}>
                <div id={"hat"}>{props.nav}</div>
                <div id={"bin"} style={{height: 80}}/>
                {props.children}
                <div id={"footer"}/>
            </div>
        </div>
    )
}