<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<link rel="stylesheet" href="/assets/custom/css/nbb/nbb-output.css" />
<style>
    .bootstrap-select.btn-group .btn .filter-option {
        color: #FFF;
        text-align: center;
        text-transform: uppercase;
        font-weight: 600;
    }
    .bootstrap-select.btn-group .btn .caret {
        width: 50px;
    }
    .bootstrap-select > .btn {
        border-bottom: none !important;
        border-radius: 5px;
    }
</style>
<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span class="text-uppercase"> ${factory}</span><span> Input & Output </span><span id="titlePage"></span></b>
        </div>
        <div class="row no-margin no-padding">
            <div class="col-md-3 no-padding">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control datetimerangenolimit" name="timeSpan" style="border-bottom: 0px !important; color: #fff;">
                </div>
            </div>
            <div class='col-sm-1 col-xs-3 selectCustomer'>
                <div class="dropdown dropdown-select-customer">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Vento
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer"></ul>
                </div>
            </div>
            <div class='col-sm-2 col-xs-4 selectStage'>
                <div class="dropdown dropdown-select-stage">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                        Select Stage
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage"></ul>
                </div>
            </div>
            <div class='col-sm-2 col-xs-5 selectModel hidden'>
                <div class="dropdown dropdown-select-model">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                        Select Model
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel"></ul>
                </div>
            </div>
            <div class='col-xs-2 col-xs-5 selectModel'>
                <select class="form-control bootstrap-select" name="modelName" data-live-search="true" data-focus="true"></select>
            </div>

            <div class="col-sm-1 export pull-right">
                <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>

        </div>
        <div class="row" style="margin: unset;">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 175px);color: #ccc; padding: unset;">
                <table id="tblOutput" class="table table-xxs table-bordered table-sticky" style="text-align: center;">
                    <thead></thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>

