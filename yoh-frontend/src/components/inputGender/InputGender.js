import React from "react";
import "./InputGender.css";

export function InputGender(props) {
    return (
        <select id={"gender-input"} className={"gender-input"}>
            <option selected={!props.defaultValue || props.defaultValue === "Мужской"} value={"Мужской"}>Мужчина</option>
            <option selected={props.defaultValue && props.defaultValue === "Женский"} value={"Женский"}>Женщина</option>
        </select>
    )
}