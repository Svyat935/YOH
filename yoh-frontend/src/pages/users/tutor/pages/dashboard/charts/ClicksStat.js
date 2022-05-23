import React, {Component} from "react";
import Chart from "react-apexcharts";

export class ClickStat extends Component {
    constructor(props) {
        super(props);

        this.state = {
            options: {
                chart: {
                    type: 'bar',
                    stacked: true,
                },
                plotOptions: {
                    bar: {
                        columnWidth: '45%',
                    }
                },
                colors: ['#00D8B6','#008FFB',  '#FEB019', '#FF4560', '#775DD0'],
                labels: ['1 уровень', '2 уровень', '3 уровень', '4 уровень', '5 уровень'],
                xaxis: {
                    labels: {show: false},
                    axisBorder: {show: false},
                    axisTicks: {show: false},
                },
                yaxis: {
                    axisBorder: {show: false},
                    axisTicks: {show: false},
                    labels: {
                        style: {
                            colors: '#78909c'
                        }
                    }
                },
                title: {
                    text: 'Статистика кликов',
                    align: 'left',
                    style: {
                        fontSize: '18px'
                    }
                }
            },
            series: [{
                name: "Клики",
                data: [13, 12, 17, 10, 5],
            }, {
                name: "Миссклики",
                data: [6, 5, 1, 2, 0],
            }],
        };
    }

    render() {
        return (
            <Chart
                height={380}
                width={'100%'}
                options={this.state.options}
                series={this.state.series}
                type="bar"
            />
        )
    }
}