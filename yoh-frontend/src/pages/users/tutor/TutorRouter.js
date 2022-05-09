import React from "react";
import {Route, Routes} from "react-router-dom";
import {CHomeTutor} from "./pages/home/CHomeTutor";


export function TutorRouter() {
    return (
        <Routes>
            <Route path={'/'} element={<CHomeTutor/>}/>
            {/*<Route path={'/main_page'} element={<MainPage/>}/>*/}
            {/*<Route path={'/chat_page'} element={<ChatPage/>}/>*/}
            {/*<Route path={'/patient_page'} element={<PatientPage/>}/>*/}
            {/*<Route path={'/statistics_page'} element={<StatisticsPage/>}/>*/}
            {/*<Route path={'/games_page'} element={<GamesPage/>}/>*/}
            {/*<Route path={'/account_page'} element={<AccountPage/>}/>*/}
            {/*<Route path={'/game'} element={<Game/>}/>*/}
        </Routes>
    )
}