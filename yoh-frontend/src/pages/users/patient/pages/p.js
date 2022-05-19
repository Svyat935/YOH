import React, {useContext} from "react";
import {UserContext} from "../../../../context/userContext";
import {Link} from "react-router-dom";

export function Game() {
    const context = useContext(UserContext);

    return (
        <div>
            <h1>In Game</h1>
            <Link to={"/user/patient/"}>
                <button>To Back</button>
            </Link>
            <div>
                <iframe width="100%" height="1200" src={context.theme + "?token=" + context.token}/>
            </div>
        </div>
    )
}