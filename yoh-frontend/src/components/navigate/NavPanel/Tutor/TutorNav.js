import React from "react";
import {NavPanel} from "../NavPanel";

export function TutorNav(props) {
    let links = [
        {"to": "/user/tutor/", 'text': 'Домашняя страница'},
        {"to": "/user/tutor/account", 'text': 'Личный кабинет'},
        {"to": "/user/tutor/allPatients", 'text': 'Все наблюдаемые'},
        {"to": "/user/tutor/patients", 'text': 'Наблюдаемые'},
        {"to": "/user/tutor/chat", 'text': 'Чат'},
        {"to": "/", 'text': 'Выйти', 'onClick': props.context ? props.context.logout : null},
    ]

    return <NavPanel links={links}/>
}