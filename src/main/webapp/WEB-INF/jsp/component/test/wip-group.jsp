<link rel="stylesheet" href="/assets/custom/css/nbb/nbb-mo-history.css" />
<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/heatmap.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/accessibility.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<div class="loader hidden"></div>
<div class="panel panel-re panel-flat row"  style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header" style="font-size: 23px;">
            <b><span id="titlePage"></span><span> WIP GROUP </span><span id="txtDateTime"></span></b>
        </div>
        <div class="row no-margin no-padding">
            <div class="col-xs-1 no-padding">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control daterange-single" side="right" name="timeSpan" style="border-bottom: 0px !important; color: #fff;">
                </div>
            </div>

            <div class="col-xs-2 btn-group" style="margin-bottom: 5px;">
                <button id="a-shift" class="btn btn-default select-shift selected" onclick="loadDataTypeShift('')">All</button>
                <button id="d-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('DAY')">Day</button>
                <button id="n-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('NIGHT')">Night</button>
            </div>
            <div class='col-xs-1 selectCustomer' style="margin-bottom: 5px;">
                <div class="dropdown dropdown-select-customer">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Select Customer
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer"></ul>
                </div>
            </div>
            <div class='col-xs-1 selectStage' style="margin-bottom: 5px;">
                <div class="dropdown dropdown-select-stage">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                        Stage
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage">

                    </ul>
                </div>
            </div>

            <div class='col-xs-2 selectModel hidden' style="margin-bottom: 5px;">
                <div class="dropdown dropdown-select-model">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                        Select Model
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel">

                    </ul>
                </div>
            </div>

            <div class='col-xs-2 selectModel' style="margin-bottom: 5px;">
                <select class="form-control bootstrap-select" name="modelName" data-live-search="true" data-focus-off="true"></select>
            </div>

            <div class='col-xs-1 selectLine'>
                <div class="dropdown dropdown-select">
                    <button class="btn dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
                        Line
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-line table-responsive pre-scrollable" role="menu" aria-labelledby="dropdownMenu1">

                    </ul>
                </div>
            </div>

            <div class='col-xs-2 selectMO'>
                <div class="dropdown dropdown-select-mo">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectMO" data-toggle="dropdown" aria-expanded="true">
                        MO Num
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-mo table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectMO">

                    </ul>
                </div>
            </div>

            <div class="col-md-12 no-padding" style="margin-top: 3px; margin-bottom: 5px;">
                <div class="form-group drlMenu" style="margin: unset; width: 20%">
                    <label class="col-xs-2 control-label text-bold" style="padding-top: 8px;">MO</label>
                    <div class="col-xs-10" style="padding-right: 0px;">
                        <input type="text" name="mo" class="form-control" placeholder="" style="color: #fff;border: 1px solid;height: 3rem;padding: 0px 5px;margin-top: 3px;border-radius: 3px;">
                    </div>
                </div>
                <div class="form-group drlMenu" style="margin: unset; width: 20%;margin-left: 65px;">
                    <label class="col-md-2 control-label text-bold" style="padding-top: 8px;">SN</label>
                    <div class="col-md-10">
                        <input type="text" name="sn" class="form-control" placeholder="" style="color: #fff;border: 1px solid;height: 3rem;padding: 0px 5px;margin-top: 3px;border-radius: 3px;">
                    </div>
                </div>
            <div class="col-sm-1" style="margin-left: 5px;">
                <a class="btn btn-lg" id="btnSearch" onclick="loadData()" style="font-size: 13px;color: #ccc;border: 1px solid;padding: 5px 10px;margin-top: 3px;">
                    <i class="fa fa-search"></i> SEARCH
                </a>
            </div>
            </div>
        </div>

        <div class="row no-margin">
            <div id="container1" style="width:100%; height: calc(100vh - 220px);"></div>
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);color: #ccc; padding: unset;">
                <table id="tblDetail" class="table table-xxs table-bordered table-sticky table-header-orange" style="text-align: center">
                    <thead></thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
        
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>


