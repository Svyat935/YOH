import React from "react";
import "./LoadPage.css";

export function LoadPage(props) {
    return (
        <>
            <div style={
                {
                    visibility: props.status ? "visible": "hidden",
                    opacity: props.status ? 1: 0
                }
            } className="back-load-page">
                <div className="clear-loading loading-effect-2">
                    <span></span>
                </div>
            </div>
            {props.children}
        </>
    )
}