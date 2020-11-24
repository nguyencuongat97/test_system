<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/pareto.js"></script>
<style>
    .table {
        margin-bottom: 10px;
        margin-top: 5px;
        /* width: auto; */
    }
    
    .table tr th {
        background-color: #F79646;
        color: #272727;
        text-align: center;
    }
    
    .table tr th,
    .table tr td {
        font-size: 16px;
    }
    
    .rowLine {
        background-color: #92D050;
        color: #333;
    }
    
    .paretoChart {
        padding: 0px;
        background: #333;
        height: calc(100vh - 190px);
        border: solid 1px rgba(0, 0, 0, 0.2);
    }
</style>

<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span class="text-uppercase">${factory}</span><span> TEST ERROR OVERALL </span><span id="titlePage"></span> <span id="txtDateTime"></span></b>
        </div>
        <div class="row no-margin no-padding">
            <div class="col-md-3 no-padding">
                <div class="col-xs-4 no-padding">
                    <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                        <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                        <input type="text" class="form-control daterange-single" side="right" name="timeSpan" style="border-bottom: 0px !important; color: #fff;">
                    </div>
                </div>

                <div class="col-xs-8 btn-group" style="margin-bottom: 5px;">
                    <button id="a-shift" class="btn btn-default select-shift selected" onclick="loadDataTypeShift('all')">All</button>
                    <button id="d-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('day-shift')">Day</button>
                    <button id="n-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('night-shift')">Night</button>
                </div>
            </div>
            <div class="col-md-4 no-padding">
                <div class='col-xs-3 selectCustomer' style="margin-bottom: 5px;">
                    <div class="dropdown dropdown-select-customer">
                        <button class="btn dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                            Select Customer
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer">

                        </ul>
                    </div>
                </div>
                <div class='col-xs-4 selectStage' style="margin-bottom: 5px;">
                    <div class="dropdown dropdown-select-stage">
                        <button class="btn dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                            Select Stage
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage">

                        </ul>
                    </div>
                </div>

                <div class='col-xs-5 selectModel' style="margin-bottom: 5px;">
                    <div class="dropdown dropdown-select-model">
                        <button class="btn dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                            Model Name
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel">

                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-md-3 no-padding">
                <div class='col-md-6 col-xs-4 selectLine'>
                    <div class="dropdown dropdown-select">
                        <button class="btn dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
                            Line Name
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu select-line table-responsive pre-scrollable" role="menu" aria-labelledby="dropdownMenu1">

                        </ul>
                    </div>
                </div>

                <div class='col-md-6 col-xs-4 selectMO'>
                    <div class="dropdown dropdown-select-mo">
                        <button class="btn dropdown-toggle" type="button" id="drlSelectMO" data-toggle="dropdown" aria-expanded="true">
                            MO Number
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu select-mo table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectMO">

                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-md-2">
                <div class="export pull-right">
                    <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                        <i class="fa fa-download"></i> EXPORT
                    </a>
                </div>
            </div>
        </div>
        <div id="content-detail" class="row no-margin">
        </div>
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>

