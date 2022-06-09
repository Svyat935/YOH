import React, {useContext, useState} from "react";
import {InfoBlock} from "../../../../../components/infoBlock/InfoBlock";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";
import {AdminNav} from "../../../../../components/navigate/NavPanel/Admin/AdminNav";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import gameStub from "../../../../../assets/gameStub.jpg";
import {useNavigate} from "react-router-dom";
import {CheckBox} from "../../../../../components/checkbox/CheckBox";
import {SearchFrame} from "../../../../../frame/SearchFrame/SearchFrame";
import {InputGameType} from "../../../../../components/InputGameType/InputGameType";
import {BsSortAlphaUp, BsSortAlphaDown, BsSortNumericDown, BsSortNumericUp} from "react-icons/bs";

export function VComponents(props) {
    const filterList = [
        {
            "text": "По алфавиту", "icon": <BsSortAlphaDown size={"1.3em"}/>, "value": 1, "defaultChecked": true, "onClick": () => {
                props.setOrder(1);
                props.refresh();
            }
        },
        {
            "text": "По алфавиту", "icon": <BsSortAlphaUp size={"1.3em"}/>, "value": -1, "onClick": () => {
                props.setOrder(-1);
                props.refresh();
            }
        },
        {
            "text": "По дате добавления", "icon": <BsSortNumericDown size={"1.3em"}/>, "value": 2, "onClick": () => {
                props.setOrder(2);
                props.refresh();
            }
        },
        {
            "text": "По дате добавления", "icon": <BsSortNumericUp size={"1.3em"}/>, "value": -2, "onClick": () => {
                props.setOrder(-2);
                props.refresh();
            }
        },
        {
            "text": "По типу", "icon": <BsSortAlphaDown size={"1.3em"}/>, "value": 3, "onClick": () => {
                props.setOrder(3);
                props.refresh();
            }
        },
        {
            "text": "По типу", "icon": <BsSortAlphaUp size={"1.3em"}/>, "value": -3, "onClick": () => {
                props.setOrder(-3);
                props.refresh();
            }
        },
    ]
    const router = useNavigate();
    const [show, setShow] = useState(false);
    //TODO: replace the int type with something better.
    //Note: Adding Game - 0, Removing Game - 1, Confirm Removing - 2, Changing Game - 3, Look Choice - 4.
    const [buttonStatus, setButtonStatus] = useState(0);
    const [gameForChanging, setChangingGame] = useState(null);

    const createBasicViewGames = () => {
        let games = props.games.slice(0, 9),
            view = [];

        if (games.length > 0) {
            games.forEach((game) => {
                let image = game["image"] !== null ? "https://mobile.itkostroma.ru/images/" + game["image"]
                    : gameStub;

                view.push(
                    <InfoBlock trash={game["gameActiveStatus"] === "DELETED"}
                               ikey={game["id"]}
                               text={game["name"]}
                               addText={"Тип: " + game["type"]}
                               onClick={
                        () => {
                            setButtonStatus(4);
                            setChangingGame(game);
                            setShow(true);
                        }
                    }>
                        <img style={{width: "100%", height: "100%", borderRadius: 40, objectFit: "cover"}}
                             src={image} alt={'game'}/>
                    </InfoBlock>
                )
            })
        } else {
            view.push(
                <div style={
                    {
                        height: 387,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center"
                    }
                }>
                    <h3>Игры в системе отсутствуют.</h3>
                </div>
            )
        }
        return view;
    }

    const addGame = async () => {
        let fName = document.getElementById("name"),
            fDescription = document.getElementById("description"),
            fFile = document.getElementById("file"),
            fType = document.getElementById("input-game-type"),
            fStatistics = document.getElementById("useStatistics"),
            fImage = document.getElementById("image");
        let valid_style = "red 1px solid"
        let none_style = "none"
        let valid_status = true;

        fName.style.border = none_style;
        fDescription.style.border = none_style;
        fType.style.border = none_style;
        fFile.style.border = none_style;

        if (!fName.value.trim()) {
            fName.style.border = valid_style;
            valid_status = false;
        }

        if (!fDescription.value.trim()) {
            fDescription.style.border = valid_style;
            valid_status = false;
        }

        if (!fType.value.trim()) {
            fType.style.border = valid_style;
            valid_status = false;
        }

        if (!fFile.files[0]) {
            fFile.style.border = valid_style;
            valid_status = false;
        }

        if (valid_status) {
            let formData = new FormData();
            formData.append("name", fName.value.trim());
            formData.append("description", fDescription.value.trim());
            formData.append("type", fType.value.trim());
            formData.append("file", fFile.files[0]);
            formData.append("image", fImage.files[0]);
            formData.append("useStatistic", fStatistics.value);
            let response = await props.sendGames(formData);
            // //TODO: Validate.
            props.refresh();
            setShow(false);
        }
    }

    const removeGame = () => {
        //TODO: Validate.
        let response = props.removeGame(gameForChanging["id"]);
        if (response !== null) {
            props.refresh();
            setShow(false);
        }
    }

    const changeGame = async () => {
        // TODO: Validate
        let fName = document.getElementById("name"),
            fDescription = document.getElementById("description");

        let body = {}
        if (fName.value) body["name"] = fName.value;
        if (fDescription.value) body["description"] = fDescription.value;

        if (JSON.stringify(body) !== '{}') {
            body["game_id"] = gameForChanging["id"];
            let response = await props.changeGame(body);
            props.refresh();
        }

        setShow(false);
    }

    const changingView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px"
                }
            }>
                <label>Новое Название: </label>
                <input id={"name"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
                } required defaultValue={gameForChanging["name"] ? gameForChanging["name"] : null}/>
                <p id={"name-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Описание: </label>
                <textarea id={"description"} maxLength={320} rows={7} style={
                    {resize: "none", borderRadius: 40, border: "none", padding: "10px 20px", marginBottom: 10, width: "100%"}
                } required defaultValue={gameForChanging["description"] ? gameForChanging["description"] : null}/>
                <p id={"description-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
            </div>
        )
    }

    const createGamesView = () => {
        return (
            <div style={
                {
                    display: "flex",
                    flexDirection: "column",
                    padding: "0 10px",
                    alignItems: "flex-start"
                }
            }>
                <label>Название: </label>
                <input id={"name"} type={"text"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10, width: "100%"}
                } required/>
                <p id={"name-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Описание: </label>
                <textarea id={"description"} maxLength={320} rows={7} style={
                    {resize: "none", borderRadius: 40, border: "none", padding: "10px 20px", marginBottom: 10, width: "100%"}
                } required/>
                <p id={"description-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Тип: </label>
                <InputGameType/>
                <p id={"type-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Используется рекомендуемый класс для отправки статистики: </label>
                <CheckBox id={"useStatistics"} style={
                    {borderRadius: 40, marginBottom: 10, width: 20, height: 20}
                }/>
                <label>Файл: </label>
                <input id={"file"} type={"file"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                } required/>
                <p id={"file-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>
                <label>Изображение: </label>
                <input id={"image"} type={"file"} style={
                    {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 15}
                } required/>
            </div>
        )
    }

    const searchGame = () => {
        let searchValue = document.getElementById("searchInput").value;
        props.setRegex(searchValue);
        props.refresh();
    }

    const chooseAction = (
        <div style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center"
        }}>
            <div>
                <p><b>Название</b>: {gameForChanging ? gameForChanging["name"]: null}</p>
                <p><b>Тип</b>: {gameForChanging ? gameForChanging["type"] : null}</p>
                <p><b>Описание</b>: {gameForChanging ? gameForChanging["description"] : null}</p>
            </div>
            {   gameForChanging && gameForChanging["gameActiveStatus"] !== "DELETED" ?
                <>
                    <div style={{height: 1, width: "100%", border: "#FFFFFF 1px solid"}}/>
                    <ButtonB width={"75%"} text={"Посмотреть игру"} onClick={() => {
                        let url = "https://" + gameForChanging["url"] + "?" +
                            "token=" + props.context.token + "&" +
                            "use_statistics=" + gameForChanging["useStatistic"];
                        props.context.addInfo(url);
                        router("/user/admin/game/");}
                    }/>
                    <ButtonB width={"75%"} text={"Изменить игру"} onClick={() => {
                        setButtonStatus(3);
                    }}/>
                    <ButtonB width={"75%"} text={"Удалить"} onClick={() => {
                setButtonStatus(2);
            }}/>
                </> : null
            }
        </div>
    )

    const buttons = (
        <>
            <ButtonA width={300} text={"Добавить +"} onClick={() => {
                setButtonStatus(0);
                setShow(true);
            }}/>
        </>
    )

    const paginationButtons = () => {
        let view = [];

        if (props.start) view.push(
            <ButtonA width={300} text={"Предыдущая страница"} onClick={() => {
                props.setStart(props.start - 9);
                props.refresh();
            }}/>
        )
        if (props.games.length === 10) view.push(
            <ButtonA width={300} text={"Следующая страница"} onClick={() => {
                props.setStart(props.start + 9);
                props.refresh();
            }}/>
        )
        return view;
    }

    const modalTitle = () => {
        if (buttonStatus === 0) return "Добавление Игры";
        else if (buttonStatus === 1 || buttonStatus === 2) return "Удаление игры";
        else if (buttonStatus === 3) return "Изменение игры";
        else if (buttonStatus === 4) return "Выбор действия";
    }

    const modalBody = () => {
        if (buttonStatus === 0) return createGamesView();
        else if (buttonStatus === 2)
            return "Вы уверен что хотите удалить игру с таким названием: '" + gameForChanging["name"] + "' ?";
        else if (buttonStatus === 3) return changingView();
        else if (buttonStatus === 4) return chooseAction;
    }

    const modalFooter = () => {
        let view = [<ButtonB text={"Отмена"} onClick={() => setShow(false)}/>];

        if (buttonStatus === 0) view.push(
            <ButtonB text={"Добавить"} onClick={addGame}/>
        );
        else if (buttonStatus === 2) view.push(
            <ButtonB text={"Удалить"} onClick={removeGame}/>
        );
        else if (buttonStatus === 3) view.push(
            <ButtonB text={"Изменить"} onClick={changeGame}/>
        );

        return view;
    }

    return (
        <SearchFrame
            navPanel={<AdminNav context={props.context}/>}
            filterList={filterList}
            modalShow={show}
            modalOnHide={() => setShow(false)}
            modalTitle={modalTitle()}
            modalBody={modalBody()}
            modalFooter={modalFooter()}
            title={"Список компонентов"}
            buttons={buttons}
            switch={
                {
                    title: "Удаленные игры",
                    onClick: () => {props.setShowDeleted(!props.showDeleted); props.refresh();}
                }
            }
            searchInputOnKeyDown={searchGame}
            searchInputOnBlur={searchGame}
            searchInputOnClick={searchGame}
            content={createBasicViewGames()}
            contentFooter={paginationButtons()}
        />
    )
}