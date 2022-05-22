Apex.grid = {
  padding: {
    right: 0,
    left: 0
  }
}

Apex.dataLabels = {
  enabled: false
}


// the default colorPalette for this dashboard
//var colorPalette = ['#01BFD6', '#5564BE', '#F7A600', '#EDCD24', '#F74F58'];
var colorPalette = ['#00D8B6','#008FFB',  '#FEB019', '#FF4560', '#775DD0']

var spark1 = {
  chart: {
    id: 'sparkline1',
    group: 'sparklines',
    type: 'area',
    height: 160,
    sparkline: {
      enabled: true
    },
  },
  stroke: {
    curve: 'straight'
  },
  fill: {
    opacity: 1,
  },
  series: [{
    name: 'Время прохождения',
    data: [15, 35, 120, 25, 73]
  }],
  yaxis: {
    min: 0,
    labels: {
      formatter: function(val) {
        var hours = Math.floor(val / 60 / 60);
        var minutes = Math.floor(val / 60) - (hours * 60);
        var seconds = val % 60;
        var result = '';
        if (hours) {
          result+=hours.toString() + ' ч. ';
        }
        if (minutes) {
          result+=minutes.toString() + ' мин. ';
        }
        if (seconds) {
          result+=seconds.toString() + ' сек.';
        }
        return result;
      }
    }
  },
  xaxis: {
    categories: ['1 уровень', '2 уровень', '3 уровень', '4 уровень', '5 уровень']
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
}


new ApexCharts(document.querySelector("#spark1"), spark1).render();


var optionsBar = {
  chart: {
    type: 'bar',
    height: 380,
    width: '100%',
    stacked: true,
  },
  plotOptions: {
    bar: {
      columnWidth: '45%',
    }
  },
  colors: colorPalette,
  series: [{
    name: "Клики",
    data: [13, 12, 17, 10, 5],
  }, {
    name: "Миссклики",
    data: [6, 5, 1, 2, 0],
  }],
  labels: ['1 уровень', '2 уровень', '3 уровень', '4 уровень', '5 уровень'],
  xaxis: {
    labels: {
      show: false
    },
    axisBorder: {
      show: false
    },
    axisTicks: {
      show: false
    },
  },
  yaxis: {
    axisBorder: {
      show: false
    },
    axisTicks: {
      show: false
    },
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

}

var chartBar = new ApexCharts(document.querySelector('#bar'), optionsBar);
chartBar.render();



var answersBarParam = {
  chart: {
    type: 'bar',
    height: 380,
    width: '100%',
    stacked: true,
  },
  plotOptions: {
    bar: {
      columnWidth: '45%',
    }
  },
  colors: [colorPalette[0], '#FF4560'],
  series: [{
    name: "Правильные ответы",
    data: [1, 2, 1, 1, 1],
  }, {
    name: "Неправильные ответы",
    data: [2, 0, 0, 1, 0],
  }],
  labels: ['1 уровень', '2 уровень', '3 уровень', '4 уровень', '5 уровень'],
  xaxis: {
    labels: {
      show: false
    },
    axisBorder: {
      show: false
    },
    axisTicks: {
      show: false
    },
  },
  yaxis: {
    axisBorder: {
      show: false
    },
    axisTicks: {
      show: false
    },
    labels: {
      style: {
        colors: '#78909c'
      }
    }
  },
  title: {
    text: 'Анализ ответов',
    align: 'left',
    style: {
      fontSize: '18px'
    }
  }

}

var answersBar = new ApexCharts(document.querySelector('#donut'), answersBarParam);
answersBar.render();



var options = {
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
  chart: {
  height: 350,
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
  colors: colorPalette,
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
};

var timeline = new ApexCharts(document.querySelector("#timeline"), options);
timeline.render();



