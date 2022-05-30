import React, {useContext, useEffect, useState} from "react";
import {AdminNav} from "../../../../../components/navigate/NavPanel/Admin/AdminNav";
import {Back} from "../../../../../components/back/Back";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function VGame() {
    let context = useContext(UserContext);
    const [load, setLoad] = useState(true);

    return (
        <Back navPanel={<AdminNav context={context}/>}>
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
                        id={"game-iframe"}
                        src={context.info}
                        style={{width: "95vw", height: "82vh"}}
                        onLoad={() => setLoad(false)}
                    />
                </LoadPage>
            </div>
        </Back>
    )
}