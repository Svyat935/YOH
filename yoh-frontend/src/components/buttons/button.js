import React from "react";
import "./Button.css";

export function Button(props) {
    return (
        <button style={{width: props.width}} onClick={props.onClick} className={"buttons"}>
            {props.text}
        </button>
    )
}