import React from "react";
import {NavPanel} from "../NavPanel";

export function PatientNav(props) {
    let links = [
        {"to": "/user/patient/", 'text': 'Домашняя страница'},
        {"to": "/user/patient/account", 'text': 'Личный кабинет'},
        {"to": "/user/patient/chat", 'text': 'Чат'},
        {"to": "/", 'text': 'Выйти', 'onClick': props.context ? props.context.logout : null},
    ]

    return <NavPanel links={links}/>
}