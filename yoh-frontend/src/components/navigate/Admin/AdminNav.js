import {Button, Container, FormControl, Nav, Navbar, NavDropdown} from "react-bootstrap";
import React from "react";
import {Offcanvas} from "bootstrap";
import "./AdminNav.css";
import {Link} from "react-router-dom";

export function AdminNav(props) {
    return (
        <div className="hamburger-menu">
            <input id="menu__toggle" type="checkbox"/>
            <label className="menu__btn" htmlFor="menu__toggle">
                <span></span>
            </label>

            <ul className="menu__box">
                <li><Link className="menu__item" to={"/user/admin/"}>Домашняя страница</Link></li>
                <li><Link className="menu__item" to={"/user/admin/users"}>Пользователи</Link></li>
                <li><Link className="menu__item" to={"/user/admin/components"}>Компоненты</Link></li>
                <li><Link className="menu__item" to={"/"} onClick={
                    props.context ? props.context.logout : null
                }>Выйти</Link></li>
            </ul>
        </div>
    )
}