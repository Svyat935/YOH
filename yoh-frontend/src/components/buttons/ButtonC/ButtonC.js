import React from "react";
import "./ButtonC.css";

export function ButtonC(props) {
    return (
        <button
            style={
                {
                    width: props.width,
                }
            }
            onClick={props.onClick}
            className={"buttons-c"}
        >
            {props.text}
        </button>
    )
}