<!-- Modal -->
<div class="modal fade" id="modalDetailWip" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #333; color: #ccc;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
                <div class="modal-title">
                    <span class="spMO" id="spMONum"></span>
                    <input id="txtFilterWip" class="form-control" type="text" placeholder="Filter" style="float: right; padding: 5px 10px; border-radius: 4px; color: #fff; background: #272727; width: 20%; height: 32px; margin-right: 5%;" />
                </div>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 160px);">
                    <table id="tblDetailWip" class="table table-bordered table-xxs table-sticky table-header-orange" style="margin-bottom: 10px; color: #fff; text-align: center;">
                        <thead>
                            <tr>
                                <th style="width: 2%;">STT</th>
                                <th style="width: 14%;">Serial Number</th>
                                <th style="width: 7%;">Line</th>
                                <th style="width: 17%;">Model</th>
                                <th style="width: 11%;">Group</th>
                                <th style="width: 12%;">Station</th>
                                <th style="width: 19%;">In Station Time</th>
                                <th>WIP Group</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalDetailSN" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #333; color: #ccc; min-height: calc(100vh - 60px);">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
                <div class="modal-title">
                    <span class="spMO">SN History</span>
                    <input id="txtFilterSN" class="form-control" type="text" placeholder="Filter" style="float: right; padding: 5px 10px; border-radius: 4px; color: #fff; background: #272727; width: 20%; height: 32px; margin-right: 5%;" />
                </div>
            </div>
            <div class="modal-body">
                <div class=" pre-scrollable" style="color: #333333; max-height: calc(100vh - 160px);">
                    <table id="tblDetailSN" class="table table-bordered table-xxs table-sticky table-header-orange" style="margin-bottom: 10px; color: #fff; text-align: center;">
                        <thead>
                            <tr>
                                <th style="width: 2%;">STT</th>
                                <th style="width: 14%;">Serial Number</th>
                                <th style="width: 7%;">Line</th>
                                <th style="width: 17%;">Model</th>
                                <th style="width: 11%;">Group</th>
                                <th style="width: 14%;">Station</th>
                                <th style="width: 19%;">In Station Time</th>
                                <th>WIP Group</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="/assets/custom/js/nbb/getTimeNow.js"></script>
