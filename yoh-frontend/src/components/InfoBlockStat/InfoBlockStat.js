import React from "react";
import "./InfoBlockStat.css";

export function InfoBlockStat(props) {
    return (
        <div className={"InfoBlockStat"} onClick={props.onClick}>
            {props.children}
        </div>
    )
}