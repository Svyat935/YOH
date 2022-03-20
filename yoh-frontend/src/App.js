import './App.css';
import React from "react";
import {UserContext} from "./authentication/userContext";
import {LocalAuthorizeUser} from "./authentication/authorizeUser";
import {AppRouters} from "./AppRouters";

function App() {
    //TODO DELETE
    let data = JSON.stringify({token: 'testToken', userRole: 'testUserRole', theme: 'testTheme'});
    localStorage.setItem("userContext", data);
    //TODO END

    const {token, userRole, theme} = LocalAuthorizeUser();

    return (
        <UserContext.Provider value={{token: token, userRole: userRole, theme: theme}}>
            <AppRouters/>
        </UserContext.Provider>
    );
}

export default App;
