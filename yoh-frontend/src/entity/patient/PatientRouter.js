import React from "react";
import {Link, Route, Routes} from "react-router-dom";
import {ChatPage} from "./pages/ChatPage";
import {GamesPage} from "./pages/GamesPage";
import {MainPage} from "./pages/MainPage";
import {TestsPage} from "./pages/TestsPage";
import {AuthPage} from "../../basicEntity/Auth/AuthPage";
import {AccountPage} from "./pages/AccountPage";


export function PatientRouter() {
    return (
        <Routes>
            <Route path={'/'} element={
                <div>
                    <p>Patient page</p>
                    <Link to={"main_page"}>
                        <button>Main Page</button>
                    </Link>
                    {/*<Link to={"chat_page"}>*/}
                    {/*    <button>Chat Page</button>*/}
                    {/*</Link>*/}
                    <Link to={"games_page"}>
                        <button>Games Page</button>
                    </Link>
                    <Link to={"tests_page"}>
                        <button>Tests Page</button>
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
            <Route path={'/games_page'} element={<GamesPage/>}/>
            <Route path={'/tests_page'} element={<TestsPage/>}/>
            <Route path={'/account_page'} element={<AccountPage/>}/>
        </Routes>
    )
}