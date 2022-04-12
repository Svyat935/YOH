import React from "react";
import {Link, Route, Routes} from "react-router-dom";
import {UsersPage} from "./pages/UsersPage";
import {ComponentsPage} from "./pages/ComponentsPage";
import {OrganizationPage} from "./pages/OrganizationPage";


export function AdminRouter() {
    return (
        <Routes>
            <Route path={'/'} element={
                <div>
                    <p>Admin page</p>
                    <Link to={"users_page"}>
                        <button>Users page</button>
                    </Link>
                    <Link to={"components_page"}>
                        <button>Components page</button>
                    </Link>
                    <Link to={"organization_page"}>
                        <button>Organization page</button>
                    </Link>
                </div>
            }/>
            <Route path={'/users_page'} element={<UsersPage/>}/>
            <Route path={'/components_page'} element={<ComponentsPage/>}/>
            <Route path={'/organization_page'} element={<OrganizationPage/>}/>
        </Routes>
    )
}