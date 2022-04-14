import React, {useContext, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {UserContext} from "../../../authentication/userContext";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";

function ViewComponentsPage(props){
    function createGames() {
        let games = props.games,
            view = [];

        games.forEach((game) => {
            view.push(
                <Row>
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

export function ComponentsPage() {
    const context = useContext(UserContext);
    const [view, setView] = useState(null);

    const requestGetComponents = async () => {
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
            let response = await requestGetComponents();
            response = response["jsonObject"];

            if (response !== []) setView(<ViewComponentsPage games={response["games"]}/>)
        }
    }, [context])

    return (
        <div>
            <h1>Components Page</h1>
            <Link to={"/user/admin/"}>
                <button>To Back</button>
            </Link>
            {view}
        </div>
    );
}