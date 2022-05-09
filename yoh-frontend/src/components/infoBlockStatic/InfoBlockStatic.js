import React from "react";
import "./InfoBlockStatic.css";

export function InfoBlockStatic(props){
    return (
        <div onClick={props.onClick} className={"info-block-static"}>
            <div className={"info-block-static-content"}>
                {props.children}
            </div>
            <p>{props.text}</p>
            <p>{props.addText}</p>
        </div>
    )
}