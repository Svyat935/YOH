import Row from "react-bootstrap/Row";
import Container from "react-bootstrap/Container";
import React, {useContext, useState} from "react";
import {UserContext} from "../../../authentication/userContext";

function GamesPageView(props) {
    function createGames() {
        let games = props.games,
            view = [];

        games.forEach((game) => {
            view.push(
                <Row key={game.id}>
                    <p>id: {game.id}; name: {game.name}; description: {game.name}; url: {game.url + "?token=stub"}</p>
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
        return await fetch("/games/getting", {
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
}