<script type="text/javascript" src="/assets/custom/js/nbb/loadSelection.js"></script>
<script type="text/javascript" src="/assets/custom/js/nbb/getTimeNow.js"></script>
<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if (dataset == null || dataset == undefined || dataset.path != "${path}") {
        dataset = {
            shiftType: "",
            customer: "Vento",
            stage: "SMA",
            lineName: "",
            mo: "",
            modelName: "",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }

    var dataModelNames = [];
    var dataGroupNames = [];

    function init() {
        loadSelectCustomer();
        loadSelectStage();
        loadSelectModel();
        loadSelectLine();
        loadSelectMO();
        loadDataTypeShift(dataset.shiftType);
        $('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        if (dataset.stage == "" || dataset.stage == undefined) {
            $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        } else $('.dropdown-select-stage').find('.dropdown-toggle').html(dataset.stage + ' <span class="caret"></span>');

        if (dataset.modelName == "" || dataset.modelName == undefined) {
            $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        } else $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');

        if (dataset.mo == "" || dataset.mo == undefined) {
            $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        } else $('.dropdown-select-mo').find('.dropdown-toggle').html(dataset.mo + ' <span class="caret"></span>');

        if (dataset.lineName == "" || dataset.lineName == undefined) {
            $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        } else $('.dropdown-select').find('.dropdown-toggle').html(dataset.lineName + ' <span class="caret"></span>');
    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        dataset.customer = $(this).html();
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        dataset.lineName = "";
        dataset.mo = "";
        $('.selectLine').removeClass("disableSelect");
        $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        loadSelectLine();
        $(".loader").removeClass("disableSelect");
        loadData();
        $('.selectStage').removeClass("disableSelect");
        $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        loadSelectStage();
        $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        loadSelectModel();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    $('.dropdown-select').on('click', '.select-line li a', function() {
        var target = $(this).html();
        dataset.lineName = $(this).attr("name");
        $(this).parents('.select-line').find('li').removeClass('active');
        $(this).parent('li').addClass('active');
        $(this).parents('.dropdown-select').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        dataset.mo = "";
        $('.selectMO').removeClass("disableSelect");
        $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        loadSelectMO();
        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    $('.dropdown-select-stage').on('click', '.select-stage li a', function() {
        var value = $(this).html();
        if (value == "Select All")
            dataset.stage = "";
        else dataset.stage = value;
        $(this).parents('.select-stage').find('li').removeClass('active');
        $(this).parent('li').addClass('active');
        $(this).parents('.dropdown-select-stage').find('.dropdown-toggle').html(value + ' <span class="caret"></span>');
        dataset.modelName = "";
        $(".loader").removeClass("disableSelect");
        loadData();

        $('.selectModel').removeClass("disableSelect");
        $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        loadSelectModel();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    $('.dropdown-select-model').on('click', '.select-model li a', function() {
        var value = $(this).html();
        if (value == "Select All")
            dataset.modelName = "";
        else dataset.modelName = value;
        $(this).parents('.select-model').find('li').removeClass('active');
        $(this).parent('li').addClass('active');
        $(this).parents('.dropdown-select-model').find('.dropdown-toggle').html(value + ' <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    $('.dropdown-select-mo').on('click', '.select-mo li a', function() {
        var target = $(this).html();
        if (target == "Select All") {
            dataset.mo = "";
        } else dataset.mo = target;
        $(this).parents('.select-mo').find('li').removeClass('active');
        $(this).parent('li').addClass('active');
        $(this).parents('.dropdown-select-mo').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    function loadDataTypeShift(typeShift) {
        if (typeShift == "all" || typeShift == "") {
            dataset.shiftType = "";
            $("#a-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#n-shift").removeClass("selected");
        } else if (typeShift == "night-shift" || typeShift == "NIGHT") {
            dataset.shiftType = "NIGHT";
            $("#n-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
        } else {
            dataset.shiftType = "DAY";
            $("#d-shift").addClass("selected");
            $("#n-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
        }
        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        var getD = moment(dataset.workDate).format("MM/DD");
        $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
    }

    function loadData() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/error/overall",
            data: {
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
                lineName: dataset.lineName,
                customer: dataset.customer,
                mo: dataset.mo,
                stage: dataset.stage,
                modelName: dataset.modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#content-detail").html("");
                var dataStage = Object.keys(data);
                var dataChart = new Array();
                var categories = new Array();
                var html = '';
                for (i in dataStage) {
                    var stt = 0;
                    html = '<div class="row no-margin"><div class="col-sm-6 col-xs-12 table-responsive pre-scrollable no-padding" style="max-height: calc(100vh - 190px);color: #ccc;">' +
                        '<table class="table table-xxs table-bordered table-sticky tbl' + dataStage[i] + ' no-margin" style="text-align: center">';
                    html += '<thead><tr><th>STT</th><th>' + dataStage[i] + '</th><th>Final Fail</th><th>Test Fail</th><th>Total Failure Qty</th></tr></thead><tbody>';
                    if (data[dataStage[i]] == null || data[dataStage[i]] == undefined) {
                        html += '<tr><td colspan="5">NO DATA!</td></tr>';
                    } else {
                        for (j in data[dataStage[i]]) {
                            dataChart[stt] = (Number(data[dataStage[i]][j].testFail) + Number(data[dataStage[i]][j].fail));
                            categories[stt] = j;
                            stt++;
                            html += '<tr><td>' + stt + '</td><td>' + j + '</td><td>' + data[dataStage[i]][j].fail + '</td><td>' + data[dataStage[i]][j].testFail + '</td><td>' + (Number(data[dataStage[i]][j].testFail) + Number(data[dataStage[i]][j].fail)) + '</td></tr>';
                        }
                    }
                    html += '</tbody></table></div><div class="col-sm-6 col-xs-12"><div id="nbbPareto' + i + '" class="panel panel-flat panel-body paretoChart"></div></div></div>';
                    $("#content-detail").append(html);
                    var idPareto = 'nbbPareto' + i;
                    drawPareto(dataChart, categories, idPareto);
                }
                $(".loader").addClass("disableSelect");
                $("#content-detail").append("<div id='phantomjsMark'></div>");
                // drawPareto(dataChart,categories);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                dataset.timeout = setTimeout(loadData, 30000, dataset);
            }
        });
    }

    function drawPareto(dataChart, categories, idPareto) {
        Highcharts.chart(idPareto, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: 'PARETO',
                style: {
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            tooltip: {
                shared: true
            },
            xAxis: {
                categories: categories,
                allowDecimals: false,
                crosshair: true,
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '12px'
                    }
                }
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }, {
                title: {
                    text: ''
                },
                minPadding: 0,
                maxPadding: 0,
                max: 100,
                min: 0,
                opposite: true,
                labels: {
                    format: "{value:.2f}%"
                }
            }],
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
                type: 'pareto',
                name: 'Pareto',
                yAxis: 1,
                zIndex: 10,
                baseSeries: 1,
                tooltip: {
                    valueDecimals: 2,
                    valueSuffix: '%'
                }
            }, {
                name: 'Total Failure Qty',
                type: 'column',
                zIndex: 2,
                data: dataChart
            }]
        });
    }

    var tableToExcel = (function() {
        var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>',
            base64 = function(s) {
                return window.btoa(unescape(encodeURIComponent(s)))
            },
            format = function(s, c) {
                return s.replace(/{(\w+)}/g, function(m, p) {
                    return c[p];
                })
            }
        return function(table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {
                worksheet: name || 'Worksheet',
                table: table.innerHTML
            }
            var blob = new Blob([format(template, ctx)]);
            var blobURL = window.URL.createObjectURL(blob);
            return blobURL;
        }
    })();
    $("#btnExport").click(function() {
        var isIE = /*@cc_on!@*/ false || !!document.documentMode;
        if (isIE) {
            var tab_text = "<table border='2px'><tr bgcolor='#F79646'>";
            var textRange;
            var j = 0;
            tab = document.getElementById('tblDetail'); // id of table

            for (j = 0; j < tab.rows.length; j++) {
                tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
                //tab_text=tab_text+"</tr>";
            }

            tab_text = tab_text + "</table>";
            tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, ""); //remove if u want links in your table
            tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
            tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

            var ua = window.navigator.userAgent;
            var msie = ua.indexOf("MSIE ");

            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) // If Internet Explorer
            {
                txtArea1.document.open("txt/html", "replace");
                txtArea1.document.write(tab_text);
                txtArea1.document.close();
                txtArea1.focus();
                sa = txtArea1.document.execCommand("SaveAs", true, "UPH MB Assembly Line.xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblDetail', 'uph_table');
            $(this).attr('download', 'UPH MB Assembly Line(' + dataset.shiftType + ').xls')
            $(this).attr('href', blobURL);
        }


    });

    $(document).ready(function() {
        getTimeNow();

        $('input[name=timeSpan]').on('change', function() {
            dataset.workDate = this.value;
            $(".loader").removeClass("disableSelect");
            dataset.lineName = "";
            dataset.modelName = "";
            dataset.mo = "";
            window.localStorage.setItem('dataset', JSON.stringify(dataset));
            $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
            loadSelectLine();
            $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
            loadSelectModel();
            $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
            loadSelectMO();

            loadData();

            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
        });
    });
</script>