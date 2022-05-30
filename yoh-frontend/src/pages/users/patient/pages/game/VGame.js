import React, {useContext, useEffect, useState} from "react";
import {Back} from "../../../../../components/back/Back";
import {Container} from "react-bootstrap";
import {UserContext} from "../../../../../context/userContext";
import {PatientNav} from "../../../../../components/navigate/NavPanel/Patient/PatientNav";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function VGame() {
    let context = useContext(UserContext);
    const [view, setView] = useState(null);
    const [load, setLoad] = useState(true);

    useEffect(() => {
        if (context.token){
            setView(
                <div style={
                    {
                        display: "flex",
                        justifyContent: "center"
                    }
                }>
                    <LoadPage
                        status={load}
                    >
                        <iframe
                            src={context.info.url}
                            style={{width: "95vw", height: "82vh"}}
                            onLoad={() => setLoad(false)}
                        />
                    </LoadPage>
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