import React from "react";
import "./RightArrow.css";

export function RightArrow(props) {
    let reactFunc = props.onClick ?
        props.onClick :
        () => {
            console.log("Please, pass the function.")
        };

    return (
        <button className="button-arrow" onClick={reactFunc}>
            <div className="back-arrow">
                <div className="arrow arrow-right"/>
            </div>
        </button>
    )
}