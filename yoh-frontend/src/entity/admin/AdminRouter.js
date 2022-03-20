import React, {useContext} from "react";
import {Route, Routes} from "react-router-dom";
import {UsersPage} from "./pages/UsersPage";
import {UserContext} from "../../authentication/userContext";
import {ComponentsPage} from "./pages/ComponentsPage";

export function AdminRouter() {
    const userContext = useContext(UserContext);

    return (
        <Routes>
            <Route path={'/users_page'} element={<UsersPage token={userContext.token}/>}/>
            <Route path={'/components_page'} element={<ComponentsPage token={userContext.token}/>}/>
        </Routes>
    )
}