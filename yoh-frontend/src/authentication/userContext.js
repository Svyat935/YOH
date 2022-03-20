import {createContext} from "react";

export const UserContext = createContext({
    token: null,
    userRole: null,
    theme: null,
})