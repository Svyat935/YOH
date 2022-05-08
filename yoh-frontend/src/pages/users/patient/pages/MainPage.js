import {useContext} from "react";
import {UserContext} from "../../../../context/userContext";

function MainPageView() {

}

export function MainPage() {
    const context = useContext(UserContext);

    const requestGames = async () => {
        return await fetch("/patient/games/getting", {
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

    return (
        <div>
            <h3>Main Page</h3>
        </div>
    )
}