import "./PopUp.css";
import React from "react";

export function PopUp(props) {

    return (
        <div className={"popup"}>
            <div className={"popup-content"}>
                <p>{props.text}</p>
            </div>
        </div>
    )
}