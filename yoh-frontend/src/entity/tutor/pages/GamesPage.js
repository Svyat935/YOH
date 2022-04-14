import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../authentication/userContext";
import {Link} from "react-router-dom";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";

function GamesPageView(props) {
    function createView() {
        let view = [];
        let games = props.games;
        games.forEach((game) => {
            view.push(
                <Row style={{"background": "wheat"}}>
                   <p>id: {game.id}; name: {game.name}; description: {game.description}; url: {game.url};</p>
                </Row>
            )
        })
        return view;
    }

    return (
        <Container>
            <h3>Games in system</h3>
            {createView()}
        </Container>
    )
}

export function GamesPage() {
    const context = useContext(UserContext);
    const [view, setView] = useState(null);
    const [reset, executeReset] = useState(false);

    const requestGetGames = async () => {
        return await fetch("/games/all", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
                // if (reset === true){
                //     executeReset(false);
                // }else{
                //     executeReset(true);
                // }
            }else{
                return null;
            }
        });
    }

    useEffect(async () => {
        if (context.token !== null){
            let response = await requestGetGames();
            response = response["jsonObject"];

            if (response["games"] !== [])
                setView(<GamesPageView games={response["games"]}/>)
            else
                setView(null);
        }
    }, [context])

    return (
        <div>
            <h1>GamesPage</h1>
            <Link to={"/user/tutor/"}>
                <button>To Back</button>
            </Link>
            {view}
        </div>
    )
}