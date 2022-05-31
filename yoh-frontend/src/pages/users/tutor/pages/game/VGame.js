import React, {useContext, useEffect, useState} from "react";
import {Back} from "../../../../../components/back/Back";
import {Container} from "react-bootstrap";
import {UserContext} from "../../../../../context/userContext";
import {TutorNav} from "../../../../../components/navigate/NavPanel/Tutor/TutorNav";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function VGame() {
    let context = useContext(UserContext);
    const [src, setSrc] = useState(null);
    const [load, setLoad] = useState(true);

    useEffect(() => {
        if (context.token) {
            setSrc(context.info.url);
        }
    }, [context])

    return (
        <Back navPanel={<TutorNav context={context}/>}>
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