import React, {Component} from "react";
import Chart from "react-apexcharts";

export class ResponseAnalysis extends Component {
    constructor(props) {
        super(props);

        this.current = [props.correct, props.incorrect]
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
        if (this.current !== [prevProps.correct, prevProps.incorrect]){
            this.current = [prevProps.correct, prevProps.incorrect];
            this.setState(
                {
                    options:{
                        ...prevState.options,
                        labels: prevProps.labels,
                    },
                    series: [
                        {
                            name: "Правильные ответы",
                            data: prevProps.correct,
                        },
                        {
                            name: "Неправильные ответы",
                            data: prevProps.incorrect,
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