import Row from "react-bootstrap/Row";
import Container from "react-bootstrap/Container";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../context/userContext";
import {Link} from "react-router-dom";

function GamesPageView(props) {
    const context = useContext(UserContext);

    function createGames() {
        let games = props.games,
            view = [];

        games.forEach((game) => {
            view.push(
                <Row key={game.id}>
                    <p>id: {game.id};
                        name: {game.name};
                        description: {game.description};
                        url: <Link onClick={() => {context.theme = game.url}} to={"/user/patient/game"}>
                            Перейти
                        </Link>
                    </p>
                </Row>
            )
        })
        return view;
    }

    return (
        <Container style={{"background": "wheat"}}>
            {createGames()}
        </Container>
    );
}

export function GamesPage() {
    const context = useContext(UserContext);
    const [view, setView] = useState(null);

    const requestGetGames = async () => {
        return await fetch("/patient/games/getting", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            }else{
                return null;
            }
        });
    }

    useEffect(async() => {
        if (context.token !== null){
            let response = await requestGetGames();
            response = response["jsonObject"];

            if (response !== []) setView(<GamesPageView token={context.token} games={response["gamesArray"]}/>)
        }
    }, [context]);

    return (
        <div>
            <h1>Games page</h1>
            <Link to={"/user/patient/"}>
                <button>To Back</button>
            </Link>
            {view}
        </div>
    )
}