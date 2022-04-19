import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React from "react";
import {UserContext} from "./authentication/userContext";
import {LocalAuthorizeUser} from "./authentication/authorizeUser";
import {AppRouters} from "./AppRouters";

function App() {
    const {token, userRole, theme, login, logout} = LocalAuthorizeUser();

    return (
        <UserContext.Provider value={{token: token, userRole: userRole, theme: theme, login: login, logout: logout}}>
            <AppRouters/>
        </UserContext.Provider>
    );
}

export default App;
