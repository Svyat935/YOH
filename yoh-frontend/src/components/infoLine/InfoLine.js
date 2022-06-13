import React from "react";
import "./InfoLine.css";

export function InfoLine(props) {
    return (
        <div className={"info-line"}>
            <span>{props.infoLeft}</span>
            <span>{props.infoRight}</span>
        </div>
    )

}