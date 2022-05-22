import React from "react";
import "./SearchInput.css";
import searchIcon from "./../../assets/searchIcon.png";

export function SearchInput(props) {
    return (
        <div className={"search-input"}>
            <input
                id={props.id}
                type={"text"}
                onKeyDown={props.onKeyDown}
                onBlur={props.onBlur}
            />
                <button onClick={props.onClick}>
                    <img src={searchIcon} alt={"searchIcon"}/>
                </button>
        </div>
    )
}