import React, {useContext, useEffect, useState} from "react";
import {Back} from "../../../../../components/back/Back";
import {Container} from "react-bootstrap";
import {UserContext} from "../../../../../context/userContext";
import {PatientNav} from "../../../../../components/navigate/NavPanel/Patient/PatientNav";

export function VGame() {
    let context = useContext(UserContext);
    let [view, setView] = useState(null);

    useEffect(() => {
        if (context.token){
            setView(
                <div style={
                    {
                        display: "flex",
                        justifyContent: "center"
                    }
                }>
                    <iframe src={context.info} style={{width: "95vw", height: "82vh"}}/>
                </div>
            )
        }
    }, [context])

    return (
        <Back navPanel={<PatientNav context={context}/>}>
            {view}
        </Back>
    )
}