<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<link rel="stylesheet" href="/assets/css/custom/style.css" />
<style>
    #Result8sTable {
        /* table-layout: fixed; */
        /* word-wrap: break-word; */
        text-align: center;
        table-layout: fixed;
        max-width: 15000px;
    }

    #Result8sTable tr:hover {
        background: rgba(0, 0, 0, 0.5);
    }

    .goodbad {
        height: 297px !important;
        background: #2C2C2D;
        color: #fff;
    }

    .goodbad2 {
        background: #272727;
        height: 297px !important;
        color: #fff;
        padding: 0px 0px 0px 10px;
    }


    #Result8sTable tr td:nth-of-type(1) {
        position: -webkit-sticky;
        position: sticky;
        left: -1px;
        background-color: #272727;
    }

    #Result8sTable tr td:nth-of-type(2) {
        position: -webkit-sticky;
        position: sticky;
        left: 43px;
        background-color: #272727;
    }

    #Result8sTable tr td:nth-of-type(3) {
        position: -webkit-sticky;
        position: sticky;
        left: 212px;
        background-color: #272727;
    }

    #Result8sTable tr td:nth-of-type(4) {
        position: -webkit-sticky;
        position: sticky;
        left: 306px;
        background-color: #272727;
    }

    #Result8sTable tr td:nth-of-type(4) {
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #fff,
            inset -1px 0 0 #fff;
    }

    #Result8sTable tr td:nth-of-type(3) {
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #fff,
            inset -1px 0 0 #fff;
    }

    #Result8sTable tr td:nth-of-type(2) {
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #fff,
            inset -1px 0 0 #fff;
    }

    #Result8sTable tr td:nth-of-type(1) {
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 2px 0 0 #fff,
            inset -1px 0 0 #fff;
    }

    #Result8sTable tr td:nth-of-type(5),
    #Result8sTable tr th:nth-of-type(5) {
        border-left: none !important;
    }

    #Result8sTable tr th:nth-of-type(1) {
        left: -1px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 2px 1px 0 #fff,
            inset -1px -1px 0 #fff;
    }

    #Result8sTable tr th:nth-of-type(2) {
        left: 43px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 1px 1px 0 #fff,
            inset -1px -1px 0 #fff;
    }

    #Result8sTable tr th:nth-of-type(3) {
        left: 212px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 1px 1px 0 #fff,
            inset -1px -1px 0 #fff;
    }

    #Result8sTable tr th:nth-of-type(4) {
        left: 306px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 1px 1px 0 #fff,
            inset -1px -1px 0 #fff;
    }

    .table caption {
        text-align: center;
        font-size: 17px;
        color: #ffffff;
        font-weight: bold;
        padding-top: 1px;
    }
