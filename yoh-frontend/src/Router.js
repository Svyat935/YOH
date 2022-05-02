import React from "react";
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import {EntityRouter} from "./pages/users/EntityRouter";
import {VHome} from "./pages/basic/home/VHome";
import {CAuth} from "./pages/basic/Auth/CAuth";


export function Router() {

    return (
        <BrowserRouter>
            <Routes>
                <Route path={"/user/*"} element={<EntityRouter/>}/>
                <Route path={"/auth/"} element={<CAuth/>}/>
                <Route path={"/*"} element={<VHome/>}/>
            </Routes>
        </BrowserRouter>
    )
}