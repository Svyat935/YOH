import 'bootstrap/dist/css/bootstrap.min.css';
import React from "react";
import {UserContext} from "./context/userContext";
import {LocalAuthorizeUser} from "./context/authorizeUser";
import {Router} from "./Router";

function App() {
    const {token, userRole, theme, info, login, logout, addInfo} = LocalAuthorizeUser();

    return (
        <UserContext.Provider value={
            {
                token: token,
                userRole: userRole,
                theme: theme,
                info: info,
                login: login,
                logout: logout,
                addInfo: addInfo
            }
        }>
            <Router/>
        </UserContext.Provider>
    );
}

export default App;
