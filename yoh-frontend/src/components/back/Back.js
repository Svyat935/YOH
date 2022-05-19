import './Back.css';
import React from "react";

export function Back(props) {
    return (
        <div style={props.style}>
            {props.navPanel}
            <div id={"content"}>
                <div id={"hat"}>{props.nav}</div>
                <div id={"bin"} style={{height: 80}}/>
                {props.children}
                <div id={"footer"}>
                    <p>
                        <span>Костромской государственный университет</span>
                        <span>https://ksu.edu.ru/</span>
                    </p>
                </div>
            </div>
        </div>
    )
}