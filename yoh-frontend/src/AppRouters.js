import React, {useContext} from "react";
import {BrowserRouter, Routes, Route, Link} from 'react-router-dom';
import {EntityRouter} from "./entity/EntityRouter";
import {AuthPage} from "./basicEntity/Auth/AuthPage";
import {RegPage} from "./basicEntity/Reg/RegPage";
// import {UserContext} from "./authentication/userContext";


export function AppRouters() {
    // const userContext = useContext(UserContext);

    return (
        <BrowserRouter>
            <Routes>
                <Route path={"/user/*"} element={<EntityRouter/>}/>
                <Route path={"/auth/"} element={<AuthPage/>}/>
                <Route path={"/reg/"} element={<RegPage/>}/>
                <Route path={"/"} element={
                    <div>
                        <h1>Version 0.1.0</h1>
                        <Link to="/user/">
                            <button>To User</button>
                        </Link>
                        <Link to="/auth/">
                            <button>To Auth</button>
                        </Link>
                        <Link to="/reg/">
                            <button>To Auth</button>
                        </Link>
                    </div>
                }
                />
            </Routes>
        </BrowserRouter>
    )
}