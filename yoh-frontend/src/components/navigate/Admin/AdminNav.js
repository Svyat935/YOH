import {Button, Container, FormControl, Nav, Navbar, NavDropdown} from "react-bootstrap";
import React from "react";
import {Offcanvas} from "bootstrap";
import "./AdminNav.css";

export function AdminNav() {
    return (
        <div className="hamburger-menu">
            <input id="menu__toggle" type="checkbox"/>
            <label className="menu__btn" htmlFor="menu__toggle">
                <span></span>
            </label>

            <ul className="menu__box">
                <li><a className="menu__item" href="#">Home</a></li>
                <li><a className="menu__item" href="#">About</a></li>
                <li><a className="menu__item" href="#">Team</a></li>
                <li><a className="menu__item" href="#">Contact</a></li>
                <li><a className="menu__item" href="#">Twitter</a></li>
            </ul>
        </div>
    )
}