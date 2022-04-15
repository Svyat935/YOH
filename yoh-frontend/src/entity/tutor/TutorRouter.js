import React from "react";
import {Link, Route, Routes} from "react-router-dom";
import {AccountPage} from "./pages/AccountPage";
import {PatientPage} from "./pages/PatientPage";
import {StatisticsPage} from "./pages/StatisticsPage";
import {MainPage} from "./pages/MainPage";
import {ChatPage} from "./pages/ChatPage";
import {GamesPage} from "./pages/GamesPage";
import {Game} from "../patient/pages/p";


export function TutorRouter() {
    return (
        <Routes>
            <Route path={'/'} element={
                <div>
                    <p>Tutor page</p>
                    {/*<Link to={"main_page"}>*/}
                    {/*    <button>Main Page</button>*/}
                    {/*</Link>*/}
                    {/*<Link to={"chat_page"}>*/}
                    {/*    <button>Chat Page</button>*/}
                    {/*</Link>*/}
                    <Link to={"patient_page"}>
                        <button>Patients Page</button>
                    </Link>
                    <Link to={"games_page"}>
                        <button>Games Page</button>
                    </Link>
                    <Link to={"tests_page"}>
                        <button>Statistics Page</button>
                    </Link>
                    <Link to={"account_page"}>
                        <button>Account Page</button>
                    </Link>
                    <Link to={"/"}>
                        <button>To back</button>
                    </Link>
                </div>
            }/>
            <Route path={'/main_page'} element={<MainPage/>}/>
            <Route path={'/chat_page'} element={<ChatPage/>}/>
            <Route path={'/patient_page'} element={<PatientPage/>}/>
            <Route path={'/statistics_page'} element={<StatisticsPage/>}/>
            <Route path={'/games_page'} element={<GamesPage/>}/>
            <Route path={'/account_page'} element={<AccountPage/>}/>
            <Route path={'/game'} element={<Game/>}/>
        </Routes>
    )
}