import React from "react";
import "./SwitchInput.css";

export function SwitchInput(props) {
    return (
        <label className="switch">
            <input type="checkbox" onClick={props.options.onClick}/>
                <span className="slider round"/>
        </label>
    )
}