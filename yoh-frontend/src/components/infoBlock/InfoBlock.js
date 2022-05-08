import React from "react";
import "./InfoBlock.css";

export function InfoBlock(props){
    return (
        <div className={"info-block"}>
            <div className={"info-block-content"}>
                {props.children}
            </div>
            <p>{props.text}</p>
        </div>
    )
}