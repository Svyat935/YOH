import React from "react";
import "./ChatContainerSend.css";

export function ChatContainerSend(props) {
    return (
        <div style={{display: "flex", justifyContent: "flex-start"}}>
            <div className="chat-container-send">
                <img src={props.image} alt="Avatar"/>
                <p>{props.text}</p>
                <span className="time-right">{props.date}</span>
            </div>
        </div>
    )
}