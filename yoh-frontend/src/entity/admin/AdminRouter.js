import React, {useContext} from "react";
import {Link, Route, Routes} from "react-router-dom";
import {UsersPage} from "./pages/UsersPage";
import {ComponentsPage} from "./pages/ComponentsPage";
import {UserContext} from "../../authentication/userContext";


export function AdminRouter() {
    const userContext = useContext(UserContext);

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
                </div>
            }/>
            <Route path={'/users_page'} element={<UsersPage token={userContext.token}/>}/>
            <Route path={'/components_page'} element={<ComponentsPage token={userContext.token}/>}/>
        </Routes>
    )
}