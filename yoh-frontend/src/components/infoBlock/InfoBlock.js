import React from "react";
import "./InfoBlock.css";
import {CloseButton} from "react-bootstrap";

export function InfoBlock(props){
    return (
        <div>
            {props.onClickClose ? <CloseButton style={{float: "right"}} onClick={props.onClickClose}/> : null}
            <div key={props.ikey} onClick={props.onClick} className={"info-block"}>
                <div className={"info-block-content"}>
                    {props.children}
                </div>
                <p>{props.text}</p>
                <p>{props.addText}</p>
            </div>
        </div>
    )
}