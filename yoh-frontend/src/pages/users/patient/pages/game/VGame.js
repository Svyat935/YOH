import React, {useContext, useEffect, useState} from "react";
import {Back} from "../../../../../components/back/Back";
import {Container} from "react-bootstrap";
import {UserContext} from "../../../../../context/userContext";
import {PatientNav} from "../../../../../components/navigate/NavPanel/Patient/PatientNav";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {useNavigate} from "react-router-dom";

export function VGame() {
    let context = useContext(UserContext);
    const [src, setSrc] = useState(null);
    const [load, setLoad] = useState(true);
    const router = useNavigate();

    window.addEventListener("message", (event) => {alert( "получено: " + event.data )});

    useEffect(() => {
        if (context.token) {
            setSrc(context.info.url);
        }
    }, [context])

    return (
        <Back navLeft={
            <ButtonA style={{boxShadow: "none"}}
                     text={"Вернуться"} onClick={() => router("/user/patient/")}/>}
              navPanel={<PatientNav context={context}/>}>
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
                        src={src}
                        style={{width: "95vw", height: "82vh"}}
                        onLoad={() => setLoad(false)}
                    />
                </LoadPage>
            </div>
        </Back>
    )
}