import React from "react";
import "./ButtonA.css";

export function ButtonA(props) {
    return (
        <button
            style={{width: props.width, ...props.style}}
            onClick={props.onClick}
            className={"buttons-a"}
        >
            {props.text}
        </button>
    )
}