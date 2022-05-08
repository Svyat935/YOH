import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomeAdmin} from "./pages/home/CHomeAdmin";
import {VUsersAdmin} from "./pages/users/VUsersAdmin";
// import {UsersPage} from "./pages/UsersPage";
// import {ComponentsPage} from "./pages/ComponentsPage";
// import {OrganizationPage} from "./pages/OrganizationPage";
// import {Game} from "./pages/p";


export function AdminRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomeAdmin/>}/>
            <Route path={'/users/'} element={<VUsersAdmin/>}/>
            <Route path={'/components/'} element={<p>ok 2</p>}/>
            {/*<Route path={'/organization_page'} element={<OrganizationPage/>}/>*/}
            {/*<Route path={'/game'} element={<Game/>}/>*/}
        </Routes>
    )
}