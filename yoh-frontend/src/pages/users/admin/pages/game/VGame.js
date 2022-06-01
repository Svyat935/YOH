import React, {useContext, useEffect, useState} from "react";
import {AdminNav} from "../../../../../components/navigate/NavPanel/Admin/AdminNav";
import {Back} from "../../../../../components/back/Back";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {useNavigate} from "react-router-dom";

export function VGame() {
    let context = useContext(UserContext);
    const [load, setLoad] = useState(true);
    const router = useNavigate();

    return (
        <Back navLeft={
            <ButtonA style={{boxShadow: "none"}}
                     text={"Вернуться"} onClick={() => router("/user/tutor/detail/")}/>}
              navPanel={<AdminNav context={context}/>}>
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