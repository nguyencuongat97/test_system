function showRate(){
	 var sr = Highcharts.chart('te-view-chart', {
		chart: {
			type: 'column',
			id: 'te-view',
			style: {
                fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
            }
		},
		title: {
			text: '	'
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			categories: ['2019-01-10', '2019-01-11', '2019-01-12', '2019-01-13', '2019-01-14', '2019-01-15', '2019-01-16']
		},
		yAxis: {
			title: {
				text: 'Percent (%)'
			}
		},
		plotOptions: {
			line: {
				dataLabels: {
					enabled: false
				},
				enableMouseTracking: true
			}
		},
		series: [
		{
			name: 'PT',
			data: [5.88, 6.34, 3.15, 5.17, 4.36, 7.12, 6.21 ]
		},
		{
			name: 'FT1',
			data: [3.58, 5.34, 6.15, 5.57, 4.16, 5.88, 6.97 ]
		},
		{
			name: 'RC1',
			data: [4.34, 7.43, 6.51, 4.27, 5.63, 6.21, 5.11 ]
		},
		{
			name: 'Total',
			type: 'line',
			data: [13.80, 19.11, 11.81, 15.01, 14.15, 19.21, 18.29 ]
		}]
	});
}