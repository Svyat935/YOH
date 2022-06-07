import {VStat} from "./VStat";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";
import {LoadPage} from "../../../../../components/loadpage/LoadPage";

export function CStat() {
    const context = useContext(UserContext);
    const [_, rerun] = useState(new class {
    });
    const [attempts, setAttempts] = useState([]);
    const [indexAttempt, setIndexAttempt] = useState(0);
    const [currentAttempt, setCurrentAttempt] = useState([]);
    const [allTime, setAllTime] = useState(null);
    const [clicks, setClicks] = useState(null);
    const [answers, setAnswers] = useState(null);
    const [timelines, setTimelines] = useState(null);
    const [load, setLoad] = useState(true);

    const requestGetAttemptsGames = async (game_id) => {
        return await fetch("http://mobile.itkostroma.ru/api/statistic_pagination?" +
            "gp_id=" + encodeURIComponent(game_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    const requestGetAllTime = async (sg_id) => {
        return await fetch("http://mobile.itkostroma.ru/api/all_time_widget?" +
            "sg_id=" + encodeURIComponent(sg_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    const requestGetClicks = async (sg_id) => {
        return await fetch("http://mobile.itkostroma.ru/api/clicks_widget?" +
            "sg_id=" + encodeURIComponent(sg_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    const requestGetAnswers = async (sg_id) => {
        return await fetch("http://mobile.itkostroma.ru/api/answers_widget?" +
            "sg_id=" + encodeURIComponent(sg_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    const requestGetTimeLine = async (sg_id) => {
        return await fetch("http://mobile.itkostroma.ru/api/timeline_widget?" +
            "sg_id=" + encodeURIComponent(sg_id), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'token': context.token
            }
        }).then((response) => {
            if (response.status === 200) return response.json()
            else return null;
        });
    }

    useEffect(async () => {
        if (context.token) {
            let attempt;
            if (attempts.length === 0) {
                let responseAttempts = await requestGetAttemptsGames(context.info.gamePatientId);
                let attempts = responseAttempts["attempts"];
                setAttempts(attempts);
                attempt = attempts.slice(-1)[0];
                setCurrentAttempt(attempts.length - 1);
                setIndexAttempt(attempts.length);
            } else {
                attempt = attempts[currentAttempt];
            }

            let responseAllTime = await requestGetAllTime(attempt);
            if (responseAllTime !== null) setAllTime(responseAllTime);

            let responseClicks = await requestGetClicks(attempt);
            if (responseClicks !== null) setClicks(responseClicks);

            let responseAnswers = await requestGetAnswers(attempt);
            if (responseAnswers !== null) setAnswers(responseAnswers);

            let responseTimeline = await requestGetTimeLine(attempt);
            if (responseTimeline !== null) setTimelines(responseTimeline);

            if (load === true) setLoad(false);
        }
    }, [context, _])

    return (
        <LoadPage status={load}>
            <VStat
                context={context}
                refresh={() => rerun(new class {
                })}
                allTime={allTime}
                clicks={clicks}
                answers={answers}
                timelines={timelines}
                attempts={attempts}
                changeIndexAttempt={setIndexAttempt}
                indexAttempt={indexAttempt}
                currentAttempt={currentAttempt}
                setCurrentAttempt={setCurrentAttempt}
            />
        </LoadPage>
    )
}