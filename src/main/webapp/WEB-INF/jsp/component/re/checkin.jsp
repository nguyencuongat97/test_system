<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/checkIn.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />

<div class="panel panel-re panel-flat row">
    <div class="panel panel-overview" id="header">
        <b><span>B04-BBD-RE Repair Check In Management</span></b>
    </div>
    <div class="panel panel-overview input-group" style="margin-bottom: 5px; padding-left: 10px; background: #333;">
        <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
        <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
            style="height: 26px; color: #fff;">
    </div>
    <div class="row" style="margin: unset">
        <div class="col-xs-12 col-sm-3 infchart">
            <div class="panel panel-flat panel-body chart" id="repairCheckin"></div>
        </div>
        <div class="col-xs-12 col-sm-6 infchart">
            <div class="panel panel-flat panel-body chart" id="drilldown_chart"></div>
        </div>
        <div class="col-xs-12 col-sm-3 infchart">
            <div class="panel panel-flat panel-body chart" id="trending"></div>
        </div>
    </div>
    <div class="row" style="padding: 15px;">
        <div class="table-responsive pre-scrollable col-md-12" style="max-height: auto; color: #ccc;">
            <table id="checkinTable" class="table table-xxs table-bordered table-sticky">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Model</th>
                        <th>Station</th>
                        <th>MO</th>
                        <th>MAC</th>
                        <th>Error code</th>
                        <th>FA</th>
                        <th>Input time</th>
                        <th>Output time</th>
                        <th>Owner</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <iframe id="txtArea1" style="display:none"></iframe>
        <div class="export pull-right" style="margin: 10px 10px 0px 0px;">
            <a class="btn btn-lg" id="btnExport"
                style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                <i class="fa fa-download"></i> EXPORT
            </a>
        </div>
    </div>
</div>
<style>
    #repairCheckin .highcharts-container .highcharts-button .highcharts-button-box {
        display: none;
    }
