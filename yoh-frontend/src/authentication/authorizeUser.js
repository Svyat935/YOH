import {useCallback, useEffect, useState} from "react";

const STORAGE_NAME = 'userContext';

export const LocalAuthorizeUser = () => {
    const [token, setToken] = useState(null),
        [userRole, setUserRole] = useState(null),
        [theme, setTheme] = useState("white");

    const login = useCallback((token, userRole) => {
        setToken(token);
        setUserRole(userRole);

        localStorage.setItem(STORAGE_NAME, JSON.stringify({
            token: token, role: userRole,
        }))
    }, []);

    const logout = useCallback(() => {
        setToken(null);
        setUserRole(null);

        localStorage.removeItem(STORAGE_NAME)
    }, []);

    useEffect(() => {
        let data = JSON.parse(localStorage.getItem(STORAGE_NAME));

        if (data && data.token){
            login(data.token, data.userRole);
        }
    });

    return {token, userRole, theme, login, logout}
}