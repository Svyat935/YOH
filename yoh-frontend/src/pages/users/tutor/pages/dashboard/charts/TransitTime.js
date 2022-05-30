import React, {Component} from "react";
import Chart from "react-apexcharts";

export class TransitTime extends Component {
    toDate(val) {
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

    constructor(props) {
        super(props);
        let title = props.title !== null ? this.toDate(props.title) : "Игра в процессе";

        this.current = props.data;
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
                    categories: props.labels
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
                    text: title,
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
                data: props.data
            }],
        };
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        let title = prevProps.title !== null ? this.toDate(prevProps.title) : "Игра в процессе";

        console.log(this.data, prevProps.data);
        if (this.data !== prevProps.data){
            this.setState(prevProps.data);
            this.setState(
                {
                    options:{
                        ...prevState.options,
                        xaxis: {
                            categories: prevProps.labels
                        },
                        title: {
                            ...prevState.title,
                            text: title,
                        }
                    },
                    series: [{
                        name: 'Время прохождения',
                        data: prevProps.data
                    }],
                }
            )
        }
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