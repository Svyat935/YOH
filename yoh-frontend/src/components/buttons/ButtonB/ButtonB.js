import React from "react";
import "./ButtonB.css";

export function ButtonB(props) {
    return (
        <button
            style={{width: props.width}}
            onClick={props.onClick}
            className={"buttons-b"}
        >
            {props.text}
        </button>
    )
}