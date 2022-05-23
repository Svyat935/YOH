import React, {Component} from "react";
import Chart from "react-apexcharts";

export class ResponseAnalysis extends Component {
    constructor(props) {
        super(props);

        this.state = {
            options: {
                chart: {
                    type: 'bar',
                    stacked: true,
                },
                colors: ['#00D8B6', '#FF4560'],
                plotOptions: {
                    bar: {
                        columnWidth: '45%',
                    }
                },
                xaxis: {
                    labels: {
                        show: false
                    },
                    axisBorder: {show: false},
                    axisTicks: {show: false}
                },
                yaxis: {
                    axisBorder: {show: false},
                    axisTicks: {show: false},
                    labels: {
                        style: {colors: '#78909c'}
                    }
                },
                title: {
                    text: 'Анализ ответов',
                    align: 'left',
                    style: {
                        fontSize: '18px'
                    }
                }
            },
            series: [
                {
                    name: "Правильные ответы",
                    data: [1, 2, 1, 1, 1],
                },
                {
                    name: "Неправильные ответы",
                    data: [2, 0, 0, 1, 0],
                }
            ],
        };
    }

    render() {
        return <Chart
            height={380}
            width={'100%'}
            options={this.state.options}
            series={this.state.series}
            type="bar"
        />
    }
}