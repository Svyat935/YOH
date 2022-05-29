import React from "react";
import "./progressBarCircular.css";

export function ProgressBarCircular(props) {
    return (
        <div className="progress-bar-circular">
            <div className="circle per" style={{
                backgroundImage: "conic-gradient(#6A6DCD " + props.length + "%, #FFFFFF 0)"
            }}>
                <div className="inner">{props.length}%</div>
            </div>
            <p>{props.content}</p>
        </div>
    )
}