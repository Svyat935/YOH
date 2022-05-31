import React from "react";
import "./LeftArrow.css";

export function LeftArrow(props) {
    let reactFunc = props.onClick ?
        props.onClick :
        () => {
            console.log("Please, pass the function.")
        };

    return (
        <button className="button-arrow" onClick={reactFunc}>
            <div className="back-arrow">
                <div className="arrow arrow-left"/>
            </div>
        </button>
    )
}