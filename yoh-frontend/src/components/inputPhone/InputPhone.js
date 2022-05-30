import React from "react";
import "./InputPhone.css";

export function InputPhone(props) {

    return (
        <input
            id={props.id}
            className="tel"
            style={props.style}
            value={"+7"}
            onInput={(e) => {
                let x = e.target.value.slice(2).replace(/[\D]/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
                e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
            }}/>
    )
}