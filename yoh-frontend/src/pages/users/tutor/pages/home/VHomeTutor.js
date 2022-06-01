import React from "react";
import {TutorNav} from "../../../../../components/navigate/NavPanel/Tutor/TutorNav";
import {Container} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import {Back} from "../../../../../components/back/Back";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import profileStub from "../../../../../assets/profileStub.jpg";
import {RightArrow} from "../../../../../components/arrows/RightArrow/RightArrow";
import {useNavigate} from "react-router-dom";
import {ProgressBar} from "../../../../../components/progressBar/ProgressBar";
import {Slider} from "../../../../../components/slider/Slider";

export function VHomeTutor(props) {
    const router = useNavigate();
    const status = [];

    const createViewUsers = () => {
        let users = props.users.slice(-5),
            view = [];

        if (users.length > 0){
            users.forEach((user) => {
                let imageSrc = user["image"] ? "https://mobile.itkostroma.ru/images/" + user["image"] : profileStub;

                view.push(
                    <InfoBlock key={user["id"]} text={
                        user["surname"] && user["name"] ?
                            user["surname"] + " " + user["name"] : "Отсутствует ФИО"
                    } onClick={() => router("/user/tutor/patients/")}>
                        <div>
                            <img style={{width: "100%", borderRadius: 40}} src={imageSrc} alt={'profile'}/>
                        </div>
                    </InfoBlock>
                )
            })
        }

        return view;
    }

    const createStatusUsers = () => {
        let status = props.status.slice(-5),
            view = [];

        if (status.length > 0) {
            status.forEach((stat) => {
                const allGames = stat.statistics["Done"] + stat.statistics["Assigned"] + stat.statistics["Failed"];

                let fio = stat.user["surname"] && stat.user["name"] ? stat.user["surname"] + " " + stat.user["name"] :
                            "Отсутствует ФИО",
                    value = allGames !== 0 ? Math.round(stat.statistics["Done"] / allGames * 100) : 0
                view.push(<label>{fio}: </label>)
                view.push(<ProgressBar percent={value}/>)
            })
        }else{
            view.push(
                <div>
                    <h3>Отсутвуют назначенные пользователи.</h3>
                </div>
            )
        }

        return view;
    }

    return (
        <Back navPanel={<TutorNav context={props.context}/>}>
            <Container style={{marginTop: 20}}>
                <Row>
                    <h1 style={{fontWeight: "bold"}}>{
                        props.account !== null && props.account["name"] ?
                            "Добрый день, " + props.account["name"] + "!" : "Добрый день!"
                    }
                    </h1>
                    <h3 style={{marginTop: 20}}>Последняя активность пользователей:</h3>
                    {
                        props.users.length > 0 ?
                            <Slider max={5}>
                                {createViewUsers()}
                            </Slider> :
                        <div style={
                            {
                                height: 387,
                                width: 936,
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center"
                            }
                        }>
                            <h3>Наблюдаемые пользователи в системе отсутствуют.</h3>
                        </div>
                    }
                </Row>
            </Container>
            <Container>
                <Row>
                    <div style={
                        {
                            marginTop: 30,
                            marginBottom: 20,
                            background: "#F5F5F5",
                            boxShadow: "-10px 10px 30px rgba(0, 0, 0, 0.32)",
                            borderRadius: 40,
                        }
                    }>
                        <h3 style={{margin: "20px 50px"}}>Отслеживание успехов</h3>
                        <div style={
                            {
                                padding: "10px 20px 30px 20px"
                            }
                        }>
                            {createStatusUsers()}
                        </div>
                    </div>
                </Row>
            </Container>
        </Back>
    )
}