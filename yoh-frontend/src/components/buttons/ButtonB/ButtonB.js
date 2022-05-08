import React from "react";
import "./ButtonB.css";

export function ButtonB(props) {
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
            className={"buttons-b"}
        >
            {props.text}
        </button>
    )
}