function drawCpkChart (chartId, requestData) {
    $('.loader').css('display', 'block');
    $.ajax({
        type: 'GET',
        url: '/api/test/station/cpkOfParameterByDay',
        data: requestData,
        success: function(data){
            var time = Object.keys(data);
            var cpk =[];
            for(var i in time){
                if(data[time[i]]!=null){
                    cpk[i] = data[time[i]];
                }
            }

            Highcharts.chart(chartId, {
                chart: {
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },

                title: {
                    text: dataset.modelName + ' CPK'
                },

                yAxis: {
                    title: {
                        text: ''
                    },
                    plotLines: [{
                        value: 1.33,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                        label: {
                            text: '1.33',
                            align: 'left',
                        }
                    }],
                    softMax: 1.33,
                    softMin: 0
                },

                xAxis: {
                    categories: time,
                    labels: {
                        autoRotation: false,
                        style: {
                            fontSize: '10px'
                        },
                    },
                    tickInterval: Math.ceil(time.length/7)
                },

                series: [{
                    label: {
                        enabled: false
                    },
                    name: 'CPK',
                    type: 'line',
                    data: cpk
                }],

                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                }
            });
        }
   });
}

function drawSpcChart(chartId, lowSpec, highSpec, requestData) {
    $('.loader').css('display', 'block');
    $.ajax({
        type: 'GET',
        url: '/api/test/station/valueOfParameter',
        data: requestData,
        success: function(data){
            var cpk = 0.0000;
            var values = [];

            if (!$.isEmptyObject(data)) {
                cpk = data.cpk;
                values = data.values;
            }

            var chart = Highcharts.chart(chartId, {
                chart: {
                    zoomType: 'x',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },

                title: {
                    text: ' SPC CPK'
                },

                subtitle: {
                    text: 'CPK = ' + cpk.toFixed(4) + '    TOTAL SAMPLE = ' + values.length
                },

                tooltip: {
                    valueDecimals: 4
                },

                xAxis: [{
                    title: { text: '' },
                    alignTicks: false,
                    tickInterval: 1,
                    tickPositions: [values.length > 0 ? values.length-1 : 0]
                }, {
                    title: { text: '' },
                    alignTicks: false,
                    opposite: true,
                    visible: false
                }, {
                    title: { text: '' },
                    alignTicks: false,
                    opposite: true,
                    softMax: highSpec,
                    softMin: lowSpec,
                    tickPositions: [lowSpec, highSpec],
                    plotLines: [{
                        value: highSpec,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                    },{
                        value: lowSpec,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                    }]
                }],

                yAxis: [{
                    title: { text: '' },
                    alignTicks: false,
                    softMax: highSpec,
                    softMin: lowSpec,
                    tickPositions: [lowSpec, highSpec],
                    plotLines: [{
                        value: highSpec,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                    },{
                        value: lowSpec,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                    }]
                }, {
                    title: { text: '' },
                    opposite: true,
                    labels: {
                        enabled: false
                    }
                }, {
                    title: { text: '' },
                    opposite: true,
                    labels: {
                        enabled: false
                    }
                }],

                series: [values.length > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Quantity',
                    type: 'histogram',
                    xAxis: 2,
                    yAxis: 2,
                    baseSeries: 's1',
                    zIndex: -1,
                    tooltip: {
                        valueDecimals: 0
                    },
                } : {}, values.length > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Bell curve',
                    type: 'bellcurve',
                    xAxis: 1,
                    yAxis: 1,
                    baseSeries: 's1',
                    zIndex: -2
                } : {}, values.length > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Data',
                    type: 'line',
                    id: 's1',
                    data: values,
                    visible: false
                }: {}],

                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                }
            });
           
        },
        complete: function(){
            $('.loader').css('display', 'none');
        }
    });
    //$('.loader').css('display', 'none');

}
