import React, {Component} from "react";
import Chart from "react-apexcharts";

export class ClickStat extends Component {
    constructor(props) {
        super(props);

        this.current = [props.clicks, props.missclicks];
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
                labels: props.labels,
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
                data: props.clicks,
            }, {
                name: "Миссклики",
                data: props.missclicks,
            }],
        };
    }

    componentDidUpdate(prevProps, prevState, snapshot) {

        console.log(this.current, [prevProps.clicks, prevProps.missclicks]);
        if (this.current !== [prevProps.clicks, prevProps.missclicks]) {
            this.current = [prevProps.clicks, prevProps.missclicks];
            this.setState(
                {
                    options:{
                        ...prevState.options,
                        labels: prevProps.labels,
                    },
                    series: [{
                        name: "Клики",
                        data: prevState.clicks,
                    }, {
                        name: "Миссклики",
                        data: prevState.missclicks,
                    }],
                }
            )
        }
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