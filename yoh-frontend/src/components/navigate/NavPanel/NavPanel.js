import React from "react";
import {Link} from "react-router-dom";
import "./NavPanel.css";

export function NavPanel(props) {
    const createLinks = () => {
        let links = props.links,
            view = [];

        for (let link of links){
            view.push(
                <li>
                    <Link className="menu__item"
                          to={link['to']}
                          onClick={link['onClick']}
                    >{link['text']}</Link>
                </li>
            )
        }
        return view;
    }

    return (
        <div className="hamburger-menu">
            <input id="menu__toggle" type="checkbox"/>
            <label className="menu__btn" htmlFor="menu__toggle">
                <span></span>
            </label>

            <ul className="menu__box">
                {createLinks()}
            </ul>
        </div>
    )
}
