import React, {Component} from "react";
import Chart from "react-apexcharts";

export class TransitTime extends Component {
    constructor(props) {
        super(props);

        this.state = {
            options: {
                chart: {
                    id: 'sparkline1',
                    group: 'sparklines',
                    type: 'area',
                    sparkline: {
                        enabled: true
                    },
                },
                xaxis: {
                    categories: ['1 уровень', '2 уровень', '3 уровень', '4 уровень', '5 уровень']
                },
                yaxis: {
                    min: 0,
                    labels: {
                        formatter: function (val) {
                            var hours = Math.floor(val / 60 / 60);
                            var minutes = Math.floor(val / 60) - (hours * 60);
                            var seconds = val % 60;
                            var result = '';
                            if (hours) {
                                result += hours.toString() + ' ч. ';
                            }
                            if (minutes) {
                                result += minutes.toString() + ' мин. ';
                            }
                            if (seconds) {
                                result += seconds.toString() + ' сек.';
                            }
                            return result;
                        }
                    }
                },
                colors: ['#DCE6EC'],
                title: {
                    text: '1 ч. 15 мин. 35 сек.',
                    offsetX: 30,
                    margin: 2,
                    style: {
                        fontSize: '24px',
                        cssClass: 'apexcharts-yaxis-title'
                    }
                },
                subtitle: {
                    text: 'Общее время прохождения игры',
                    offsetX: 30,
                    style: {
                        fontSize: '14px',
                        cssClass: 'apexcharts-yaxis-title'
                    }
                }
            },
            series: [{
                name: 'Время прохождения',
                data: [15, 35, 120, 25, 73]
            }],
        };
    }

    render() {
        return (
            <Chart
                height={160}
                style={{paddingTop: 15}}
                options={this.state.options}
                series={this.state.series}
                type="area"
            />
        )
    }
}