</style>
<script>
    var dataset = {
        factory: 'B04'
    }

    //$('input[name="count"]').val("4390");

    init();
    function init() {
        loadRepairCheckin();
        loadTableData();
    }

    function loadRepairCheckin() {
        $.ajax({
            type: "GET",
            url: "/api/re/input/section",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var drilldown = new Array(keys.length);

                for (i in keys) {
                    var modelMap = data[keys[i]].data;
                    if (modelMap.length < 15) {
                        var drillData = new Array(modelMap.length);
                        for (j in modelMap) {
                            drillData[j] = {
                                name: modelMap[j].key,
                                y: modelMap[j].value
                            }
                        }
                    }
                    else {
                        var drillData = new Array();
                        for (j = 0; j < 15; j++) {
                            drillData[j] = {
                                name: modelMap[j].key,
                                y: modelMap[j].value
                            }
                        }
                    }

                    dataChart[i] = {
                        name: keys[i],
                        y: data[keys[i]].size,
                        drilldown: (modelMap.length > 0 ? keys[i] : null)
                    }

                    drilldown[i] = {
                        name: keys[i],
                        id: keys[i],
                        data: drillData
                    };
                }
                Highcharts.chart('repairCheckin', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: ' ',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        },
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var section_name = this.name;
                                        loadDrillDownChart(section_name, drilldown);
                                    }
                                }
                            }

                        },
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                    },
                    legend: {
                        enabled: false
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
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
                    }],
                });
                loadDrillDownChart(drilldown[0].name, drilldown);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadDrillDownChart(section_name, drilldown) {
        var drillData;
        for (i in drilldown) {
            if (drilldown[i].name == section_name) {
                drillData = drilldown[i].data;
            }
        }

        Highcharts.chart('drilldown_chart', {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: ' ',
                style: {
                    color: '#ccc'
                }
            },
            xAxis: {
                type: 'category',
            },
            yAxis: {
                min: 0,
                title: {
                    text: ' '
                },
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                    },
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function (event) {
                                var model_name = this.name;
                                loadPiechart(model_name)
                            }
                        }
                    }

                },
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
            },
            legend: {
                enabled: false
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
                name: ' ',
                colorByPoint: true,
                data: drillData
            }]
        });
        loadPiechart(drillData[0].name);
    }

    function loadPiechart(model_name) {
        $.ajax({
            type: 'GET',
            url: '/api/re/under-repair/model',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                modelName: model_name
            },
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array();
                var categories = new Array();
                if (keys.length < 15) {
                    for (i in keys) {
                        categories[i] = keys[i];
                        dataChart[i] = { name: keys[i], y: data[keys[i]] }
                    }
                }
                else {
                    for (i = 0; i < 15; i++) {
                        categories[i] = keys[i];
                        dataChart[i] = { name: keys[i], y: data[keys[i]] }
                    }
                }

                Highcharts.chart('trending', {
                    chart: {
                        type: 'pie',
                        options3d: {
                            enabled: true,
                            alpha: 45,
                            beta: 0
                        }
                    },
                    title: {
                        text: model_name,
                        style: {
                            fontSize: '15px',
                            fontWeight: 'bold',
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            depth: 35,
                            dataLabels: {
                                enabled: false
                            },
                            showInLegend: true
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '11px',
                            fontWeight: '100'
                        },
                        layout: 'horizontal',
                        align: 'left',
                        verticalAlign: 'bottom'
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                        data: dataChart
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }


    function loadTableData() {
        $.ajax({
            type: 'GET',
            url: '/api/re/input/data',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('#checkinTable>tbody>tr').remove();
                var html = '';
                if (!$.isEmptyObject(data)) {
                    var keys = data.data;
                    var dt_length = keys.length - 1;
                    var dt_minsize;
                    if (dt_length >= 10) {
                        dt_minsize = dt_length - 10;
                    }
                    else {
                        dt_minsize = 0;
                    }
                    var number = 0;
                    for (i = dt_length; i > dt_minsize; i--) {
                        for (j in keys[i]) {
                            if (keys[i][j] == null) {
                                keys[i][j] = " ";
                            }
                        }
                        number++;
                        html += '<tr class="bg-secondary tbRow" role="row" style="border-color: #2E9AFE;">' +
                            '<td name="no" tabindex="0" rowspan="1" colspan="1">' + number + '</td>' +
                            '<td name="model" tabindex="0" rowspan="1" colspan="1">' + keys[i].modelName + '</td>' +
                            '<td name="station" tabindex="0" rowspan="1" colspan="1">' + keys[i].stationName + '</td>' +
                            '<td name="mo" tabindex="0" rowspan="1" colspan="1">' + keys[i].mo + '</td>' +
                            '<td name="mac" tabindex="0" rowspan="1" colspan="1">' + keys[i].serial + '</td>' +
                            '<td name="errorCode" tabindex="0" rowspan="1" colspan="1">' + keys[i].errorCode + '</td>' +
                            '<td name="fa" tabindex="0" rowspan="1" colspan="1">' + keys[i].remark + '</td>' +
                            '<td name="inputTime" tabindex="0" rowspan="1" colspan="1">' + keys[i].inputTime + '</td>' +
                            '<td name="outputTime" tabindex="0" rowspan="1" colspan="1">' + keys[i].outputTime + '</td>' +
                            '<td name="owner" tabindex="0" rowspan="1" colspan="1">' + keys[i].owner + '</td>' +
                            '</tr>';
                    }
                } else {
                    $('#checkinTable>tbody').append('<tr><td colspan="10" align="center">-- NO DATA --</td></tr>');
                }
                $('#checkinTable>tbody').append(html);
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    var tableToExcel = (function () {
        var uri = 'data:application/vnd.ms-excel;base64,'
            , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
            , base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) }
            , format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) }
        return function (table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = { worksheet: name || 'Worksheet', table: table.innerHTML }
            var blob = new Blob([format(template, ctx)]);
            var blobURL = window.URL.createObjectURL(blob);
            return blobURL;
        }
    })();
    $("#btnExport").click(function () {
        var isIE = /*@cc_on!@*/false || !!document.documentMode;
        if(isIE){
            var tab_text="<table border='2px'><tr bgcolor='#F79646'>";
            var textRange; var j=0;
            tab = document.getElementById('checkinTable'); // id of table

            for(j = 0 ; j < tab.rows.length ; j++)
            {
                tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
                //tab_text=tab_text+"</tr>";
            }

            tab_text=tab_text+"</table>";
            tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
            tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
            tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

            var ua = window.navigator.userAgent;
            var msie = ua.indexOf("MSIE ");

            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
            {
                txtArea1.document.open("txt/html","replace");
                txtArea1.document.write(tab_text);
                txtArea1.document.close();
                txtArea1.focus();
                sa=txtArea1.document.execCommand("SaveAs",true,"Top_10.xls");
            }
            else                 //other browser not tested on IE 11
            sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        }
        else{
            var blobURL = tableToExcel('checkinTable', 'test_table');
            $(this).attr('download', 'Top_10.xls')
            $(this).attr('href', blobURL);
        }

    });

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    $(document).ready(function () {
        // var spcTS = getTimeSpan();
        // $('.datetimerange').data('daterangepicker').setStartDate(spcTS.startDate);
        // $('.datetimerange').data('daterangepicker').setEndDate(spcTS.endDate);
        getTimeNow();

        $('input[name=timeSpan]').on('change', function () {
            dataset.timeSpan = this.value;
            init();
        });
    });
</script>