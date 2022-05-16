import React from "react";
import "./InputGender.css";

export function InputGender() {
    return (
        <select id={"gender-input"} className={"gender-input"}>
            <option selected={true} value={"Мужской"}>Мужчина</option>
            <option value={"Женский"}>Женщина</option>
        </select>
    )
}