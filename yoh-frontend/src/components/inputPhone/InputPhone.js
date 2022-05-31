import React, {useEffect} from "react";
import "./InputPhone.css";
import IMask from "imask";

export function InputPhone(props) {
    useEffect(() => {
        let element = document.getElementById(props.id);
        let maskOption = {mask: "+7(000)000-00-00", lazy: false}
        let mask = new IMask(element, maskOption);
    })

    return (
        <input
            id={props.id}
            className="tel"
            style={props.style}
            placeholder={"+7(000)000-00-00"}
            />
    )
}