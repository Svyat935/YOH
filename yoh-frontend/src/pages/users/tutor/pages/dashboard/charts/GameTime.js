import React, {Component} from "react";
import Chart from "react-apexcharts";

export class GameTime extends Component {
    constructor(props) {
        super(props);
        let date_range = [];
        props.dateRange.forEach((datePart) => {
            let start_date = new Date(datePart['daterange'][0]).getTime(),
                end_date = new Date(datePart['daterange'][1]).getTime();

            date_range.push(
                {
                    x: datePart['level_name'],
                    y: [start_date, end_date]
                }
            )
        })

        this.current = JSON.stringify(date_range);
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
                    data: date_range
                }
            ],
        };
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        let date_range = [];
        prevProps.dateRange.forEach((datePart) => {
            let start_date = new Date(datePart['daterange'][0]).getTime(),
                end_date = new Date(datePart['daterange'][1]).getTime();

            date_range.push(
                {
                    x: datePart['level_name'],
                    y: [start_date, end_date]
                }
            )
        })

        if (this.current !== JSON.stringify(date_range)) {
            this.current = JSON.stringify(date_range);
            this.setState({
                options:{...prevState.options},
                series: [{data: date_range}]
            });
        }
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