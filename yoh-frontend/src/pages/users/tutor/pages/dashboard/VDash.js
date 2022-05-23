import React, {Component} from "react";
import "./Dash.css";
import {TransitTime} from "./charts/TransitTime";
import {ResponseAnalysis} from "./charts/ResponseAnalysis";
import {ClickStat} from "./charts/ClicksStat";
import {GameTime} from "./charts/GameTime";

export class VDash extends Component{

    render() {
        return (
            <div id="wrapper">
                <div className="content-area">
                    <div className="container-fluid">
                        <div className="main">

                            <div className="row sparkboxes mt-4 mb-4">
                                <div className="col">
                                    <div className="box box1">
                                        <TransitTime/>
                                    </div>
                                </div>
                            </div>

                            <div className="row mt-5 mb-4">
                                <div className="col-md-6">
                                    <div className="box">
                                        <ClickStat/>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="box">
                                        <ResponseAnalysis/>
                                    </div>
                                </div>
                            </div>

                            <div className="row mt-4 mb-4">
                                <div className="col">
                                    <div className="box">
                                        <GameTime/>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        )
    }
}