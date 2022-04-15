import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../authentication/userContext";
import {Link} from "react-router-dom";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";

function GamesPageView(props) {
    const context = useContext(UserContext);

    function createView() {
        let view = [];
        let games = props.games;
        games.forEach((game) => {
            view.push(
                <Row key={game.id}>
                    <p>id: {game.id}; name: {game.name}; description: {game.description};
                        <Link to={"/user/tutor/game"} onClick={() => {
                            context.theme = game.url;
                        }}>Перейти</Link>
                    </p>
                </Row>
            )
        })
        return view;
    }

    return (
        <Container style={{"background": "wheat"}}>
            <h3>Games in system</h3>
            {createView()}
        </Container>
    )
}

function UsersView(props) {
    const [usersView, setView] = useState([]);

    const addingGame = async (user_id) => {
        let game_id = document.getElementById("games").value;
        await props.addGame(game_id, user_id);
    }

    const removingGame = async (user_id) => {
        let game_id = document.getElementById("games").value;
        await props.removeGame(game_id, user_id);
    }

    const createView = async () => {
        let view = [],
            users = props.users;

        for (const user of users) {
            let patient = await props.getPatient(user.id);
            view.push(
                <Row key={user.id}>
                    <p>id: {user.id}; name: {user.name}; surname: {user.surname}; organization: {user.organization};</p>
                    {patient["jsonObject"].games ?
                        <div>games: {(
                        () => {
                            let view = [];
                            patient["jsonObject"].games.forEach((game) => {
                                view.push(
                                    <p key={game.id} style={{"background": "grey"}}>id: {game.id}; name: {game.name}; description: {game.description};
                                        <Link onClick={() => {context.theme = game.url}} to={"/user/tutor/game"}>
                                            Перейти
                                        </Link>
                                    </p>
                                );
                            })
                            return view;
                        })()
                        }
                    </div>
                        : null}
                    <p>
                        <input type={"text"} id={"games"}/>
                        <button onClick={() => addingGame(user.id)}>Adding</button>
                        <button onClick={() => removingGame(user.id)}>Removing</button>
                    </p>
                </Row>
            )
        }

        return view;
    }

    useEffect(async () => {
       let view = await createView();
       setView(view);
    },[])

    return (
        <Container style={{"background": "wheat", "marginTop": "10px"}}>
            <h3>Patient users</h3>
            {usersView}
        </Container>
    )
}

export function GamesPage() {
    const context = useContext(UserContext);
    const [usersView, setUsersView] = useState(null);
    const [gamesView, setGamesView] = useState(null);
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
            } else {
                return null;
            }
        });
    }

    const requestGetUser = async () => {
        return await fetch("/tutor/patients/getting", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        });
    }

    const requestAddGame = async (game_id, patient_id) => {
        return await fetch("/tutor/patients/games/adding", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
            body: JSON.stringify({
                game_id: game_id,
                patient_id: patient_id
            })
        }).then((response) => {
            if (response.status === 200) {
                if (reset === true) {
                    executeReset(false);
                } else {
                    executeReset(true);
                }
            }
        });
    }

    const requestRemoveGame = async (game_id, patient_id) => {
        return await fetch("/tutor/patients/games/removing", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
            body: JSON.stringify({
                game_id: game_id,
                patient_id: patient_id
            })
        }).then((response) => {
            if (response.status === 200) {
                if (reset === true) {
                    executeReset(false);
                } else {
                    executeReset(true);
                }
            }
        });
    }

    const requestGetPatientOne = async (user_id) => {
        return await fetch("/tutor/patients/getting/one/?" + new URLSearchParams({patientUUID: user_id}).toString(), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "token": context.token
            },
        }).then((response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        });
    }

    useEffect(async () => {
        if (context.token !== null) {
            let response = await requestGetGames();
            response = response["jsonObject"];

            if (response["games"] !== []){
                setGamesView(<GamesPageView games={response["games"]}/>)
            }
            else{
                setGamesView(null);
            }

            response = await requestGetUser();
            response = response["jsonObject"];
            console.log(response);
            if (response["patientList"].length > 0){
                setUsersView(<UsersView
                    addGame={requestAddGame}
                    removeGame={requestRemoveGame}
                    getPatient={requestGetPatientOne}
                    users={response["patientList"]}
                />)
            }
        }
    }, [context])

    return (
        <div>
            <h1>GamesPage</h1>
            <Link to={"/user/tutor/"}>
                <button>To Back</button>
            </Link>
            {gamesView}
            {usersView}
        </div>
    )
}