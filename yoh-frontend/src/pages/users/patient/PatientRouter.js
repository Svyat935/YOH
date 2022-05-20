import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomePatient} from "./pages/home/СHomePatent";
import {CAccount} from "./pages/account/CAccount";
import {VChat} from "./pages/chat/VChat";
import {VGame} from "./pages/game/VGame";


export function PatientRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomePatient/>}/>
            <Route path={'/account'} element={<CAccount/>}/>
            <Route path={'/chat'} element={<VChat/>}/>
            <Route path={'/game'} element={<VGame/>}/>
        </Routes>
    )
}