import React from "react";
import {Route, Routes} from "react-router-dom";
import {AdminRouter} from "./admin/AdminRouter";
import {PatientRouter} from "./patient/PatientRouter";
import {TutorRouter} from "./tutor/TutorRouter";

export function EntityRouter() {

    return (
        <Routes>
            <Route path={'/admin/*'} element={<AdminRouter/>}/>
            <Route path={'/patient/*'} element={<PatientRouter/>}/>
            <Route path={'/tutor/*'} element={<TutorRouter/>}/>
            <Route path={'/researcher/*'} element={<h1>ResearcherPage</h1>}/>
        </Routes>
    )
}