import React, {useContext, useEffect, useState} from "react";
import {Back} from "../../../../../components/back/Back";
import {Container} from "react-bootstrap";
import {UserContext} from "../../../../../context/userContext";
import {PatientNav} from "../../../../../components/navigate/Patient/PatientNav";

export function VGame() {
    let context = useContext(UserContext);
    let [view, setView] = useState(null);

    useEffect(() => {
        if (context.token){
            setView(
                <Container>
                    <iframe src={context.info.url} style={{width: "90vw", height: "100vh"}}/>
                </Container>
            )
        }
    }, [context])

    return (
        <Back navPanel={<PatientNav/>}>
            {view}
        </Back>
    )
}