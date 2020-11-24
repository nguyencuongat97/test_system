<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/onlineWip.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<link rel="stylesheet" href="/assets/css/custom/style.css" />

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span class="text-uppercase" name="factory">${factory}</span><Span id="teamS"></Span><span>-RE Repair
                    Engineer Management </span></b>
        </div>
        <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333;">
            <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                    class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                style="height: 26px; color: #fff;">
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-3">
                <div class="col-xs-12" style="padding: unset">
                    <div class="panel panel-flat panel-body chart-sm" id="pieChart1"></div>
                </div>
                <div class="col-xs-12" style="padding: unset">
                    <div class="panel panel-flat panel-body chart-sm" id="pieChart2"></div>
                </div>
            </div>
            <div class="col-xs-12 col-sm-9">
                <div class="panel panel-flat panel-body chart-xxl" id="columnChart1"></div>
            </div>
            <div class="col-xs-12 col-sm-6">
                <div class="panel panel-flat panel-body chart-md" id="columnChart2"></div>
            </div>
            <div class="col-xs-12 col-sm-6">
                <div class="panel panel-flat panel-body chart-md" id="columnChart3"></div>
            </div>
            <div class="col-xs-12 col-sm-12">
                <div class="panel panel-flat panel-body chart-md" id="lineChart1"></div>
            </div>
            <div class="col-xs-12 col-sm-12">
                <div class="panel panel-flat panel-body chart-md" id="lineChart2"></div>
            </div>
        </div>
    </div>
</div>

