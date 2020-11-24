<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<style>
    .rowLine {
        background-color: #92D050 !important;
        color: #333;
        font-weight: bold;
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
            <b><span class="text-uppercase"> ${factory}</span><span> Yield Overall SMT </span><span id="titlePage" class="hidden"></span> <span id="txtDateTime"></span></b>
        </div>
        <div class="row" style="margin: unset; padding: unset">

            <div class="drlMenu" style="margin: unset; width: 10%">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control daterange-single" side="right" name="timeSpan" style="border-bottom: 0px !important; color: #fff;">
                </div>
            </div>

            <div class="btn-group" style="margin: 0 15px; float:left">
                <button id="a-shift" class="btn btn-default select-shift selected" onclick="loadDataTypeShift('all')">All</button>
                <button id="d-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('day-shift')">Day</button>
                <button id="n-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('night-shift')">Night</button>
            </div>

            <div class='drlMenu selectCustomer disableSelect'>
                <div class="dropdown dropdown-select-customer">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Vento
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer">

                    </ul>
                </div>
            </div>

            <div class='drlMenu selectStage disableSelect'>
                <div class="dropdown dropdown-select-stage">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                        Select Stage
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage">

                    </ul>
                </div>
            </div>

            <div class='drlMenu selectModel disableSelect'>
                <div class="dropdown dropdown-select-model">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                        Select Model
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel">

                    </ul>
                </div>
            </div>
            <div class='drlMenu selectLine'>
                <div class="dropdown dropdown-select">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
                        Select Line
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-line table-responsive pre-scrollable" role="menu" aria-labelledby="dropdownMenu1">

                    </ul>
                </div>
            </div>
            <div class='drlMenu selectModel'>
                <div class="dropdown dropdown-select-model">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                        Select Model
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel">

                    </ul>
                </div>
            </div>

            <div class='drlMenu selectMO disableSelect'>
                <div class="dropdown dropdown-select-mo">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectMO" data-toggle="dropdown" aria-expanded="true">
                        Select MO
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-mo table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectMO">

                    </ul>
                </div>
            </div>

            <div class="warningSepupModel disableSelect">
                <ion-nav-buttons class="pull-right" style="position: absolute; top: 0px; right: 15px;" data-toggle="modal" data-target="#modalSetupModel">
                    <i class="fa fa-exclamation-circle" title="Click to setup Model" data-toggle="modal" style="font-size: 36px; cursor: pointer; color: #F79646;"></i>
                </ion-nav-buttons>
            </div>

            <div class="col-sm-1 export pull-right">
                <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 200px);color: #ccc; padding: unset;">
                <table id="tblOutput" class="table table-xxs table-bordered table-sticky" style="text-align: center">
                    <thead>
                        <tr>
                            <th>Line</th>
                            <th>Time</th>
                            <th>Model</th>
                            <th>Total Input Qty</th>
                            <th>Pass Qty</th>
                            <th>Fail Qty</th>
                            <th>Repass Qty</th>
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
            lineName: "S5",
            modelName: "",
            mo: "",
            stage: "SMT",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }

    var dataModelNames = [];
    var dataGroupNames = [];

    function loadSelectAoiModel() {
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/aoi/model",
            data: {
                lineName: dataset.lineName,
                workDate: dataset.workDate,
                shiftType: dataset.shiftType,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
                for (i in data) {
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">' + data[i] + '</a></li>';
                }
                $(".select-model").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function init() {
        loadSelectAoiModel();
        loadDataTypeShift(dataset.shiftType);

        $('.dropdown-select').find('.dropdown-toggle').html('SMT 5 <span class="caret"></span>');
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
        // $("#titlePage").html(target.toUpperCase());
        $(".loader").removeClass("disableSelect");
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
            url: "/api/test/" + dataset.factory + "/aoi/io",
            data: {
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
                lineName: dataset.lineName,
                modelName: dataset.modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#tblOutput tbody").html("");
                var dataSubLine = {
                    "SMT": {}
                };
                var html = '';

                for (i in data) {
                    html += '<tr><td>' + data[i].lineName + '</td>';
                    html += '<td>' + data[i].timeSpan + '</td>';
                    html += '<td>' + data[i].modelName + '</td>';
                    html += '<td><b>' + data[i].wip + '</b></td>';
                    html += '<td><b>' + data[i].pass + '</b></td>';
                    html += '<td><b>' + data[i].wkFail + '</b></td>';
                    html += '<td><b>' + data[i].repass + '</b></td>';
                    html += '<td><b>' + data[i].yieldRate.toFixed(2) + '%</b></td>';
                    html += '</tr>';
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
            var sa;
            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) // If Internet Explorer
            {
                txtArea1.document.open("txt/html", "replace");
                txtArea1.document.write(tab_text);
                txtArea1.document.close();
                txtArea1.focus();
                sa = txtArea1.document.execCommand("SaveAs", true, "Yield Overall SMT.xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblOutput', 'uph_table');
            $(this).attr('download', 'Yield Overall SMT.xls')
            $(this).attr('href', blobURL);
        }


    });

    $(document).ready(function() {
        getTimeNow();
        $('input[name=timeSpan]').on('change', function() {
            dataset.workDate = this.value;
            $(".loader").removeClass("disableSelect");
            dataset.lineName = "";
            window.localStorage.setItem('dataset', JSON.stringify(dataset));
            $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
            loadSelectLine();

            loadData();
            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
        });
    });
</script>