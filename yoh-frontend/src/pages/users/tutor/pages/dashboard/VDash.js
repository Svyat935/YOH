import React from "react";
import "./Dash.css";
import {TransitTime} from "./charts/TransitTime";
import {ResponseAnalysis} from "./charts/ResponseAnalysis";
import {ClickStat} from "./charts/ClicksStat";
import {GameTime} from "./charts/GameTime";

export function VDash (props) {

    return (
        <div id="wrapper">
            <div className="content-area">
                <div className="container-fluid">
                    <div className="main">

                        <div className="row sparkboxes mt-4 mb-4">
                            <div className="col">
                                <div className="box box1">
                                    {props.allTime !== null ?
                                        <TransitTime
                                            data={props.allTime['spend_time']}
                                            title={props.allTime['all_time_spent']}
                                            labels={props.allTime['level_names']}
                                        /> : "Отсутствует статистика"
                                    }
                                </div>
                            </div>
                        </div>

                        <div className="row mt-5 mb-4">
                            <div className="col-md-6">
                                <div className="box">
                                    {props.clicks !== null ?
                                        <ClickStat
                                            labels={props.clicks['level_names']}
                                            missclicks={props.clicks['missclicks']}
                                            clicks={props.clicks['clicks']}
                                        /> : "Отсутствует статистика"
                                    }
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="box">
                                    {props.answers !== null ?
                                        <ResponseAnalysis
                                            labels={props.answers['level_names']}
                                            incorrect={props.answers['incorrect']}
                                            correct={props.answers['correct']}
                                        /> : "Отсутствует статистика"
                                    }
                                </div>
                            </div>
                        </div>

                        <div className="row mt-4 mb-4">
                            <div className="col">
                                <div className="box">
                                    {props.timelines !== null ?
                                        <GameTime
                                            dateRange={props.timelines}
                                        /> : "Отсутствует статистика"
                                    }
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    )
}