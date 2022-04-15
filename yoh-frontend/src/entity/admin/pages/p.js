import React, {useContext} from "react";
import {UserContext} from "../../../authentication/userContext";
import {Link} from "react-router-dom";

export function Game() {
    const context = useContext(UserContext);

    return (
        <div>
            <h1>In Game</h1>
            <Link to={"/user/admin/"}>
                <button>To Back</button>
            </Link>
            <div>
                <iframe width={1280} height={1024} src={context.theme + "?token=" + context.token}/>
            </div>
        </div>
    )
}