<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if (dataset == null || dataset == undefined || dataset.path != "${path}" || dataset.factory != "${factory}") {
        dataset = {
            stage: "",
            modelName: "",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }
    var timeSpan = getTimeSpan(0, true);
    dataset.timeSpan = moment(timeSpan.startDate).format("YYYY/MM/DD HH:mm") + " - " + moment(timeSpan.endDate).format("YYYY/MM/DD HH:mm");

    init();

    function init() {
        if(dataset.factory == "nbb" || dataset.factory == "s03"){
            loadSelectCustomer();
            $('.selectCustomer').removeClass('hidden');
            $('.selectStage').removeClass('hidden');
        } else{
            dataset.customer = '';
            loadSelectStage();
            $('.selectCustomer').addClass('hidden');
            // $('.selectStage').addClass('hidden');
        }
    }

    function loadSelectCustomer(allFlag){
        $.ajax({
            type: "GET",
            url: "/api/test/sfc/customer",
            data: {
                factory: dataset.factory
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(!$.isEmptyObject(data)){
                    if(dataset.customer == "" || dataset.customer == undefined){
                        dataset.customer = data[0];
                        window.localStorage.setItem('dataset', JSON.stringify(dataset));
                    }
                    var list = '';
                    if (allFlag != undefined && allFlag) {
                        list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">All</a></li>';
                    }
                    for(i in data){
                        list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                    }
                    $(".select-customer").html(list);
                    $("#titlePage").html(dataset.customer.toUpperCase());
                }
                $('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
                if (dataset.stage == "" || dataset.stage == undefined) {
                    $('.dropdown-select-stage').find('.dropdown-toggle').html('Stage <span class="caret"></span>');
                } else $('.dropdown-select-stage').find('.dropdown-toggle').html(dataset.stage + ' <span class="caret"></span>');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                loadSelectStage();
                loadData();
            }
        });
    }

    function loadSelectStage(){
        $.ajax({
            type: "GET",
            url: "/api/test/sfc/stage",
            data: {
                factory: dataset.factory,
                customer: dataset.customer
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var list = '';
                if(dataset.factory != "nbb" && dataset.factory != "s03"){
                    if(dataset.stage == "" || dataset.stage == undefined){
                        dataset.stage = data[0];
                        $('.dropdown-select-stage').find('.dropdown-toggle').html(dataset.stage + ' <span class="caret"></span>');
                        window.localStorage.setItem('dataset', JSON.stringify(dataset));
                    }
                } else{
                    list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
                }
                var list = '';
                for(i in data){
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                }
                $(".select-stage").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                loadSelectModel();
            }
        });
    }

    function loadSelectModel(){
        $.ajax({
            type: "GET",
            url: "/api/test/sfc/model",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage,
                workDate: dataset.workDate,
                shiftType: dataset.shiftType,
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(!$.isEmptyObject(data)){
                    var list = '';
                    if(dataset.factory != "nbb" && dataset.factory != "s03"){
                        if(dataset.modelName == "" || dataset.modelName == undefined){
                            dataset.modelName = data[0];
                            window.localStorage.setItem('dataset', JSON.stringify(dataset));
                        }
                        loadData();
                    } else{
                        list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
                    }
                    for(i in data){
                        list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                    }
                    $(".select-model").html(list);
                    if (dataset.modelName == "" || dataset.modelName == undefined) {
                        $('.dropdown-select-model').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');
                    } else $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');

                    var selector = $("select[name='modelName']");
                    selector.children('option').remove();
                    var options = "<option value=''>Select All</option>";
                    for(i in data){
                        if(data[i] == dataset.modelName){
                            options+='<option value=' + data[i] + ' selected>' + data[i] + '</option>';
                            $('li[data-original-index="'+i+'"]').addClass("active");
                        }
                        else
                            options+='<option value=' + data[i] + '>' + data[i] + '</option>';
                    }
                    selector.append(options);
                    selector.selectpicker('refresh');
                } else {
                    if(dataset.factory != "nbb" && dataset.factory != "s03"){
                        loadData();
                    }
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        dataset.customer = $(this).html();

        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        dataset.stage = "";
        dataset.modelName = "";
        $(".loader").removeClass("disableSelect");
        loadData();
        $('.dropdown-select-stage').find('.dropdown-toggle').html('Stage Name <span class="caret"></span>');
        loadSelectStage();
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
        $(".loader").removeClass("disableSelect");
        loadData();

        $('.selectModel').removeClass("disableSelect");
        $('.dropdown-select-model').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');
        loadSelectModel();
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
        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    $('select[name=modelName]').on('change', function() {
        dataset.modelName = this.value;

        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    function loadData() {
        $.ajax({
            type: 'GET',
            url: '/api/test/group/output',
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                timeSpan: dataset.timeSpan
            },
            success: function(data) {
                $("#tblOutput thead").html("");
                $("#tblOutput tbody").html("");
                if(!$.isEmptyObject(data)){
                    var dataHeader = Object.keys(data);
                    var dataStage = new Object;
                    dataStage["SMT"] = (data["HEADER"]["SMT"]);
                    dataStage["SMA"] = (data["HEADER"]["SMA"]);
                    dataStage["FAT"] = (data["HEADER"]["FAT"]);
                    dataStage["SUB-PACK"] = (data["HEADER"]["SUB-PACK"]);
                    dataStage["MAIN-PACK"] = (data["HEADER"]["MAIN-PACK"]);
                    for(i in data["HEADER"]){
                        if(i != "SMT" && i != "SMA" && i != "FAT" && i != "SUB-PACK" && i != "MAIN-PACK"){
                            dataStage[i] = (data["HEADER"][i]);
                        }
                    }
                    // var dataStage = data["HEADER"];
                    var th = '<tr><th style="width:120px">Stage</th><th style="width:160px">Line/Model</th><th style="width:70px">Shift</th><th style="width:80px">In/Out</th>';
                    for (i in dataHeader) {
                        if (dataHeader[i] != "HEADER")
                            th += '<th style="width:85px">' + dataHeader[i] + '</th>';
                    }
                    th += '</tr>';
                    $("#tblOutput thead").html(th);

                    var dataLine = [];
                    var tb = '';
                    for (i in dataStage) {
                        for (j in dataStage[i]) {
                            tb += '<tr class="' + i + " " + i + j + 'Day in">' +
                                '</tr>' +
                                '<tr class="' + i + " " + i + j + 'Day out">' +
                                '</tr>' +
                                '<tr  class="' + i + " " + i + j + 'Night sNight in">' +
                                '</tr>' +
                                '<tr  class="' + i + " " + i + j + 'Night sNight out">' +
                                '</tr>';
                            if (dataLine.indexOf(j) == -1) dataLine.push(j);
                        }
                    }
                    $("#tblOutput tbody").append(tb);

                    var rowS1 = 0;
                    for (i in dataStage) {
                        if (dataStage[i] != null && dataStage[i] != undefined) {
                            rowS1 = Object.keys(dataStage[i]).length * 4;
                            $("." + i).first().append("<td rowspan=" + rowS1 + " class='" + i + "1 column1'>" + i + "</td>");
                        }
                    }
                    var childS1 = 1;
                    for (i in dataStage) {
                        if (dataStage[i] != null && dataStage[i] != undefined) {
                            for (j in dataStage[i]) {
                                $("." + i + ":nth-child(" + childS1 + ")").append("<td rowspan=" + 4 + " class='" + i + "2 column2'>" + j + "</td>");
                                childS1 += 4;
                            }
                        }
                    }
                    var childS2 = 1;
                    for (i in dataStage) {
                        if (dataStage[i] != null && dataStage[i] != undefined) {
                            for (j in dataStage[i]) {
                                $("." + i + j + "Day:nth-child(" + childS2 + ")").append("<td rowspan=" + 2 + " class='" + i + "3 column3'>Day</td>");
                                childS2 += 2;
                                $("." + i + j + "Night:nth-child(" + childS2 + ")").append("<td rowspan=" + 2 + " class='nShift " + i + "3 column3'>Night</td>");
                                childS2 += 2;
                            }
                        }
                    }
                    for (i in dataStage) {
                        if (dataStage[i] != null && dataStage[i] != undefined) {
                            for (j in dataStage[i]) {
                                $("." + i + j + "Day.in").append("<td class='" + i + "4 column4'>Input</td>");
                                $("." + i + j + "Day.out").append("<td class='" + i + "4 column4'>Output</td>");
                                $("." + i + j + "Night.in").append("<td class='nShift " + i + "4 column4'>Input</td>");
                                $("." + i + j + "Night.out").append("<td class='nShift " + i + "4 column4'>Output</td>");
                            }
                        }
                    }

                    for (i in data) {
                        if (i != "HEADER") {
                            for (j in dataStage) {
                                if (data[i][j] == null || data[i][j] == undefined) {
                                    $("." + j).append("<td class='noDataB'><b>0</b></td>");
                                } else {
                                    for (k in dataLine) {
                                        if (data[i][j][dataLine[k]] == null || data[i][j][dataLine[k]] == undefined) {
                                            $("." + j + dataLine[k] + "Day").append("<td class='noDataB'><b>0</b></td>");
                                            $("." + j + dataLine[k] + "Night").append("<td class='noDataB'><b>0</b></td>");
                                        } else {
                                            if (data[i][j][dataLine[k]]["DAY"] == null || data[i][j][dataLine[k]]["DAY"] == undefined) {
                                                $("." + j + dataLine[k] + "Day").append("<td class='noDataB'><b>0</b></td>");
                                            } else {
                                                if (data[i][j][dataLine[k]]["DAY"].input == null || data[i][j][dataLine[k]]["DAY"].input == undefined) {
                                                    $("." + j + dataLine[k] + "Day.in").append("<td class='noDataB'><b>0</b></td>");
                                                } else {
                                                    var tdD = '<td><b>' + data[i][j][dataLine[k]]["DAY"].input + '</b></td>';
                                                    $("." + j + dataLine[k] + "Day.in").append(tdD);
                                                }
                                                if (data[i][j][dataLine[k]]["DAY"].output == null || data[i][j][dataLine[k]]["DAY"].output == undefined) {
                                                    $("." + j + dataLine[k] + "Day.out").append("<td class='noDataB'><b>0</b></td>");
                                                } else {
                                                    var tdD = '<td><b>' + data[i][j][dataLine[k]]["DAY"].output + '</b></td>';
                                                    $("." + j + dataLine[k] + "Day.out").append(tdD);
                                                }
                                            }
                                            if (data[i][j][dataLine[k]]["NIGHT"] == null || data[i][j][dataLine[k]]["NIGHT"] == undefined) {
                                                $("." + j + dataLine[k] + "Night").append("<td class='nShift'><b>0</b></td>");
                                            } else {
                                                if (data[i][j][dataLine[k]]["NIGHT"].input == null || data[i][j][dataLine[k]]["NIGHT"].input == undefined) {
                                                    $("." + j + dataLine[k] + "Night.in").append("<td class='noDataB'><b>0</b></td>");
                                                } else {
                                                    var tdN = '<td class="nShift"><b>' + data[i][j][dataLine[k]]["NIGHT"].input + '</b></td>';
                                                    $("." + j + dataLine[k] + "Night.in").append(tdN);
                                                }
                                                if (data[i][j][dataLine[k]]["NIGHT"].output == null || data[i][j][dataLine[k]]["NIGHT"].output == undefined) {
                                                    $("." + j + dataLine[k] + "Night.out").append("<td class='noDataB'><b>0</b></td>");
                                                } else {
                                                    var tdN = '<td class="nShift"><b>' + data[i][j][dataLine[k]]["NIGHT"].output + '</b></td>';
                                                    $("." + j + dataLine[k] + "Night.out").append(tdN);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $(".loader").addClass("disableSelect");
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
        var tab_text = "<table border='1px'><tr bgcolor='#F79646'>";
        var textRange;
        var j = 0;
        tab = document.getElementById('tblOutput'); // id of table

        for (j = 0; j < tab.rows.length; j++) {
            tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
            //tab_text=tab_text+"";
        }

        tab_text = tab_text + "</table>";
        tab_text = tab_text.replace(/<a[^>]*>|<\/A>/g, ""); //remove if u want links in your table
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
            sa = txtArea1.document.execCommand("SaveAs", true, "Input & Output.xls");
            return (sa);
        } else {
            //other browser not tested on IE 11
            //sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            var a = document.createElement('a');
            a.href = 'data:application/vnd.ms-excel,' + encodeURIComponent(tab_text);
            a.download = 'Input & Output.xls';
            a.click();
            // e.preventDefault();
        }
    });

    $(document).ready(function() {
        var spcTS = getTimeSpan(0, true);
        $('.datetimerangenolimit').data('daterangepicker').setStartDate(spcTS.startDate);
        $('.datetimerangenolimit').data('daterangepicker').setEndDate(spcTS.endDate);

        $('input[name=timeSpan]').on('change', function() {
            dataset.timeSpan = this.value;
            $(".loader").removeClass("disableSelect");
            dataset.modelName = "";
            window.localStorage.setItem('dataset', JSON.stringify(dataset));
            $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
            loadSelectModel();

            loadData();
            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
        });
    });
</script>