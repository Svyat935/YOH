import {Link} from "react-router-dom";
import React from "react";
import "./PatientNav.css";

export function PatientNav() {
    return (
        <div className="hamburger-menu">
            <input id="menu__toggle" type="checkbox"/>
            <label className="menu__btn" htmlFor="menu__toggle">
                <span></span>
            </label>

            <ul className="menu__box">
                <li><Link className="menu__item" to={"/user/patient/"}>Домашняя страница</Link></li>
                <li><Link className="menu__item" to={"/user/patient/account"}>Личный кабинет</Link></li>
                <li><Link className="menu__item" to={"/user/patient/chat"}>Чат</Link></li>
                <li><Link className="menu__item" to={"/"}>Выйти</Link></li>
            </ul>
        </div>
    )
}