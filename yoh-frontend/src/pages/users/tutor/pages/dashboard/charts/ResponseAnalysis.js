import React, {Component} from "react";
import Chart from "react-apexcharts";

export class ResponseAnalysis extends Component {
    constructor(props) {
        super(props);

        this.current = JSON.stringify([props.correct, props.incorrect]);
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
                labels: props.labels,
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
                    data: props.correct,
                },
                {
                    name: "Неправильные ответы",
                    data: props.incorrect,
                }
            ],
        };
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.current !== JSON.stringify([this.props.correct, this.props.incorrect])){
            this.current = JSON.stringify([this.props.correct, this.props.incorrect]);
            this.setState(
                {
                    options:{
                        ...prevState.options,
                        labels: this.props.labels,
                    },
                    series: [
                        {
                            name: "Правильные ответы",
                            data: this.props.correct,
                        },
                        {
                            name: "Неправильные ответы",
                            data: this.props.incorrect,
                        }
                    ],
                }
            )
        }
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