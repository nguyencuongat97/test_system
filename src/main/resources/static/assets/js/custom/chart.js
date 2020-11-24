function initGraph(graphProps) {
    var graph = Highcharts.chart(graphProps.id, {
        chart: {
            style: {
                fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
            }
        },

        title: {
            text: graphProps.title
        },

        tooltip: {
            pointFormat: graphProps.tooltipFormat,
            valueSuffix: graphProps.unit
        },

        yAxis: [{
            title: {
                text: ''
            },
            labels: {
                style: {
                    fontSize: '10px'
                },
            }
        },{
            title: {
                text: ''
            },
            labels: {
                style: {
                    fontSize: '10px'
                },
            }
        }],

        xAxis: {
            type: 'category',
            labels: {
                autoRotation: false,
                style: {
                    fontSize: '10px'
                },
            }
        },

        series: [{
            label: {
                enabled: false
            },
            name: graphProps.name,
            type: graphProps.type,
        }, {}, {}, {}, {}]
    });

    return graph;
}