</style>

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span>B04-RE Result For 8S</span></b>
        </div>
        <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333;">
            <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                    class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                style="height: 26px; color: #fff;">
        </div>
        <div class="row" style="margin: unset; background: #272727;">
            <div class="panel col-xs-12 col-sm-3 goodbad topGood">
                <table class="table table-bordered" id="tblTop5Good" style="text-align: center;">
                    <caption>TOP 5 ENGINEER GOOD </caption>
                    <thead>
                        <tr id="item">
                            <th style="text-align: center">#</th>
                            <th style="text-align: center">Name</th>
                            <th style="text-align: center">Good</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="panel col-xs-12 col-sm-9 goodbad2">
                <div id="chartChecklistControl" style="height:297px"></div>
            </div>
        </div>
        <div class="row" style="margin: unset;background: #272727;">
            <div class="panel col-xs-12 col-sm-3 goodbad topBad">
                <table class="table table-bordered" id="tblTop5Bad" style="text-align: center">
                    <caption style="text-align: center;font-size: 17px;color: #ffffff;">TOP 5 ENGINEER BAD </caption>
                    <thead>
                        <tr id="item">
                            <th style="text-align: center">#</th>
                            <th style="text-align: center">Name</th>
                            <th style="text-align: center">Bad</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="panel col-xs-12 col-sm-9 goodbad2">
                <div id="chartChecklistHighlight" style="height:297px"></div>
            </div>
        </div>
        <div class="row">
            <div class="table-responsive pre-scrollable col-md-12"
                style="max-height: 350px; color: #ccc; padding: unset;">
                <table id="Result8sTable" class="table table-xxs table-bordered table-sticky">
                    <thead>

                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="export pull-right" style="margin: 10px 10px 0px 0px;">
                <a class="btn btn-lg" id="btnExport"
                    style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
    </div>

    <iframe id="txtArea1" style="display:none"></iframe>

    <script>
        init();
        function init() {
            loadChart();
            loadTableData();
        }

        function loadChart() {
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
            var pastDate = d.getDate() - 7;
            d.setDate(pastDate);
            var startDate = d.getFullYear().toString() + "/" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "/" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" + d.getDate().toString());
            // END 7 days ago
            var data = {
                startDate: startDate + " 07:30",
                endDate: endDate + " 07:30"
            }
            console.log("data:", data)
            $.ajax({
                type: 'GET',
                url: '/api/re/8s/checklist/report',
                data: data,
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    console.log("data::", data)
                    if (!$.isEmptyObject(data)) {
                        var dataChart = new Array();
                        for (i in data["checklistControl"]) {
                            dataChart[i] = {
                                name: data["checklistControl"][i].fullName,
                                y: data["checklistControl"][i].formsCount
                            }
                            dataChart.push({ name: data["checklistControl"][i].fullName, y: data["checklistControl"][i].formsCount })
                        }
                        var dataBad = new Array();
                        for (i in data["topbad"]) {
                            dataBad[i] = {
                                name: data["topbad"][i].fullName,
                                y: data["topbad"][i].badCount
                            };
                        }
                        var html = '';
                        for (i in data["topGood"]) {
                            let j = i * 1 + 1;
                            html += '<tr><td>' + j + '</td>' +
                                '<td>' + data["topGood"][i].fullName + '</td>' +
                                '<td>' + data["topGood"][i].goodCount + '</td></tr>';
                            if (i == 4) {
                                break;
                            }
                        }
                        $('#tblTop5Good>tbody').html(html);
                        var ml = '';
                        for (i in data["topbad"]) {
                            let j = i * 1 + 1;
                            ml += '<tr><td>' + j + '</td>' +
                                '<td>' + data["topbad"][i].fullName + '</td>' +
                                '<td>' + data["topbad"][i].badCount + '</td></tr>';
                            if (i == 4) {
                                break;
                            }
                        }
                        $('#tblTop5Bad>tbody').html(ml);
                        chartChecklistControl(dataChart);
                        chartChecklistHighlight(dataBad);
                    }
                },
                error: function () {
                    alert("Fail!");
                }
            });
        }

        function loadTableData() {
            $.ajax({
                type: 'GET',
                url: '/api/re/8s/checklist/status',
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    console.log("data:::", data)
                    if (!$.isEmptyObject(data)) {
                        var dt = data.data;
                        $('#Result8sTable').html('');
                        var thead = '<thead><tr><th style="width:50px; text-align:center;">No</th><th style="width:170px;text-align:center;">Name</th><th style="width:95px;text-align:center;">ID Num</th><th style="width:72px;text-align:center;">Location</th>';

                        var header = [];
                        for (i in dt[0].statusList) {
                            header.push(dt[0].statusList[i].date.slice(5, 10) + '_' + dt[0].statusList[i].shiftType);
                        }

                        for (i in header) {
                            thead += '<th style="width:100px;text-align:center;">' + header[i] + '</th>';
                        }
                        thead += '</tr></thead>';
                        $('#Result8sTable').append(thead);

                        var tbody = '<tbody>';
                        for (i in dt) {
                            tbody += '<tr><td>' + (Number(i) + 1) + '</td><td style="width:150px"><span style="width:180px">' + dt[i].fullName + '</span></td><td>' + dt[i].userId + '</td><td>' + dt[i].location + '</td>';
                            for (j in dt[i].statusList) {
                                if (dt[i].statusList[j].status == "3") {
                                    tbody += '<td bgcolor="#ED030A"> </td>';
                                } else if (dt[i].statusList[j].status == "2") {
                                    tbody += '<td bgcolor="#ffaa00"> </td>';
                                } else if (dt[i].statusList[j].status == "0") {
                                    tbody += '<td bgcolor="#008000"> </td>';
                                } else tbody += '<td> </td>';
                            }
                            tbody += '</tr>';
                        }
                        tbody += '</tbody>';
                        $('#Result8sTable').append(tbody);
                    }
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
            if (isIE) {
                var tab_text = "<table border='2px'><tr bgcolor='#F79646'>";
                var textRange; var j = 0;
                tab = document.getElementById('Result8sTable'); // id of table

                for (j = 0; j < tab.rows.length; j++) {
                    tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
                    //tab_text=tab_text+"</tr>";
                }

                tab_text = tab_text + "</table>";
                tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
                tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
                tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

                var ua = window.navigator.userAgent;
                var msie = ua.indexOf("MSIE ");

                if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
                {
                    txtArea1.document.open("txt/html", "replace");
                    txtArea1.document.write(tab_text);
                    txtArea1.document.close();
                    txtArea1.focus();
                    sa = txtArea1.document.execCommand("SaveAs", true, "Result For 8s.xls");
                }
                else                 //other browser not tested on IE 11
                    sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
                return (sa);
            }
            else {
                var blobURL = tableToExcel('Result8sTable', 'checklist_table');
                $(this).attr('download', 'Result For 8s.xls')
                $(this).attr('href', blobURL);
            }
        });


        $(document).ready(function () {
            var spcTS = getTimeSpan();
            $('.datetimerange').data('daterangepicker').setStartDate(spcTS.startDate);
            $('.datetimerange').data('daterangepicker').setEndDate(spcTS.endDate);

            $('input[name=timeSpan]').on('change', function () {
                dataset.timeSpan = this.value;
                init();
            });
        });

        function chartChecklistHighlight(dataBad) {
            Highcharts.chart('chartChecklistHighlight', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    }
                },
                title: {
                    text: '8S - Hightlight issue by week',
                    style: {
                        fontSize: '16px',
                        fontWeight: 'bold'
                    }
                },
                xAxis: {
                    type: 'category',
                    allowDecimals: false,
                },
                plotOptions: {
                    series: {
                        borderWidth: 0,
                        dataLabels: {
                            enabled: true
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
                    }
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
                },
                legend: {
                    enabled: false
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
                    name: ' ',
                    data: dataBad
                }]
            }); /// END Chart 2
        } // END FUNCITON

        function chartChecklistControl(dataChart) {
            Highcharts.chart('chartChecklistControl', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    }
                },
                title: {
                    text: '8S - Checklist control by week',
                    style: {
                        fontSize: '16px',
                        fontWeight: 'bold'
                    }
                },
                xAxis: {
                    type: 'category',
                    allowDecimals: false,
                },
                plotOptions: {
                    series: {
                        borderWidth: 0,
                        dataLabels: {
                            enabled: true
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
                    }
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
                },
                legend: {
                    enabled: false
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
                    name: ' ',
                    data: dataChart
                }]
            }); // ENd 1
        } // END FUNCTION


    </script>