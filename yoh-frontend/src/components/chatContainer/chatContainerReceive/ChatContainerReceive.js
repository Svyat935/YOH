import React from "react";
import "./ChatContainerReceive.css";

export function ChatContainerReceive(props) {
    return (
        <div style={{display: "flex", justifyContent: "flex-end"}}>
            <div className="chat-container-receive">
                <img src={props.image} alt="Avatar"/>
                <p>{props.text}</p>
                <span className="time-left">{props.date}</span>
            </div>
        </div>
    )
}