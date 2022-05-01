import React, {useContext} from "react";
import {BrowserRouter, Routes, Route, Link} from 'react-router-dom';
// import {EntityRouter} from "./pages/users/EntityRouter";
import {VHome} from "./pages/basic/home/VHome";
import {CAuth} from "./pages/basic/Auth/CAuth";
// import {UserContext} from "./context/userContext";


export function Router() {
    // const userContext = useContext(UserContext);

    return (
        <BrowserRouter>
            <Routes>
                {/*<Route path={"/user/*"} element={<EntityRouter/>}/>*/}
                <Route path={"/auth/"} element={<CAuth/>}/>
                <Route path={"/*"} element={<VHome/>}/>
            </Routes>
        </BrowserRouter>
    )
}