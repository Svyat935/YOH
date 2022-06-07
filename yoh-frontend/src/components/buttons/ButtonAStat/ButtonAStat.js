import React from "react";
import "./ButtonAStat.css";

export function ButtonAStat(props) {
    let fontSize = props.fontSize;
    if (!fontSize) fontSize = "large";

    return (
        <button
            style={
                {
                    width: props.width,
                    fontSize: fontSize
                }
            }
            onClick={props.onClick}
            className={"buttons-a-stat"}
        >
            {props.text}
        </button>
    )
}