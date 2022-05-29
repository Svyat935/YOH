import React from "react";
import {NavPanel} from "../NavPanel";

export function TutorNav(props) {
    let links = [
        {"to": "/user/tutor/", 'text': 'Домашняя страница'},
        {"to": "/user/tutor/account", 'text': 'Личный кабинет'},
        {"to": "/user/tutor/allPatients", 'text': 'Организация'},
        {"to": "/user/tutor/patients", 'text': 'Мои наблюдаемые'},
        {"to": "/user/tutor/chat", 'text': 'Чат'},
        {"to": "/", 'text': 'Выйти', 'onClick': props.context ? props.context.logout : null},
    ]

    return <NavPanel links={links}/>
}