<script type="text/javascript" src="/assets/js/custom/load-selection-group.js"></script>
<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if (dataset == null || dataset == undefined || dataset.path != "${path}" || dataset.factory != "${factory}") {
        dataset = {
            stage: "",
            lineName: "",
            mo: "",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        getTimeNow();
    } else {
        getTimeNow();
    }

    function init() {
        if(dataset.factory == "nbb" || dataset.factory == "s03"){
            loadSelectCustomer();
            $('.selectCustomer').removeClass('hidden');
            $('.selectStage').removeClass('hidden');
        } else{
            dataset.customer = '';
            loadSelectModel();
            $('.selectCustomer').addClass('hidden');
            $('.selectStage').addClass('hidden');
        }
    }

     $('.dropdown-select-customer').on( 'click', '.select-customer li a', function() { 
        dataset.customer = $(this).html();
        $("#titlePage").html(dataset.customer.toUpperCase());
        // console.log("stage",dataset.customer);
        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        dataset.stage = "";
        dataset.modelName = "";
        dataset.mo = "";
        dataset.lineName = "";

        $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        loadSelectStage();
        $('#container1').html('');
    });
    $('.dropdown-select-stage').on( 'click', '.select-stage li a', function() { 
        var value = $(this).html();
        if(value == "Select All")
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

        $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        loadSelectModel();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        $('#container1').html('');
    });
    $('.dropdown-select-model').on( 'click', '.select-model li a', function() { 
        var value = $(this).html();
        if(value == "Select All")
            dataset.modelName = ""; 
        else dataset.modelName = value;
        
        //Adds active class to selected item
        $(this).parents('.select-model').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-model').find('.dropdown-toggle').html(value + ' <span class="caret"></span>');
        dataset.lineName = "";
        dataset.mo = "";
        $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        loadSelectLine();
        $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        loadSelectMO();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    $('select[name=modelName]').on('change', function() {
        dataset.modelName = this.value;

        dataset.lineName = "";
        $('.dropdown-select').find('.dropdown-toggle').html('Line <span class="caret"></span>');
        loadSelectLine();
        dataset.mo = "";
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
        loadSelectMO();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        $('#container1').html('');
    });

    $('.dropdown-select').on( 'click', '.select-line li a', function() { 
        var target = $(this).html();
        dataset.lineName = $(this).attr("name");

        //Adds active class to selected item
        $(this).parents('.select-line').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        // loadSelectMO();
        $('#container1').html('');
    });
    $('.dropdown-select-mo').on( 'click', '.select-mo li a', function() { 
        var target = $(this).html();
        if(target == "Select All"){
            dataset.mo =  "";
        }
        else dataset.mo = target;
        console.log("stage",dataset.mo);
        //Adds active class to selected item
        $(this).parents('.select-mo').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-mo').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        $(".loader").removeClass("hidden");
        loadData(dataset.mo);
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
function loadDataTypeShift(typeShift){
    if(typeShift == "all" || typeShift == ""){
        dataset.shiftType = "";
        $("#a-shift").addClass("selected");
        $("#d-shift").removeClass("selected");
        $("#n-shift").removeClass("selected");
    }
    else if(typeShift == "night-shift" || typeShift == "NIGHT"){
        dataset.shiftType = "NIGHT";
        $("#n-shift").addClass("selected");
        $("#d-shift").removeClass("selected");
        $("#a-shift").removeClass("selected");
    }
    else{
        dataset.shiftType = "DAY";
        $("#d-shift").addClass("selected");
        $("#n-shift").removeClass("selected");
        $("#a-shift").removeClass("selected");
    }
    // loadData();
    $('#container1').html('');
    window.localStorage.setItem('dataset', JSON.stringify(dataset));
    var getD = moment(dataset.workDate).format("MM/DD");
    $("#txtDateTime").html(getD + " ("+dataset.shiftType+")");
}
function loadData(valueMO){
    console.log("value stage",valueMO);
    dataChart = [];
    var mo = $('input[name="mo"]').val();
    var sn = $('input[name="sn"]').val();
    var dataGet = {};
    if (valueMO == undefined) {
        console.log(valueMO);
        dataGet = {
            factory: dataset.factory,
            customer: dataset.customer,
            stage: dataset.stage,
            modelName: dataset.modelName,
            lineName: dataset.lineName,
            mo: mo,
            serial: sn,
            shiftType: dataset.shiftType,
            workDate: dataset.workDate
        }
        $('.select-mo li a').parents('.select-mo').find('li').removeClass('active');
        $('.dropdown-select-mo').find('.dropdown-toggle').html(mo + ' <span class="caret"></span>');
    }else{
        console.log(valueMO);
        dataGet = {
            factory: dataset.factory,
            customer: dataset.customer,
            stage: dataset.stage,
            modelName: dataset.modelName,
            lineName: dataset.lineName,
            mo: valueMO,
            shiftType: dataset.shiftType,
            workDate: dataset.workDate,
        }
    }
    $.ajax({
        type: "GET",
        url: "/api/test/wip-group",
        data: dataGet,
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $(".loader").removeClass("hidden");
            $("#tblDetail thead").html("");
            $("#tblDetail tbody").html("");
            if(!$.isEmptyObject(data)){
                var chart_corr;
                var dataChart = [];
                var cateX = [];
                var cateY = [];

                var header = [];
                var stationList = [];
                for(i in data){
                    if(stationList.indexOf(i) == -1){
                        stationList.push(i);
                    }
                    for(j in data[i]){
                        if(header.indexOf(j) == -1){
                            header.push(j);
                        }
                    }
                }
                cateX = header;
                cateY = stationList;
                
                var thead = "<tr><th style='min-width:65px;'>Line</th><th style='min-width:65px;'>Plan</th><th style='min-width:180px;'>Model</th>";
                for(i in header){
                    thead += "<th><a data-toggle='modal' data-target='#modalDetailWip' onclick='loadGroupHistory(\""+header[i]+"\")'>"+header[i]+"</a></th>";
                }
                thead += "</tr>";
                // $("#tblDetail thead").html(thead);

                var model_name;
                var plan;
                var tbody = '';
                for(i in data){
                    for(j in data[i]){
                        tbody += '<tr class="row_'+data[i][j].lineName+'">'
                            +  '<td>'+data[i][j].lineName+'</td>'
                            +  '<td>'+data[i][j].plan+'</td>'
                            +  '<td>'+data[i][j].modelName+'</td></tr>';
                        dataset.mo = data[i][j].mo;
                        model_name = data[i][j].modelName;
                        plan = data[i][j].plan;
                        $('input[name="mo"]').val(dataset.mo);
                        break;
                    }
                }
                // $("#tblDetail tbody").html(tbody);
                var dem1 = 0;
                var dem2 = 0;
                var dem3 = 0;
                for(i in data){
                    for(j in header){
                        if(data[i][header[j]] == null || data[i][header[j]].wip == undefined){
                            $(".row_"+i).append("<td class='noData'>N/A</td>");
                            dataChart[dem3] = [dem2,dem1,0];
                        }
                        else{
                            if(data[i][header[j]].wip == 0){
                                $(".row_"+i).append("<td class='noDataNum'>0</td>");
                            }else{
                                $(".row_"+i).append("<td class='haveData'><a data-toggle='modal' data-target='#modalDetailWip' onclick='loadDetail(\""+data[i][header[j]].groupName+"\")'><b>"+data[i][header[j]].wip+"</b></a></td>");
                            }
                            dataChart[dem3] = [dem2,dem1,data[i][header[j]].wip];
                        }
                        dem2++;
                        dem3++;
                    }
                    dem1++;
                    dem2 = 0;
                }
                
                loadChart(dataChart, cateX, cateY, model_name, plan);
                
            } else {
                $("#tblDetail thead").html("");
                $("#tblDetail tbody").html("");
                alert("NO DATA! PLEASE SELECT OR INPUT OTHER!");
            }
        },
        failure: function(errMsg) {
                console.log(errMsg);
        },
        complete: function () {
            $(".loader").addClass("hidden");
        }
    });
}

    function loadDetail(wipGroup){
        $("#modalDetailWip").modal('show');
        $('#spMONum').html('MO: '+dataset.mo);
        $.ajax({
            type: "GET",
            url: "/api/test/wip-group-detail",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                lineName: dataset.lineName,
                mo: dataset.mo,
                wipGroup: wipGroup,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $(".loader").removeClass("hidden");
                $("#tblDetailWip tbody").html("");
                if(!$.isEmptyObject(data)){
                    var tbody = '';
                    for(i in data){
                        tbody += '<tr><td>'+(Number(i)+1)+'</td>'
                            +  '<td class="haveData"><a data-toggle="modal" data-target="#modalDetailSN" onclick="loadDetailSN(\''+data[i].serialNumber+'\')">'+data[i].serialNumber+'</a></td>'
                            +  '<td>'+data[i].lineName+'</td>'
                            +  '<td>'+data[i].modelName+'</td>'
                            +  '<td>'+data[i].groupName+'</td>'
                            +  '<td>'+data[i].stationName+'</td>'
                            +  '<td>'+data[i].inStationTime+'</td>'
                            +  '<td>'+data[i].wipGroup+'</td></tr>';
                    }
                    $("#tblDetailWip tbody").html(tbody);
                    dataset.wipGroup = wipGroup;
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
            complete: function () {
                $(".loader").addClass("hidden");
            }
        });
    }

function loadDetailSN(sn){
    $.ajax({
        type: "GET",
        url: "/api/test/serial-detail",
        data: {
            factory: dataset.factory,
            mo: dataset.mo,
            wipGroup: dataset.wipGroup,
            serial: sn
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $(".loader").removeClass("hidden");
            $("#tblDetailSN tbody").html("");
            if(!$.isEmptyObject(data)){
                var tbody = '';
                for(i in data){
                    tbody += '<tr><td>'+(Number(i)+1)+'</td>'
                        +  '<td>'+data[i].serialNumber+'</td>'
                        +  '<td>'+data[i].lineName+'</td>'
                        +  '<td>'+data[i].modelName+'</td>'
                        +  '<td>'+data[i].groupName+'</td>'
                        +  '<td>'+data[i].stationName+'</td>'
                        +  '<td>'+data[i].inStationTime+'</td>'
                        +  '<td>'+data[i].wipGroup+'</td></tr>';
                }
                $("#tblDetailSN tbody").html(tbody);
            }
        },
        failure: function(errMsg) {
                console.log(errMsg);
        },
        complete: function () {
            $(".loader").addClass("hidden");
        }
    });
}

    function loadGroupHistory(wipGroup){
        $("#modalDetailWip").modal('show');
        $('#spMONum').html('MO: '+dataset.mo);
        $.ajax({
            type: "GET",
            url: "/api/test/wip-group-history",
            data: {
                factory: dataset.factory,
                mo: dataset.mo,
                wipGroup: wipGroup,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $(".loader").removeClass("hidden");
                $("#tblDetailWip tbody").html("");
                if(!$.isEmptyObject(data)){
                    var tbody = '';
                    for(i in data){
                        tbody += '<tr><td>'+(Number(i)+1)+'</td>'
                            +  '<td class="haveData"><a data-toggle="modal" data-target="#modalDetailSN" onclick="loadDetailSN(\''+data[i].serialNumber+'\')">'+data[i].serialNumber+'</a></td>'
                            +  '<td>'+data[i].lineName+'</td>'
                            +  '<td>'+data[i].modelName+'</td>'
                            +  '<td>'+data[i].groupName+'</td>'
                            +  '<td>'+data[i].stationName+'</td>'
                            +  '<td>'+data[i].inStationTime+'</td>'
                            +  '<td>'+data[i].wipGroup+'</td></tr>';
                    }
                    $("#tblDetailWip tbody").html(tbody);
                    dataset.wipGroup = wipGroup;
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
            complete: function () {
                $(".loader").addClass("hidden");
            }
        });
    }

    $("#txtFilterWip").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblDetailWip tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    $("#txtFilterSN").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblDetailSN tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    var tableToExcel = (function() {
      var uri = 'data:application/vnd.ms-excel;base64,'
        , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
        , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
        , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
      return function(table, name) {
        if (!table.nodeType) table = document.getElementById(table)
        var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
        var blob = new Blob([format(template, ctx)]);
      var blobURL = window.URL.createObjectURL(blob);
        return blobURL;
      }
    })();
$("#btnExport").click(function () {
    var tab_text = "<table border='1px'><tr bgcolor='#F79646'>";
    var textRange; var j = 0;
    tab = document.getElementById('tblDetail'); // id of table

    for (j = 0 ; j < tab.rows.length ; j++) {
        tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
        //tab_text=tab_text+"";
    }

    tab_text = tab_text + "</table>";
    tab_text = tab_text.replace(/<a[^>]*>|<\/A>/g, "");//remove if u want links in your table
    tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
    tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");

    var sa;
    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
    {
        txtArea1.document.open("txt/html", "replace");
        txtArea1.document.write(tab_text);
        txtArea1.document.close();
        txtArea1.focus();
        sa = txtArea1.document.execCommand("SaveAs", true, "WIP GROUP");
        return (sa);
    }
    else {
        var a = document.createElement('a');
        a.href = 'data:application/vnd.ms-excel,' + encodeURIComponent(tab_text);
        a.download = 'WIP GROUP';
        a.click();
    }
});
function loadChart(dataChart, cateX, cateY, model_name, plan){
    chart_corr = new Highcharts.Chart({
        chart: {
            type: 'heatmap',
            renderTo: "container1",
            style: {
                fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
            },
            events: {
                load() {
                    let chart = this;
                    chart.xAxis[0].labelGroup.element.childNodes.forEach(function(label) {
                        label.style.cursor = "pointer";
                        label.onclick = function() {
                            loadGroupHistory(this.textContent);
                        }
                    });
                }
            }
        },

        title: {
            text: 'Model : ' + model_name + ' ( Plan : ' + plan + ' )',
            style: {
                fontSize: '16px',
                fontWeight: 'bold'
            }
        },

        xAxis: {
            categories: cateX,
            labels: {
                rotation: -45,
                align: 'right',
            },
        },

        yAxis: {
            categories: cateY,
            title: null
        },

        colorAxis: {
            // dataClassColor: 'category',
            dataClasses: [{
                to: 10
            }, {
                from: 10,
                to: 20
            }, {
                from: 20,
                to: 40
            }, {
                from: 40,
                to: 100
            }, {
                from: 100,
                to: 300
            }, {
                from: 300,
                to: 500
            }, {
                from: 500,
                to: 1000
            }, {
                from: 1000,
                to: 2000
            }, {
                from: 2000,
                to: 3000
            }, {
                from: 3000
            }],
            minColor: '#FFFFFF',
            // maxColor: Highcharts.getOptions().colors[0]
            maxColor: '#FF7200'
        },

        legend: {
            enabled: true,
            layout: 'horizontal',
            align: 'right',
            verticalAlign: 'bottom',
            // marginTop: -5,
            y: -5,
            // symbolHeight: 500
        },
        plotOptions: {
            series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function (event) {
                            var index = this.x;
                            var wipGroupClick = cateX[index];
                            loadDetail(wipGroupClick);
                        }
                    }
                }

            },
        },

        tooltip: {
            useHTML: true,
            formatter: function () {
                return '<b>' + this.series.xAxis.categories[this.point.x] + '</b> <br><b>' + this.point.value + '</b> pcs on <b>' + this.series.yAxis.categories[this.point.y] + '</b>';
            }
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
            name: '',
            borderWidth: 1,
            borderColor: '#CCCCCC',
            data: dataChart,
            dataLabels: {
                enabled: true,
                color: 'black',
                style: {
                    textShadow: 'none',
                    HcTextStroke: null
                }
            }
        }]
    });
}

    $(document).ready(function() {
        $('.loader').addClass('hidden');
        $('.select-mo li a').parents('.select-mo').find('li').removeClass('active');
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
        $('input[name=timeSpan]').on('change', function() {
            dataset.workDate = this.value;
            dataset.lineName = "";
            dataset.mo = "";
            $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
            $('.dropdown-select').find('.dropdown-toggle').html('Line <span class="caret"></span>');
            window.localStorage.setItem('dataset', JSON.stringify(dataset));

            init();

            $('#container1').html('');

            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
        });
    });
</script>