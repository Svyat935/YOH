import React from "react";
import "./InputBirthday.css";

export function InputBirthday(props) {
    return (
        <input
            id={"input-birthday"}
            className={"input-birthday"}
            type={"date"}
            defaultValue={props.defaultValue}
        />
    )
}