import React from "react";
import "./ProgressBar.css";

export function ProgressBar(props) {
    return (
        <div className="container-progressbar">
            <div className="container-progressbar__progress" style={{width: props.percent + "%"}}>
                {props.percent + "%"}
            </div>
        </div>
    )
}