import {Link} from "react-router-dom";
import React from "react";

export function BasicNav() {
    return (
        <ul className={"nav"}>
            <li className={"nav-item"}>
                <Link className={"nav-link"} to={"#"}>Новости платформы</Link>
            </li>
            <li className={"nav-item"}>
                <Link className={"nav-link"} to={"#"}>Контакты</Link>
            </li>
            <li className={"nav-item"}>
                <Link className={"nav-link"} to={"/auth/"}>Авторизация</Link>
            </li>
        </ul>
    )
}