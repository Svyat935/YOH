import React from "react";
import {TutorNav} from "../../../../../components/navigate/Tutor/TutorNav";
import {Back} from "../../../../../components/back/Back";

export function VStat(props) {
    return (
        <Back navPanel={<TutorNav context={props.context}/>}>

        </Back>
    )
}