<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<style>
    .rowLine {
        background-color: #92D050 !important;
        color: #333;
        font-weight: bold;
        position: sticky;
        top: 36px;
        border-top: none !important;
        border-bottom: none !important;
        box-shadow: inset 0 0px 0 #fff, inset 0 -1px 0 #fff;
    }
    
    #tblOutput tr th {
        background-color: #F79646;
        color: #272727;
        text-align: center;
    }
    
    #tblOutput tr th,
    #tblOutput tr td {
        font-size: 16px;
    }
</style>

<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span class="text-uppercase"> ${factory}</span><span> Yield Overall </span><span id="titlePage"></span> <span id="txtDateTime"></span></b>
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
        <div class="row" style="margin: unset;">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 175px);color: #ccc; padding: unset;">
                <table id="tblOutput" class="table table-xxs table-bordered table-sticky" style="text-align: center">
                    <thead>
                        <tr>
                            <th>Line</th>
                            <th>Group Name</th>
                            <th>Total Input Qty</th>
                            <th>Pass Qty</th>
                            <th>First Fail Qty</th>
                            <th>Fail Qty</th>
                            <th>Repair Qty</th>
                            <th>First Pass Yield</th>
                            <th>Retest Rate</th>
                            <th>Final Pass Yield</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
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
            lineName: "",
            modelName: "",
            mo: "",
            stage: "",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }

    var dataModelNames = [];
    var dataGroupNames = [];

    function init() {
        // loadData();
        loadSelectCustomer();
        loadSelectStage();
        loadSelectModel();
        loadSelectLine();
        loadSelectMO();
        loadDataTypeShift(dataset.shiftType);
        $('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        if (dataset.stage != "") {
            $('.dropdown-select-stage').find('.dropdown-toggle').html(dataset.stage + ' <span class="caret"></span>');
            loadSelectModel();
            $('.selectModel').removeClass("disableSelect");
        } else if (dataset.stage == "" && dataset.modelName != "") {
            $('.dropdown-select-stage').find('.dropdown-toggle').html('Select All <span class="caret"></span>');
            loadSelectModel();
            $('.selectModel').removeClass("disableSelect");
            $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');
        } else $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        if (dataset.modelName != "") {
            $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');
        } else $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');

        if (dataset.mo == "" || dataset.mo == undefined) {
            $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        } else $('.dropdown-select-mo').find('.dropdown-toggle').html(dataset.mo + ' <span class="caret"></span>');

        if (dataset.lineName == "" || dataset.lineName == undefined) {
            $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        } else $('.dropdown-select').find('.dropdown-toggle').html(dataset.lineName + ' <span class="caret"></span>');
    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        dataset.customer = $(this).html();
        $("#titlePage").html(dataset.customer.toUpperCase());

        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        dataset.lineName = "";
        dataset.mo = "";

        $('.selectLine').removeClass("disableSelect");
        $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadSelectLine();
        loadData();
        $('.selectStage').removeClass("disableSelect");
        $('.selectModel').addClass("disableSelect");
        $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        loadSelectStage()
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    $('.dropdown-select').on('click', '.select-line li a', function() {
        var target = $(this).html();
        dataset.lineName = $(this).attr("name");

        //Adds active class to selected item
        $(this).parents('.select-line').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        // dataset.mo = "";
        // $("#titlePage").html(target.toUpperCase());
        $('.selectMO').removeClass("disableSelect");
        // $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadSelectMO();
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    $('.dropdown-select-stage').on('click', '.select-stage li a', function() {
        var value = $(this).html();
        if (value == "Select All")
            dataset.stage = "";
        else dataset.stage = value;

        //Adds active class to selected item
        $(this).parents('.select-stage').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-stage').find('.dropdown-toggle').html(value + ' <span class="caret"></span>');
        dataset.modelName = "";
        dataset.lineName = "";
        dataset.mo = "";
        $(".loader").removeClass("disableSelect");
        loadData();

        $('.selectModel').removeClass("disableSelect");
        $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        loadSelectModel();
        $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        loadSelectLine();
        $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        loadSelectMO();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    $('.dropdown-select-model').on('click', '.select-model li a', function() {
        var value = $(this).html();
        if (value == "Select All")
            dataset.modelName = "";
        else dataset.modelName = value;

        //Adds active class to selected item
        $(this).parents('.select-model').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-model').find('.dropdown-toggle').html(value + ' <span class="caret"></span>');
        dataset.lineName = "";
        dataset.mo = "";
        loadData();
        $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        loadSelectLine();
        $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        loadSelectMO();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    $('.dropdown-select-mo').on('click', '.select-mo li a', function() {
        var target = $(this).html();
        if (target == "Select All") {
            dataset.mo = "";
        } else dataset.mo = target;

        //Adds active class to selected item
        $(this).parents('.select-mo').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-mo').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        loadSelectLine();
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

    function loadData1() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/group/overall",
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
                $("#tblOutput tbody").html("");
                var dataSubLine = {
                    "SMT": {},
                    "SMA": {},
                    "FAT": {},
                    "SUB-PACK": {}
                };

                for (i in dataSubLine) {
                    var dataStation = {};
                    for (j in data[i]) {
                        for (k in data[i][j]) {
                            dataStation[k] = j;
                        }
                        dataSubLine[i] = dataStation;
                    }
                }

                var html = '';
                var stt = 0;
                for (i in dataSubLine) {
                    html = '<tr><td  colspan="12" class="rowLine">Line: ' + i + '</td></tr>';
                    for (j in dataSubLine[i]) {
                        html += '<tr class="' + dataSubLine[i][j] + '_' + i + '_' + stt + '"><td>' + dataSubLine[i][j] + '</td><td>' + j + '</td></tr>';
                        stt++;
                    }
                    $("#tblOutput tbody").append(html);
                }
                var stt1 = 0;
                var tb = '';
                for (i in dataSubLine) {
                    for (j in dataSubLine[i]) {
                        if (data[i][dataSubLine[i][j]] != null || data[i][dataSubLine[i][j]] != undefined) {
                            tb = '<td><b>' + data[i][dataSubLine[i][j]][j].wip + '</b></td>';
                            tb += '<td><b>' + data[i][dataSubLine[i][j]][j].pass + '</b></td>';
                            tb += '<td><b>' + data[i][dataSubLine[i][j]][j].firstFail + '</b></td>';
                            tb += '<td><b>' + data[i][dataSubLine[i][j]][j].fail + '</b></td>';
                            tb += '<td><b>' + data[i][dataSubLine[i][j]][j].secondFail + '</b></td>';
                            tb += '<td><b>' + data[i][dataSubLine[i][j]][j].firstPassRate.toFixed(2) + '%</b></td>';
                            tb += '<td><b>' + data[i][dataSubLine[i][j]][j].retestRate.toFixed(2) + '%</b></td>';
                            tb += '<td><b>' + data[i][dataSubLine[i][j]][j].yieldRate.toFixed(2) + '%</b></td>';
                            $('.' + dataSubLine[i][j] + '_' + i + '_' + stt1).append(tb);
                            stt1++;
                        }
                    }
                }
                $(".loader").addClass("disableSelect");
                $("#tblOutput").append("<div id='phantomjsMark'></div>");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                dataset.timeout = setTimeout(loadData, 30000, dataset.shiftType, dataset.workDate, dataset.lineName, dataset.customer, dataset.mo, dataset.stage, dataset.modelName);
            }
        });
    }

    function loadData() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/group/overall",
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
                $("#tblOutput tbody").html("");
                var dataSubLine = {
                    "SMT": {},
                    "SMA": {},
                    "FAT": {},
                    "SUB-PACK": {}
                };
                var html = '';
                for (i in dataSubLine) {
                    html += '<tr><td class="rowLine" colspan="10">' + i + '</td></tr>';
                    for (j in data[i]) {
                        for (k in data[i][j]) {
                            html += '<tr><td><a data-href="/nbb-uph-by-line?customer=' + dataset.customer + '&lineName=' + j + '&shiftType=' + dataset.shiftType + '&workDate=' + dataset.workDate + '" onclick="openWindow(this.dataset.href);">' + j + '</a></td></td>';
                            html += '<td>' + k + '</td>';
                            html += '<td><b>' + data[i][j][k].wip + '</b></td>';
                            html += '<td><b>' + data[i][j][k].pass + '</b></td>';
                            html += '<td><b>' + data[i][j][k].firstFail + '</b></td>';
                            html += '<td><b>' + data[i][j][k].fail + '</b></td>';
                            html += '<td><b>' + data[i][j][k].secondFail + '</b></td>';
                            html += '<td><b>' + data[i][j][k].firstPassRate.toFixed(2) + '%</b></td>';
                            html += '<td><b>' + data[i][j][k].retestRate.toFixed(2) + '%</b></td>';
                            html += '<td><b>' + data[i][j][k].yieldRate.toFixed(2) + '%</b></td>';
                            html += '</tr>';
                        }
                    }
                }
                $("#tblOutput tbody").append(html);
                $(".loader").addClass("disableSelect");
                $("#tblOutput").append("<div id='phantomjsMark'></div>");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                dataset.timeout = setTimeout(loadData, 30000, dataset);
            }
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
            tab = document.getElementById('tblOutput'); // id of table

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
                sa = txtArea1.document.execCommand("SaveAs", true, "Yield Overall .xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblOutput', 'uph_table');
            $(this).attr('download', 'Yield Overall.xls')
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