import {UserContext} from "../authentication/userContext";
import React, {useContext} from "react";
import {Route, Routes} from "react-router-dom";
import {AdminRouter} from "./admin/AdminRouter";

export function EntityRouter() {
    // const userContext = useContext(UserContext);

    return (
        <Routes>
            <Route path={'/admin/*'} element={<AdminRouter/>}/>
            <Route path={'/patient/*'} element={<h1>PatientPage</h1>}/>
            <Route path={'/tutor/*'} element={<h1>TutorPage</h1>}/>
            <Route path={'/researcher/*'} element={<h1>ResearcherPage</h1>}/>
        </Routes>
    )
}