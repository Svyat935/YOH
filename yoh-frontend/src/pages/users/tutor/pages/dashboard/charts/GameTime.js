import React, {Component} from "react";
import Chart from "react-apexcharts";

export class GameTime extends Component {
    constructor(props) {
        super(props);

        this.state = {
            options: {
                chart: {
                    type: 'rangeBar'
                },
                plotOptions: {
                    bar: {
                        horizontal: true
                    }
                },
                xaxis: {
                    type: 'datetime',
                    labels: {
                        datetimeUTC: false,
                    }
                },
                colors: ['#00D8B6','#008FFB',  '#FEB019', '#FF4560', '#775DD0'],
                title: {
                    text: 'Игровое время',
                    style: {
                        fontSize: '18px',
                    }
                },
                tooltip: {
                    x: {
                        format: "d MMM HH:mm"
                    }
                }
            },
            series: [
                {
                    data: [
                        {
                            x: '1 уровень',
                            y: [
                                new Date('2019-03-01 11:39:51.136659').getTime(),
                                new Date('2019-03-01 12:39:51.136659').getTime()
                            ]
                        },
                        {
                            x: '2 уровень',
                            y: [
                                new Date('2019-03-01 12:39:51.136659').getTime(),
                                new Date('2019-03-01 13:39:51.136659').getTime()
                            ]
                        },
                        {
                            x: '2 уровень',
                            y: [
                                new Date('2019-03-01 15:39:51.136659').getTime(),
                                new Date('2019-03-01 16:39:51.136659').getTime()
                            ]
                        },
                        {
                            x: '3 уровень',
                            y: [
                                new Date('2019-03-01 13:39:51.136659').getTime(),
                                new Date('2019-03-01 14:39:51.136659').getTime()
                            ]
                        },
                        {
                            x: '4 уровень',
                            y: [
                                new Date('2019-03-01 14:39:51.136659').getTime(),
                                new Date('2019-03-01 15:39:51.136659').getTime()
                            ]
                        },
                        {
                            x: '5 уровень',
                            y: [
                                new Date('2019-03-01 16:39:51.136659').getTime(),
                                new Date('2019-03-01 17:39:51.136659').getTime()
                            ]
                        },

                    ]
                }
            ],
        };
    }

    render() {
        return (
            <Chart
                height={350}
                options={this.state.options}
                series={this.state.series}
                type="rangeBar"
            />
        )
    }
}