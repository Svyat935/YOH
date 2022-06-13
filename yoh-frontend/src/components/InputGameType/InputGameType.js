import React from "react";
import "./InputGameType.css";

export function InputGameType() {
    return (
        <select id={"input-game-type"} className={"input-game-type"}>
            <option selected={true} value={"Без типа"}>Без типа</option>
            <option value={"Социальные нормы"}>Социальные нормы</option>
            <option value={"Бытовая жизнь"}>Бытовая жизнь</option>
            <option value={"Развлекательная"}>Развлекательная</option>
        </select>
    )
}