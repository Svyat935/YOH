import React from "react";
import {Link, Route, Routes} from "react-router-dom";
import {AccountPage} from "./pages/AccountPage";
import {MainPage} from "../patient/pages/MainPage";
import {ChatPage} from "../patient/pages/ChatPage";
import {GamesPage} from "../patient/pages/GamesPage";
import {TestsPage} from "../patient/pages/TestsPage";
import {PatientPage} from "./pages/PatientPage";


export function TutorRouter() {
    return (
        <Routes>
            <Route path={'/'} element={
                <div>
                    <p>Tutor page</p>
                    <Link to={"main_page"}>
                        <button>Main Page</button>
                    </Link>
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
            <Route path={'/statistics_page'} element={<TestsPage/>}/>
            <Route path={'/account_page'} element={<AccountPage/>}/>
        </Routes>
    )
}