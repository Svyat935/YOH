import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomeAdmin} from "./pages/home/CHomeAdmin";
import {CUsersAdmin} from "./pages/users/CUsersAdmin";
import {CComponents} from "./pages/components/CComponents";
// import {ComponentsPage} from "./pages/ComponentsPage";
// import {OrganizationPage} from "./pages/OrganizationPage";
// import {Game} from "./pages/p";


export function AdminRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomeAdmin/>}/>
            <Route path={'/users/'} element={<CUsersAdmin/>}/>
            <Route path={'/components/'} element={<CComponents/>}/>
            {/*<Route path={'/organization_page'} element={<OrganizationPage/>}/>*/}
            {/*<Route path={'/game'} element={<Game/>}/>*/}
        </Routes>
    )
}