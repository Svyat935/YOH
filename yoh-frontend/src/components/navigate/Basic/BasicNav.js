import {Link} from "react-router-dom";
import React from "react";

export function BasicNav() {
    return (
        <ul className={"nav"}>
            <li className={"nav-item"}>
                <Link className={"nav-link"} to={"/"}>Домашняя страница</Link>
            </li>
            <li className={"nav-item"}>
                <Link className={"nav-link"} to={"/contacts/"}>Контакты</Link>
            </li>
            <li className={"nav-item"}>
                <Link className={"nav-link"} to={"/auth/"}>Авторизация</Link>
            </li>
        </ul>
    )
}