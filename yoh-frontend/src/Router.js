import React, {useContext} from "react";
import {BrowserRouter, Routes, Route, Link} from 'react-router-dom';
// import {EntityRouter} from "./pages/users/EntityRouter";
// import {AuthPage} from "./pages/basic/Auth/AuthPage";
// import {RegPage} from "./pages/basic/Reg/RegPage";
import {VHome} from "./pages/basic/home/VHome";
// import {UserContext} from "./context/userContext";


export function Router() {
    // const userContext = useContext(UserContext);

    return (
        <BrowserRouter>
            <Routes>
                {/*<Route path={"/user/*"} element={<EntityRouter/>}/>*/}
                {/*<Route path={"/auth/"} element={<AuthPage/>}/>*/}
                {/*<Route path={"/reg/"} element={<RegPage/>}/>*/}
                <Route path={"/*"} element={<VHome/>}/>
            </Routes>
        </BrowserRouter>
    )
}