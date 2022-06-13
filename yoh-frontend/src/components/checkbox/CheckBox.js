import React from "react";

export function CheckBox(props) {
    const [checked, setChecked] = React.useState(false);

    const handleChange = () => {
        setChecked(!checked);
    };

    return <input
        id={props.id}
        style={props.style}
        type="checkbox"
        checked={checked}
        value={checked}
        onChange={handleChange}
    />
}