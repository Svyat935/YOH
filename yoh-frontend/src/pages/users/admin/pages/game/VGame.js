import React, {useContext} from "react";
import {AdminNav} from "../../../../../components/navigate/Admin/AdminNav";
import {Back} from "../../../../../components/back/Back";
import {Container} from "react-bootstrap";
import {UserContext} from "../../../../../context/userContext";

export function VGame() {
    let context = useContext(UserContext);

    return (
        <Back navPanel={<AdminNav/>}>
            <Container>
                <iframe src={context.info} style={{width: "90vw", height: "100vh"}}/>
            </Container>
        </Back>
    )
}