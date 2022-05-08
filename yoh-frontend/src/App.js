import 'bootstrap/dist/css/bootstrap.min.css';
import React from "react";
import {UserContext} from "./context/userContext";
import {LocalAuthorizeUser} from "./context/authorizeUser";
import {Router} from "./Router";

function App() {
    const {token, userRole, theme, login, logout} = LocalAuthorizeUser();

    return (
        <UserContext.Provider value={{token: token, userRole: userRole, theme: theme, login: login, logout: logout}}>
            <Router/>
        </UserContext.Provider>
    );
}

export default App;
