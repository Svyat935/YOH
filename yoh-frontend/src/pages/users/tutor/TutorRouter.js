import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomeTutor} from "./pages/home/CHomeTutor";
import {CAccount} from "./pages/account/CAccount";
import {CAllPatients} from "./pages/allPatients/CAllPatients";
import {VChat} from "./pages/chat/VChat";
import {CPatients} from "./pages/patients/CPatients";
import {CDetailInfo} from "./pages/detailInfo/СDetailInfo";
import {CVector} from "./pages/vector/CVector";
import {VGame} from "./pages/game/VGame";
import {CDelete} from "./pages/delete/CDelete";


export function TutorRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomeTutor/>}/>
            <Route path={'/account'} element={<CAccount/>}/>
            <Route path={'/allPatients'} element={<CAllPatients/>}/>
            <Route path={'/patients'} element={<CPatients/>}/>
            <Route path={'/chat'} element={<VChat/>}/>
            <Route path={'/detail'} element={<CDetailInfo/>}/>
            <Route path={'/vector'} element={<CVector/>}/>
            <Route path={'/game'} element={<VGame/>}/>
            <Route path={'/delete'} element={<CDelete/>}/>
        </Routes>
    )
}