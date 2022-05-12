import {useCallback, useEffect, useState} from "react";

const STORAGE_NAME = 'userContext';

export const LocalAuthorizeUser = () => {
    const [token, setToken] = useState(null),
        [userRole, setUserRole] = useState(null),
        [theme, setTheme] = useState("white"),
        [info, setInfo] = useState(null);

    const login = useCallback((token, userRole) => {
        setToken(token);
        setUserRole(userRole);

        localStorage.setItem(STORAGE_NAME, JSON.stringify({
            token: token, role: userRole, info: info
        }))
    }, []);

    const logout = useCallback(() => {
        setToken(null);
        setUserRole(null);

        localStorage.removeItem(STORAGE_NAME)
    }, []);

    const addInfo = useCallback((info) => {
        let data = JSON.parse(localStorage.getItem(STORAGE_NAME));
        data["info"] = info;
        setInfo(info);

        localStorage.setItem(STORAGE_NAME, JSON.stringify(data));
    }, [])

    useEffect(() => {
        let data = JSON.parse(localStorage.getItem(STORAGE_NAME));

        if (data && data.token){
            login(data.token, data.role);
            addInfo(data.info);
        }
    });

    return {token, userRole, theme, info, login, logout, addInfo}
}