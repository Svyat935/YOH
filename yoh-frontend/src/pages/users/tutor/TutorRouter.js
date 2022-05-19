import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomeTutor} from "./pages/home/CHomeTutor";
import {CAccount} from "./pages/account/CAccount";


export function TutorRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomeTutor/>}/>
            <Route path={'/account'} element={<CAccount/>}/>
            {/*<Route path={'/main_page'} element={<MainPage/>}/>*/}
            {/*<Route path={'/chat_page'} element={<ChatPage/>}/>*/}
            {/*<Route path={'/patient_page'} element={<PatientPage/>}/>*/}
            {/*<Route path={'/statistics_page'} element={<StatisticsPage/>}/>*/}
            {/*<Route path={'/games_page'} element={<GamesPage/>}/>*/}
            {/*<Route path={'/game'} element={<Game/>}/>*/}
        </Routes>
    )
}