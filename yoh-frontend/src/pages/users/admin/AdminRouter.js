import React from "react";
import {Route, Routes} from "react-router-dom";
import {VHomeAdmin} from "./pages/home/VHomeAdmin";
// import {UsersPage} from "./pages/UsersPage";
// import {ComponentsPage} from "./pages/ComponentsPage";
// import {OrganizationPage} from "./pages/OrganizationPage";
// import {Game} from "./pages/p";


export function AdminRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<VHomeAdmin/>}/>
            {/*<Route path={'/users_page'} element={<UsersPage/>}/>*/}
            {/*<Route path={'/components_page'} element={<ComponentsPage/>}/>*/}
            {/*<Route path={'/organization_page'} element={<OrganizationPage/>}/>*/}
            {/*<Route path={'/game'} element={<Game/>}/>*/}
        </Routes>
    )
}