import {Link} from "react-router-dom";
import React from "react";
import "./TutorNav.css";

export function TutorNav() {
    return (
        <div className="hamburger-menu">
            <input id="menu__toggle" type="checkbox"/>
            <label className="menu__btn" htmlFor="menu__toggle">
                <span></span>
            </label>

            <ul className="menu__box">
                <li><Link className="menu__item" to={"/user/tutor/"}>Домашняя страница</Link></li>
                <li><Link className="menu__item" to={"/user/tutor/account"}>Личный кабинет</Link></li>
                <li><Link className="menu__item" to={"/user/tutor/users"}>Пользователи</Link></li>
                <li><Link className="menu__item" to={"/user/tutor/chat"}>Чат</Link></li>
                <li><Link className="menu__item" to={"/"}>Выйти</Link></li>
            </ul>
        </div>
    )
}