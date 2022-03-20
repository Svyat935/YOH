import React, {useContext} from "react";
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import {EntityRouter} from "./entity/EntityRouter";
import {AuthPage} from "./basicEntity/AuthPage";
// import {UserContext} from "./authentication/userContext";


export function AppRouters() {
    // const userContext = useContext(UserContext);

    return (
        <BrowserRouter>
            <Routes>
                <Route path={"/user/*"} element={<EntityRouter/>}/>
                <Route path={"/auth/"} element={<AuthPage/>}/>
                <Route path={"/"} element={<h1>Version 0.1.0</h1>}/>
            </Routes>
        </BrowserRouter>
    )
}