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
            <b><span id="titlePage"></span><span class="text-uppercase"> ${factory}</span><span> WIP</span></b>
        </div>
        <div class="row" style="margin: 0 0 5px 0;">
            <div class='col-sm-1 col-xs-3 no-padding selectCustomer'>
                <div class="dropdown dropdown-select-customer">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Vento
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer"></ul>
                </div>
            </div>

            <div class='col-sm-2 col-xs-4 selectStage'>
                <div class="dropdown dropdown-select-stage">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                        Select Stage
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage"></ul>
                </div>
            </div>

            <div class='col-sm-2 col-xs-5 selectModel'>
                <div class="dropdown dropdown-select-model">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                        Select Model
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel"></ul>
                </div>
            </div>

            <div class='col-sm-2 col-xs-5 selectMO'>
                <div class="dropdown dropdown-select-mo">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectMO" data-toggle="dropdown" aria-expanded="true">
                        Select MO
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-mo table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectMO"></ul>
                </div>
            </div>

            <div class="col-sm-1 export pull-right">
                <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 175px);color: #ccc; padding: unset;">
                <table id="tblOutput" class="table table-xxs table-bordered table-sticky" style="text-align: center">
                    <thead>
                        <tr>
                            <!-- <th>Station</th> -->
                            <th>Model Name</th>
                            <th>MO</th>
                            <th>Target Qty</th>
                            <th>Total Input</th>
                            <th>Stockin Qty</th>
                            <th>Link Qty</th>
                            <th>Unlink Qty</th>
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
            stage: "",
            modelName: "",
            mo: "",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }

    var dataModelNames = [];
    var dataGroupNames = [];

    init();

    function init() {
        loadSelectCustomer();
        loadSelectStage();
        loadSelectModel();
        loadSelectMO();
        // loadDataTypeShift(dataset.shiftType);
        loadData();

        $('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        if (dataset.stage != "") {
            $('.dropdown-select-stage').find('.dropdown-toggle').html(dataset.stage + ' <span class="caret"></span>');
        } else if (dataset.stage == "" && dataset.modelName != "") {
            $('.dropdown-select-stage').find('.dropdown-toggle').html('Select All <span class="caret"></span>');
            $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');
        } else $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        if (dataset.modelName != "") {
            $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');
        } else $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');

        if (dataset.mo == "" || dataset.mo == undefined) {
            $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        } else $('.dropdown-select-mo').find('.dropdown-toggle').html(dataset.mo + ' <span class="caret"></span>');

    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        dataset.customer = $(this).html();
        $("#titlePage").html(dataset.customer.toUpperCase());

        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        dataset.mo = "";
        dataset.modelName = "";
        dataset.stage = "";

        $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadData();
        $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        loadSelectStage();
        $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        loadSelectModel();
        $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        loadSelectMO();
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
        dataset.mo = ""

        $(".loader").removeClass("disableSelect");
        loadData();
        $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        loadSelectModel();
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
        dataset.mo = "";
        loadData();
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

    function loadData() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/group/wipByModel",
            data: {
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                mo: dataset.mo
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#tblOutput tbody").html("");
                var dataSubLine = ["SMT", "SMA", "FAT", "SUB-PACK", "MAIN-PACK"];
                var html = '';
                for (i in dataSubLine) {
                    if (data[dataSubLine[i]] != undefined || data[dataSubLine[i]] != null) {
                        html += '<tr><td colspan="7" class="rowLine">' + dataSubLine[i] + '</td></tr>';
                        for (j in data[dataSubLine[i]]) {
                            html += '<tr><td>' + data[dataSubLine[i]][j].modelName + '</td>' +
                                '<td>' + data[dataSubLine[i]][j].mo + '</b></td>' +
                                '<td><b>' + data[dataSubLine[i]][j].plan + '</b></td>' +
                                '<td><b>' + data[dataSubLine[i]][j].wip + '</b></td>' +
                                '<td><b>' + (Number(data[dataSubLine[i]][j].pass) + Number(data[dataSubLine[i]][j].fail)) + '</b></td>' +
                                '<td><b>' + data[dataSubLine[i]][j].pass + '</b></td>' +
                                '<td><b>' + data[dataSubLine[i]][j].fail + '</b></td></tr>';
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

    //Filter Model Setup
    $("#txtFilterSetup").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblOutput tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

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
</script>