import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomePatient} from "./pages/home/СHomePatent";
import {CAccount} from "./pages/account/CAccount";
import {VChat} from "./pages/chat/VChat";


export function PatientRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomePatient/>}/>
            <Route path={'/account'} element={<CAccount/>}/>
            <Route path={'/chat'} element={<VChat/>}/>
            {/*<Route path={'/games_page'} element={<GamesPage/>}/>*/}
            {/*<Route path={'/tests_page'} element={<TestsPage/>}/>*/}
            {/*<Route path={'/game'} element={<Game/>}/>*/}
        </Routes>
    )
}