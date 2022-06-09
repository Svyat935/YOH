import React from "react";
import "./InfoBlock.css";
import {CloseButton} from "react-bootstrap";
import {BsTrash} from "react-icons/bs";

export function InfoBlock(props){
    return (
        <div>
            {props.onClickClose ? <CloseButton style={{float: "right"}} onClick={props.onClickClose}/> : null}
            <div key={props.ikey} onClick={props.onClick} className={"info-block"}>
                <div className={"info-block-content"}>
                    {props.children}
                </div>
                <BsTrash style={{
                    visibility: props.trash ? "visible": "hidden",
                    position: "relative",
                    top: "33px",
                    zIndex: "10"}}
                />
                <p>{props.text}</p>
                <p>{props.addText}</p>
            </div>
        </div>
    )
}