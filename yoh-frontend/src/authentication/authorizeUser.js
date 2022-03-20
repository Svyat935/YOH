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

    const login = (token, userRole, theme) => {
        setToken(token);
        setUserRole(userRole);
        setTheme(theme);
    };

    const logout = () => {
        setToken(null);
        setUserRole(null);
        setTheme(null);
    };

    return {token, userRole, theme}
}