import React from "react";
import {NavPanel} from "../NavPanel";

export function AdminNav(props) {
    let links = [
        {"to": "/user/admin/", 'text': 'Домашняя страница'},
        {"to": "/user/admin/users", 'text': 'Пользователи'},
        {"to": "/user/admin/components", 'text': 'Компоненты'},
        {"to": "/", 'text': 'Выйти', 'onClick': props.context ? props.context.logout : null},
    ];
    return <NavPanel links={links}/>
}