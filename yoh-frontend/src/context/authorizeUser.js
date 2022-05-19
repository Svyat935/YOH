import {useEffect, useState} from "react";

const STORAGE_NAME = 'userContext';

export const LocalAuthorizeUser = () => {
    const [token, setToken] = useState(null),
        [userRole, setUserRole] = useState(null),
        [theme, setTheme] = useState("white"),
        [info, setInfo] = useState(null);

    const login = (token, userRole, info) => {
        setToken(token);
        setUserRole(userRole);
        setInfo(info);

        localStorage.setItem(STORAGE_NAME, JSON.stringify({
            token: token, role: userRole, info: info
        }))
    };

    const logout = () => {
        setToken(null);
        setUserRole(null);

        localStorage.removeItem(STORAGE_NAME)
    };

    const addInfo = (info) => {
        setInfo(info);

        let data = JSON.parse(localStorage.getItem(STORAGE_NAME));
        localStorage.setItem(STORAGE_NAME, JSON.stringify({
            token: data.token, role: data.role, info: info
        }))
    };

    useEffect(() => {
        let data = JSON.parse(localStorage.getItem(STORAGE_NAME));

        if (data && data.token){
            login(data.token, data.role, data.info);
        }
    }, []);

    return {token, userRole, theme, info, login, logout, addInfo}
}