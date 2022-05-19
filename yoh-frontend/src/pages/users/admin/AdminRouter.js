import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomeAdmin} from "./pages/home/CHomeAdmin";
import {CUsersAdmin} from "./pages/users/CUsersAdmin";
import {CComponents} from "./pages/components/CComponents";
import {VGame} from "./pages/game/VGame";


export function AdminRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomeAdmin/>}/>
            <Route path={'/users/'} element={<CUsersAdmin/>}/>
            <Route path={'/components/'} element={<CComponents/>}/>
            <Route path={'/game'} element={<VGame/>}/>
        </Routes>
    )
}