<script>
    var team = "-JBD";
    var dataset = {
        factory: '${factory}'
    }

    init();

    function init() {
        if (dataset.factory == "B04") {
            team = "-BBD";
        }
        $('#teamS').text(team)
        loadColumnChart1();
        loadColumnChartbyShift();
        loadDataLineChartCapacityUserByMonth();
    }

    function loadColumnChart1() {
        var data = {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan
        };
        $.ajax({
            type: 'GET',
            url: '/api/re/capacity/total',
            data: data,
            success: function (res) {
                var dataTop3SMT = [], dataTop3SI = [];
                var countTongSMT = 0, countTongSI = 0;
                var arrUser = new Array();
                var dataTotal = new Array();
                var dataSI = new Array();
                var dataSMT = new Array();
                var dataRCVI = new Array();
                for (var index = 0; index < res.length; index++) {
                    var element = res[index];
                    var tmpArr = Object.keys(element);
                    var ddd = tmpArr[0];
                    arrUser.push(ddd);
                    var tmpArrVal = new Array();
                    for (i in element) {
                        tmpArrVal.push(element[i]);
                    }
                    countTongSMT += tmpArrVal[0].smt;
                    countTongSI += tmpArrVal[0].si;
                    dataSI.push(tmpArrVal[0].si);
                    dataSMT.push(tmpArrVal[0].smt);
                    dataRCVI.push(tmpArrVal[0].rcvi);
                    dataTotal.push(tmpArrVal[0].rcvi + tmpArrVal[0].si + tmpArrVal[0].smt);
                    dataTop3SMT.push({ name: ddd, count: tmpArrVal[0].smt });
                    dataTop3SI.push({ name: ddd, count: tmpArrVal[0].si });
                }
                var dataChartPieTopSMT = [], dataChartPieTopSI = [];
                dataTop3SMT.sort(compare);
                dataTop3SI.sort(compare);
                var yt1 = parseFloat(((dataTop3SMT[0].count / countTongSMT) * 100).toFixed(1));
                var yt2 = parseFloat(((dataTop3SMT[1].count / countTongSMT) * 100).toFixed(1));
                var yt3 = parseFloat(((dataTop3SMT[2].count / countTongSMT) * 100).toFixed(1));
                var othert = parseFloat((100 - (yt1 + yt2 + yt3)).toFixed(1));
                dataChartPieTopSMT.push({ name: dataTop3SMT[0].name, y: yt1 });
                dataChartPieTopSMT.push({ name: dataTop3SMT[1].name, y: yt2 });
                dataChartPieTopSMT.push({ name: dataTop3SMT[2].name, y: yt3 });
                dataChartPieTopSMT.push({ name: "Other", y: othert });

                var y1 = parseFloat(((dataTop3SI[0].count / countTongSI) * 100).toFixed(1));
                var y2 = parseFloat(((dataTop3SI[1].count / countTongSI) * 100).toFixed(1));
                var y3 = parseFloat(((dataTop3SI[2].count / countTongSI) * 100).toFixed(1));
                var other = parseFloat((100 - (y1 + y2 + y3)).toFixed(1));
                dataChartPieTopSI.push({ name: dataTop3SI[0].name, y: y1 });
                dataChartPieTopSI.push({ name: dataTop3SI[1].name, y: y2 });
                dataChartPieTopSI.push({ name: dataTop3SI[2].name, y: y3 });
                dataChartPieTopSI.push({ name: "Other", y: other });
                //  console.log(dataSI, dataSMT, dataRCVI, dataTotal);
                hightChartTop3Pie("pieChart1", "TOP 3 ENGINEER  BY QUANTITY (SMT + PTH)", dataChartPieTopSMT);
                hightChartTop3Pie("pieChart2", "TOP 3 ENGINEER BY QUANTITY (Function)", dataChartPieTopSI);
                hightChartRepairTotal("columnChart1", '', arrUser, dataTotal, dataSI, dataSMT, dataRCVI);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function compare(a, b) {
        if (a.count < b.count) {
            return 1;
        }
        if (a.count > b.count) {
            return -1;
        }
        return 0;
    }

    function loadColumnChartbyShift() {
        // DATE TODAY   
        var curday = function (sp) {
            today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //As January is 0.
            var yyyy = today.getFullYear();
            if (dd < 10) dd = '0' + dd;
            if (mm < 10) mm = '0' + mm;
            return (yyyy + sp + mm + sp + dd);
        };
        var endDate = curday('/');
        // END DATE TODAY
        // 7 day ago
        var d = new Date();
        var pastDate = d.getDate() - 1;
        d.setDate(pastDate);
        var startDate = d.getFullYear().toString() + "/" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "/" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" + d.getDate().toString());
        // END 7 days ago
        var time = dataset.timeSpan;
        if (time == 'undefined' || time == null || time == '') {
            time = startDate + ' 07:30 - ' + endDate + ' 07:30';

        }
        // console.log(time)
        var data = {
            factory: dataset.factory,
            timeSpan: time
        };
        //  console.log(data);
        $.ajax({
            type: 'GET',
            url: '/api/re/capacity/datashift',
            data: data,
            success: function (responsive) {
                var arrUser = new Array();
                var dataTotal = new Array();
                var dataSI = new Array();
                var dataSMT = new Array();
                var dataRCVI = new Array();
                var res = responsive.dataDay;
                for (var index = 0; index < res.length; index++) {
                    var element = res[index];
                    var tmpArr = Object.keys(element);
                    var ddd = tmpArr[0];
                    arrUser.push(ddd);
                    var tmpArrVal = new Array();
                    for (i in element) {
                        tmpArrVal.push(element[i]);
                    }
                    dataSI.push(tmpArrVal[0].si);
                    dataSMT.push(tmpArrVal[0].smt);
                    dataRCVI.push(tmpArrVal[0].rcvi);
                    dataTotal.push(tmpArrVal[0].rcvi + tmpArrVal[0].si + tmpArrVal[0].smt)
                }
                hightChartRepairTotal("columnChart2", 'RE Repair Capacity by Day shift', arrUser, dataTotal, dataSI, dataSMT, dataRCVI);
                var arrUserNight = new Array();
                var dataTotalNight = new Array();
                var dataSINight = new Array();
                var dataSMTNight = new Array();
                var dataRCVINight = new Array();
                var resNight = responsive.dataNight;
                for (var index = 0; index < resNight.length; index++) {
                    var element = resNight[index];
                    var tmpArr = Object.keys(element);
                    var ddd = tmpArr[0];
                    arrUserNight.push(ddd);
                    var tmpArrVal = new Array();
                    for (i in element) {
                        tmpArrVal.push(element[i]);
                    }
                    dataSINight.push(tmpArrVal[0].si);
                    dataSMTNight.push(tmpArrVal[0].smt);
                    dataRCVINight.push(tmpArrVal[0].rcvi);
                    dataTotalNight.push(tmpArrVal[0].rcvi + tmpArrVal[0].si + tmpArrVal[0].smt)
                }
                hightChartRepairTotal("columnChart3", 'RE Repair Capacity by Night shift', arrUserNight, dataTotalNight, dataSINight, dataSMTNight, dataRCVINight);

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }


    function loadDataLineChartCapacityUserByMonth() {
        var data = {
            factory: dataset.factory,
        };
        $.ajax({
            type: 'GET',
            url: '/api/re/capacity/databymonth',
            data: data,
            success: function (responsive) {
                console.log("responsive:::", responsive);
                var dataName = [];
                var mData = [];
                for (var key in responsive.si) {
                    if (responsive.si.hasOwnProperty(key)) {
                        var element = responsive.si[key];
                        var tmpData = [];
                        var tmpArr = [];
                        //element.sort((a, b) => (a.name > b.name) ? 1 : -1);
                        for (var i = 0; i < element.length; i++) {
                            if (dataName.indexOf(element[i].name) == -1) {
                                dataName.push(element[i].name);
                            }
                            tmpData.push(element[i].qty * 1);
                        }
                        mData.push({ name: key, data: tmpData })
                    }
                }
                console.log(mData)
                //dataName.sort((a, b) => (a > b) ? 1 : -1);
                var dataNameSMT = [];
                var mDataSMT = [];
                for (var key in responsive.smt) {
                    if (responsive.smt.hasOwnProperty(key)) {
                        var element = responsive.smt[key];
                        var tmpArr = [];
                        //element.sort((a, b) => (a.name > b.name) ? 1 : -1);

                        for (var i = 0; i < element.length; i++) {
                            if (dataNameSMT.indexOf(element[i].name) == -1) {
                                dataNameSMT.push(element[i].name);
                            }
                            tmpArr.push(element[i].qty * 1);
                        }
                        mDataSMT.push({ name: key, data: tmpArr })
                    }
                }
                //dataNameSMT.sort((a, b) => (a > b) ? 1 : -1);
                highChartUserByMonth("lineChart1", "FUNCTION CAPACITY STATUS (by Month)", dataName, mData);
                highChartUserByMonth("lineChart2", "SMT and PTH CAPACITY STATUS (by Month)", dataNameSMT, mDataSMT);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }


    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    $(document).ready(function () {
        getTimeNow();
        $('input[name=timeSpan]').on('change', function () {
            dataset.timeSpan = this.value;
            init();
        });
    });


    //FUNCTION
    function highChartUserByMonth(idHtml, titleText, dataxAxis, data) {
        Highcharts.chart(idHtml, {

            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                },
                backgroundColor: 'rgba(39,39,39,0.7)'
            },
            title: {
                text: titleText,
                style: {
                    fontSize: '14px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                categories: dataxAxis,
                type: 'category',
                labels: {
                    enabled: true,
                    rotation: -40,
                    align: 'right',
                    style: {
                        fontSize: '11px'
                    }
                },
                gridLineWidth: 0,
                minorGridLineWidth: 0,
                tickLength: 0,
                tickWidth: 0,
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: 'blue'
                }],
                title: {
                    text: ''
                },
                gridLineWidth: 0,
                minorGridLineWidth: 0,
                tickLength: 0,
                tickWidth: 0,
                // offset: -320,
            },
            legend: {
                enabled: false,
                itemStyle: {
                    fontSize: '10px',
                },
                align: 'center',
                verticalAlign: 'bottom',
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    label: {
                        connectorAllowed: false
                    },
                    //   pointStart: 2010,
                    dataLabels: {
                        enabled: true,
                    }
                }
            },

            series: data,
            responsive: {
                rules: [{
                    condition: {
                        maxWidth: 500
                    },
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    }
                }]
            }

        });
    }

    function hightChartRepairTotal(idHtml, titleText, dataxAxis, dataTotal, dataSI, dataSMT, dataRCVI) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: titleText,
                style: {
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                categories: dataxAxis,
                labels: {
                    rotation: -90,
                    align: 'right',
                    style: {
                        fontSize: '12px'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: ''
                }
            },
            tooltip: {
                pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
                shared: true
            },
            plotOptions: {
                column: {
                    stacking: true,
                    borderWidth: 0
                },
                series: {
                    borderWidth: 0,
                    label: {
                        connectorAllowed: false
                    },
                    //   pointStart: 2010,
                    dataLabels: {
                        enabled: true,
                    }
                }

            },
            // plotOptions: {
            //     series: {
            //         borderWidth: 0,
            //         label: {
            //             connectorAllowed: false
            //         },
            //         //   pointStart: 2010,
            //         dataLabels: {
            //             enabled: true,
            //         }
            //     }
            // },
            legend: {
                itemStyle: {
                    fontSize: '10px',
                },
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },
            credits: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            series: [{
                name: 'TOTAL',
                data: dataTotal
            }, {
                name: 'SMT/PTH/OTHER',
                data: dataSMT
            }, {
                name: 'RCVI',
                data: dataRCVI
            }, {
                name: 'FUNCTION SI',
                data: dataSI
            }]
        });
    }

    function hightChartTop3Pie(idHtml, titleText, dataPie) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'pie',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: titleText,
                style: {
                    fontSize: '14px',
                    fontWeight: 'bold'
                }
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
                style: {
                    zindex: 9999
                }
            },
            legend: {
                verticalAlign: 'bottom'
            },
            credits: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            plotOptions: {
                pie: {
                    borderWidth: 0,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    showInLegend: false
                }
            },
            series: [{
                name: 'Brands',
                colorByPoint: true,
                data: dataPie
            }]
        });
    }
</script>