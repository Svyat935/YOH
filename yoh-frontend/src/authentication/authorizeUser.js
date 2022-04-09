import {useEffect, useState} from "react";

const STORAGE_NAME = 'userContext';

export const LocalAuthorizeUser = () => {
    const [token, setToken] = useState(null),
        [userRole, setUserRole] = useState(null),
        [theme, setTheme] = useState("white");

    useEffect(() => {
        let data = JSON.parse(localStorage.getItem(STORAGE_NAME));

        if (data && data.token){
            login(data.token, data.userRole, data.theme);
        }
    }, []);

    const login = (token, userRole) => {
        setToken(token);
        setUserRole(userRole);
    };

    const logout = () => {
        setToken(null);
        setUserRole(null);
    };

    return {token, userRole, theme, login, logout}
}