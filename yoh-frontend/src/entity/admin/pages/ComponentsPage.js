import React, {useContext, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {UserContext} from "../../../authentication/userContext";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";

function ViewComponentsPage(props){
    const context = useContext(UserContext);

    function createGames() {
        let games = props.games,
            view = [];

        games.forEach((game) => {
            view.push(
                <Row key={game.id}>
                    <p>id: {game.id}; name: {game.name}; description: {game.description};
                        url: https://mobile.itkostroma.ru/games/{game.id}/;
                        <Link onClick={() => {context.theme = game.url}} to={"/user/admin/game"}>
                            Перейти
                        </Link></p>
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

export function ComponentsPage() {
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
            }else{
                return null;
            }
        });
    }

    const requestAddGame = async (name, description, url) => {
        return await fetch("/games/adding", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            },
            body: JSON.stringify({
                name: name,
                description: description,
                url: url
            })
        }).then((response) => {
            if (response.status === 200) {
                if (reset === false) {
                    executeReset(true);
                } else {
                    executeReset(false);
                }
            }
        });
    }

    const createGame = async () => {
        let name = document.getElementById("name").value,
            description = document.getElementById("description").value,
            url = document.getElementById("url").value;

        await requestAddGame(name, description, url);
    }

    useEffect(async () => {
        if (context.token !== null){
            let response = await requestGetGames();
            response = response["jsonObject"];

            if (response !== []) setView(<ViewComponentsPage games={response["games"]}/>)
        }
    }, [context, reset])

    return (
        <div>
            <h1>Components Page</h1>
            <Link to={"/user/admin/"}>
                <button>To Back</button>
            </Link>
            <p>
                <input type={"text"} id={"name"} placeholder={"name"}/>
                <input type={"text"} id={"description"} placeholder={"description"}/>
                <input type={"text"} id={"url"} placeholder={"url"}/>
                <button onClick={() => createGame()}>Add new game</button>
            </p>
            {view}
        </div>
    );
}