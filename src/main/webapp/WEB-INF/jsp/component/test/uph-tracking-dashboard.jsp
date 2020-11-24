<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<style>
    .yieldRate{
        font-size: 14px !important;
        font-weight: bold;
    }
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
    #tblDetail thead tr:nth-of-type(1) th{
        z-index: 11;
    }
    #tblDetail thead tr:nth-of-type(2) th{
        z-index: 1;
    }
</style>
<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span id="titlePage"></span><span> UPH Tracking Dashboard </span><span id="txtDateTime"></span></b>
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
            <div class="col-xs-1" style="text-align: right;">
                <button id="btnQuarter" class="btn select-shift" onclick="activeQuarter(this)" style="color: #FFF;">15'</button>
                <button id="btnYieldRate" class="btn select-shift" onclick="activeYieldRate(this)" style="color: #FFF;">Y.R</button>
            </div>
            <div class="col-xs-1">
                <div class="warningSetting">
                    <ion-nav-buttons class="pull-right" data-toggle="modal" data-target="#modalSetup" style="padding-top: 10px;" onclick="setupCustomers()">
                        <i class="fa fa-cog" title="Click to setting" data-toggle="modal" style="font-size: 20px; cursor: pointer;"></i>
                    </ion-nav-buttons>
                </div>
                <div class="export pull-right">
                    <a id="btnExport" class="pull-right" style="padding-top: 10px; margin-right: 10px;">
                        <i class="fa fa-download" style="font-size: 20px; color: #cccccc;" title="Export to Excel"></i>
                    </a>
                    <!-- <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                        <i class="fa fa-download"></i>
                    </a> -->
                </div>
            </div>
        </div>
        <div class="row no-margin">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 175px);color: #ccc; padding: unset;">
                <table id="tblDetail" class="table table-xxs table-bordered table-sticky" style="text-align: center">
                </table>
            </div>
        </div>
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>

<!-- Modal -->
<div id="mydiv" style="font-size: 18px; min-width: 740px;">
    <div id="mydivheader">
        <span style="font-weight:bold">Set UPH Target</span>
        <a title="Close" style="float: right; margin-right: 5px; color: #fff;" onclick="closeModal()">
            <i class="icon icon-cross"></i>
        </a>
    </div>
    <div class="row" style="color: #333333; margin: 0; padding: 5px;">
        <button id="btnAddNew" class="btn btn-lg" style="float: left; padding: 5px 10px; color: #ccc; background: #272727; border: 1px solid #fff;" onclick="addNewClick()">
        <i class="fa fa-plus"></i> Add
    </button>
        <input id="txtSearch" class="form-control" type="search" placeholder="Search by Moldel or Group" style="float: right; padding: 5px 10px; border-radius: 4px; color: #ccc; background: #272727; width: 40%; height: 32px;" />
    </div>
    <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 250px);">
        <table id="tblTarget" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px;">
            <thead>
                <tr>
                    <th style="width: 10%; text-align: center; background: #f1f1f1;">STT</th>
                    <th style="width: 30%; text-align: center; background: #f1f1f1;">Model Name</th>
                    <th style="width: 20%; text-align: center; background: #f1f1f1;">Group Name</th>
                    <th style="width: 20%; text-align: center; background: #f1f1f1;">UPH</th>
                    <th style="width: 20%; text-align: center; background: #f1f1f1; z-index: 1;">Action</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
    <div id="mydivfooter" style="text-align: right;">
        <a title="Close" class="btn" style="float: right; margin-right: 5px; color: #000;" onclick="closeModal()">Close</a>
    </div>
</div>

<div class="modal fade" id="modalSetup" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #333; color: #ccc;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
                <div class="modal-title">
                    <div class="btn-group" style="margin: 0 15px; float:left">
                        <button id="btnSetCus" class="btn btn-sm select-shift selected" onclick="setupCustomers()">Setup Customer</button>
                        <button id="btnSetGroup" class="btn btn-sm select-shift" onclick="setupGroup()">Setup Group</button>
                    </div>
                    <input id="txtFilterSetup" class="form-control" type="text" placeholder="Filter" style="float: right; padding: 5px 10px; border-radius: 4px; color: #fff; background: #272727; width: 20%; height: 32px; margin-right: 5%;" />
                </div>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="color: #333333; max-height: 500px;">
                    <table id="tblModelMeta" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px; color: #fff; text-align: center; table-layout: fixed">
                        <thead></thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" style="font-size: 13px; background: #333333; color: #ccc">Close</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="/assets/custom/js/nbb/getTimeNow.js"></script>
<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if (dataset == null || dataset == undefined || dataset.path != "${path}" || dataset.factory != "${factory}") {
        dataset = {
            stage: "",
            lineName: "",
            mo: "",
            yr: false,
            path: "${path}",
            factory: "${factory}"
        }
        getTimeNow();
    } else {
        getTimeNow();
    }

    var dataModelNames = [];
    var dataGroupNames = [];

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
                loadDataTypeShift(dataset.shiftType);
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
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
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
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(!$.isEmptyObject(data)){
                    if(dataset.factory != "nbb" && dataset.factory != "s03"){
                        if(dataset.modelName == "" || dataset.modelName == undefined){
                            dataset.modelName = data[0];
                            window.localStorage.setItem('dataset', JSON.stringify(dataset));
                        }
                        loadDataTypeShift(dataset.shiftType);
                    }
                    var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
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
                    selector.val(dataset.modelName);
                    selector.selectpicker('refresh');
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                loadSelectLine();
                loadSelectMO();
            }
        });
    }

    function loadSelectLine(){
        $.ajax({
            type: "GET",
            url: "/api/test/sfc/line",
            data: {
                factory: dataset.factory,
                workDate: dataset.workDate,
                shiftType: dataset.shiftType,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                //mo: dataset.mo
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="">Select All</a></li>';
                for(i in data){
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="'+i+'">'+ data[i] +'</a></li>';
                    if(i == dataset.lineName){
                        $('.dropdown-select').find('.dropdown-toggle').html(data[i] + ' <span class="caret"></span>');
                    }
                }
                $(".select-line").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function loadSelectMO(){
        $.ajax({
            type: "GET",
            url: "/api/test/sfc/mo",
            data: {
                factory: dataset.factory,
                workDate: dataset.workDate,
                shiftType: dataset.shiftType,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="">Select All</a></li>';
                for(i in data){
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="'+i+'">'+ data[i] +'</a></li>';
                    if(data[i] == dataset.mo){
                        $('.dropdown-select-mo').find('.dropdown-toggle').html(data[i] + ' <span class="caret"></span>');
                    }
                }
                $(".select-mo").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
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
        $('.dropdown-select-stage').find('.dropdown-toggle').html('Stage <span class="caret"></span>');
        loadSelectStage();
        dataset.modelName = "";
        $('.dropdown-select-modelName').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');
        // loadSelectModel();
        dataset.mo = "";
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
        // loadSelectMO();
        dataset.lineName = "";
        $('.dropdown-select').find('.dropdown-toggle').html('Line <span class="caret"></span>');
        // loadSelectLine();
        $(".loader").removeClass("disableSelect");
        $("#titlePage").html(dataset.customer.toUpperCase());

        closeModal();
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
        $('.dropdown-select-model').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');
        loadSelectModel();
        dataset.mo = "";
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
        // loadSelectMO();
        dataset.lineName = "";
        $('.dropdown-select').find('.dropdown-toggle').html('Line <span class="caret"></span>');
        // loadSelectLine();

        $(".loader").removeClass("disableSelect");
        closeModal();
        loadData();
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
        $('.dropdown-select').find('.dropdown-toggle').html('Line <span class="caret"></span>');
        loadSelectLine();
        dataset.mo = "";
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
        loadSelectMO();

        $(".loader").removeClass("disableSelect");
        loadData();
        closeModal();
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

        $(".loader").removeClass("disableSelect");
        loadData();
        closeModal();
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

        $(".loader").removeClass("disableSelect");
        loadData();
        closeModal();
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
        closeModal();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    function loadDataTypeShift(typeShift) {
        dataset.shiftType = typeShift;
        if (typeShift == "") {
            $("#a-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#n-shift").removeClass("selected");
        } else if (typeShift == "NIGHT") {
            $("#n-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
        } else {
            $("#d-shift").addClass("selected");
            $("#n-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
        }
        $(".loader").removeClass("disableSelect");
        loadData();
        closeModal();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        var getD = moment(dataset.workDate).format("MM/DD");
        $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
    }

    function loadData() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/group/uph-dashboard",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                lineName: dataset.lineName,
                mo: dataset.mo,
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
                quarter: dataset.quarter
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                if(!$.isEmptyObject(data)){
                    if(dataset.yr){
                        loadDataWithYieldRate(data);
                    } else{
                        loadDataNormal(data);
                    }
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                // dataset.timeout = setTimeout(loadData, 30000, dataset);
                $(".loader").addClass("disableSelect");
            }
        });
    }

    function loadDataNormal(data){
        $("#tblDetail").html("");
        var header = Object.keys(data);
        dataGroupNames = header;
        var thead = '<thead><tr><th width="8%">Time</th>';
        for (i in header) {
            thead += '<th>' + header[i] + '</th>';
        }
        thead += '</tr></thead><tbody>';
        var dataTime = [];
        var dataOnlyTime = [];
        if (dataset.shiftType == "DAY") {
            if(dataset.quarter){
                dataTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30"];
            } else{
                dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30"];
            }
        } else if (dataset.shiftType == "") {
            if(dataset.quarter){
                dataTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30", "19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30", "19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30"];
            } else{
                dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30"];
            }
        } else {
            if(dataset.quarter){
                dataTime = ["19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30"];
            } else{
                dataTime = ["19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30"];
            }
        }

        thead += '<tr class="target" onclick="showModal()" title="Click to Set UPH Target"><td>UPH Target</td></tr>';
        thead += '<tr class="accumulate"><td>Accumulated</td></tr>';
        for(let i=0; i<dataOnlyTime.length; i++){
            thead += '<tr class="time'+i+'"><td>'+dataOnlyTime[i]+'</td></tr>';
        }
        thead += '<tr class="uph-average"><td>UPH/Average</td></tr></tbody>';
        $("#tblDetail").append(thead);

        for (i in data) {
            for (j in dataTime) {
                if (data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined) {
                    $(".time" + j).append("<td class='noData'>N/A</td>");
                } else {
                    // $(".time" + j).append("<td><b>" + data[i][dataTime[j]] + "</b></td>");
                    var yieldRate = 0;
                    if((data[i][dataTime[j]].pass + data[i][dataTime[j]].fail) != 0){
                        yieldRate = data[i][dataTime[j]].pass / (data[i][dataTime[j]].pass + data[i][dataTime[j]].fail)*100;
                    }
                    if(data[i]["uph-target"].pass != 0){
                        var uphValue = 0;
                        if(dataset.quarter){
                            uphValue = (data[i][dataTime[j]].pass*4)/data[i]["uph-target"].pass*100;
                        } else uphValue = data[i][dataTime[j]].pass/data[i]["uph-target"].pass*100;
                        if(uphValue < 90){
                            $(".time" + j).append("<td class='bg-danger-custom'><b class='uphValue'>" + data[i][dataTime[j]].pass + "</b><span class='yieldRate hidden'>("+yieldRate.toFixed(2)+"%)</span></td>");
                        } else if(uphValue >= 90 && uphValue <100){
                            $(".time" + j).append("<td class='bg-yellow' style='color: #333333'><b class='uphValue'>" + data[i][dataTime[j]].pass + "</b><span class='yieldRate hidden'>("+yieldRate.toFixed(2)+"%)</span></td>");
                        } else{
                            $(".time" + j).append("<td class='bg-success-custom'><b class='uphValue'>" + data[i][dataTime[j]].pass + "</b><span class='yieldRate hidden'>("+yieldRate.toFixed(2)+"%)</span></td>");
                        }
                    } else{
                        $(".time" + j).append("<td><b class='uphValue'>" + data[i][dataTime[j]].pass + "</b><span class='yieldRate hidden'>("+yieldRate.toFixed(2)+"%)</span></td>");
                    }
                }
            }
            if (data[i]["uph-target"].pass == 0 || data[i]["uph-target"].pass == null){
                data[i]["uph-target"].pass = "N/A";
            }
            $(".target").append("<td>" + data[i]["uph-target"].pass + "</td>");
            var yieldRateAc = 0;
            var yieldRateAv = 0;
            if((data[i]["accumulate"].pass + data[i]["accumulate"].fail) != 0){
                yieldRateAc = data[i]["accumulate"].pass / (data[i]["accumulate"].pass + data[i]["accumulate"].fail)*100;
            }
            if((data[i]["uph-average"].pass + data[i]["uph-average"].fail) != 0){
                yieldRateAv = data[i]["uph-average"].pass / (data[i]["uph-average"].pass + data[i]["uph-average"].fail)*100;
            }
            $(".accumulate").append("<td><b class='uphValue'>" + data[i]["accumulate"].pass + "</b><span class='yieldRate hidden'>("+yieldRateAc.toFixed(2)+"%)</span></td>");
            $(".uph-average").append("<td><b class='uphValue'>" + data[i]["uph-average"].pass + "</b><span class='yieldRate hidden'>("+yieldRateAv.toFixed(2)+"%)</span></td>");
        }

        $("#tblDetail").append("<div id='phantomjsMark'></div>");
        $('#btnYieldRate').removeClass('selected');
    }

    function loadDataWithYieldRate(data) {
        $(".loader").removeClass("disableSelect");
        $("#tblDetail").html("");
        var header = Object.keys(data);
        dataGroupNames = header;
        var thead = '<thead><tr><th width="8%">Time</th>';
        var thead2 = '<tr><th width="8%"></th>';
        for (i in header) {
            thead += '<th colspan="2">' + header[i] + '</th>';
            thead2 += '<th>UPH</th><th>Y.R</th>';
        }
        thead += '</tr>';
        thead2 += '</tr></thead><tbody>';
        thead += thead2;
        var dataTime = [];
        var dataOnlyTime = [];
        if (dataset.shiftType == "DAY") {
            if(dataset.quarter){
                dataTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30"];
            } else{
                dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30"];
            }
        } else if (dataset.shiftType == "") {
            if(dataset.quarter){
                dataTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30", "19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 07:45", "07:45 - 08:00", "08:00 - 08:15", "08:15 - 08:30", "08:30 - 08:45", "08:45 - 09:00", "09:00 - 09:15", "09:15 - 09:30", "09:30 - 09:45", "09:45 - 10:00", "10:00 - 10:15", "10:15 - 10:30", "10:30 - 10:45", "10:45 - 11:00", "11:00 - 11:15", "11:15 - 11:30", "11:30 - 11:45", "11:45 - 12:00", "12:00 - 12:15", "12:15 - 12:30", "12:30 - 12:45", "12:45 - 13:00", "13:00 - 13:15", "13:15 - 13:30", "13:30 - 13:45", "13:45 - 14:00", "14:00 - 14:15", "14:15 - 14:30", "14:30 - 14:45", "14:45 - 15:00", "15:00 - 15:15", "15:15 - 15:30", "15:30 - 15:45", "15:45 - 16:00", "16:00 - 16:15", "16:15 - 16:30", "16:30 - 16:45", "16:45 - 17:00", "17:00 - 17:15", "17:15 - 17:30", "17:30 - 17:45", "17:45 - 18:00", "18:00 - 18:15", "18:15 - 18:30", "18:30 - 18:45", "18:45 - 19:00", "19:00 - 19:15", "19:15 - 19:30", "19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30"];
            } else{
                dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30"];
            }
        } else {
            if(dataset.quarter){
                dataTime = ["19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["19:30 - 19:45", "19:45 - 20:00", "20:00 - 20:15", "20:15 - 20:30", "20:30 - 20:45", "20:45 - 21:00", "21:00 - 21:15", "21:15 - 21:30", "21:30 - 21:45", "21:45 - 22:00", "22:00 - 22:15", "22:15 - 22:30", "22:30 - 22:45", "22:45 - 23:00", "23:00 - 23:15", "23:15 - 23:30", "23:30 - 23:45", "23:45 - 00:00", "00:00 - 00:15", "00:15 - 00:30", "00:30 - 00:45", "00:45 - 01:00", "01:00 - 01:15", "01:15 - 01:30", "01:30 - 01:45", "01:45 - 02:00", "02:00 - 02:15", "02:15 - 02:30", "02:30 - 02:45", "02:45 - 03:00", "03:00 - 03:15", "03:15 - 03:30", "03:30 - 03:45", "03:45 - 04:00", "04:00 - 04:15", "04:15 - 04:30", "04:30 - 04:45", "04:45 - 05:00", "05:00 - 05:15", "05:15 - 05:30", "05:30 - 05:45", "05:45 - 06:00", "06:00 - 06:15", "06:15 - 06:30", "06:30 - 06:45", "06:45 - 07:00", "07:00 - 07:15", "07:15 - 07:30"];
            } else{
                dataTime = ["19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30", "accumulate", "uph-average", "uph-target"];
                dataOnlyTime = ["19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30"];
            }
        }

        thead += '<tr class="target" onclick="showModal()" title="Click to Set UPH Target"><td>UPH Target</td></tr>';
        thead += '<tr class="accumulate"><td>Accumulated</td></tr>';
        for(let i=0; i<dataOnlyTime.length; i++){
            thead += '<tr class="time'+i+'"><td>'+dataOnlyTime[i]+'</td></tr>';
        }
        thead += '<tr class="uph-average"><td>UPH/Average</td></tr></tbody>';
        $("#tblDetail").append(thead);

        for (i in data) {
            for (j in dataTime) {
                if (data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined) {
                    $(".time" + j).append("<td class='noData'>N/A</td><td class='noData'>N/A</td>");
                } else {
                    // $(".time" + j).append("<td><b>" + data[i][dataTime[j]] + "</b></td>");
                    var yieldRate = 0;
                    if((data[i][dataTime[j]].pass + data[i][dataTime[j]].fail) != 0){
                        yieldRate = data[i][dataTime[j]].pass / (data[i][dataTime[j]].pass + data[i][dataTime[j]].fail)*100;
                    }
                    if(data[i]["uph-target"].pass != 0){
                        var uphValue = 0;
                        if(dataset.quarter){
                            uphValue = (data[i][dataTime[j]].pass*4)/data[i]["uph-target"].pass*100;
                        } else uphValue = data[i][dataTime[j]].pass/data[i]["uph-target"].pass*100;
                        if(uphValue < 90){
                            $(".time" + j).append("<td class='bg-danger-custom'><b>" + data[i][dataTime[j]].pass + "</b></td>");
                            if(yieldRate <= 97){
                                $(".time" + j).append("<td class='bg-danger-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            } else if(yieldRate > 97 && yieldRate < 100){
                                $(".time" + j).append("<td class='bg-yellow yieldRate' style='color: #333333'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            } else{
                                $(".time" + j).append("<td class='bg-success-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            }
                        } else if(uphValue >= 90 && uphValue <100){
                            $(".time" + j).append("<td class='bg-yellow' style='color: #333333'><b>" + data[i][dataTime[j]].pass + "</b></td>");
                            if(yieldRate <= 97){
                                $(".time" + j).append("<td class='bg-danger-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            } else if(yieldRate > 97 && yieldRate < 100){
                                $(".time" + j).append("<td class='bg-yellow yieldRate' style='color: #333333'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            } else{
                                $(".time" + j).append("<td class='bg-success-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            }
                        } else{
                            $(".time" + j).append("<td class='bg-success-custom'><b>" + data[i][dataTime[j]].pass + "</b></td>");
                            if(yieldRate <= 97){
                                $(".time" + j).append("<td class='bg-danger-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            } else if(yieldRate > 97 && yieldRate < 100){
                                $(".time" + j).append("<td class='bg-yellow yieldRate' style='color: #333333'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            } else{
                                $(".time" + j).append("<td class='bg-success-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                            }
                        }
                    } else{
                        $(".time" + j).append("<td><b>" + data[i][dataTime[j]].pass + "</b></td>");
                        if(yieldRate <= 97){
                            $(".time" + j).append("<td class='bg-danger-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                        } else if(yieldRate > 97 && yieldRate < 100){
                            $(".time" + j).append("<td class='bg-yellow yieldRate' style='color: #333333'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                        } else{
                            $(".time" + j).append("<td class='bg-success-custom yieldRate'><span>"+yieldRate.toFixed(2)+"%</span></td>");
                        }
                    }
                }
            }
            if (data[i]["uph-target"].pass == 0 || data[i]["uph-target"].pass == null){
                data[i]["uph-target"].pass = "N/A";
            }
            $(".target").append("<td colspan='2'>" + data[i]["uph-target"].pass + "</td>");
            var yieldRateAc = 0;
            var yieldRateAv = 0;
            if((data[i]["accumulate"].pass + data[i]["accumulate"].fail) != 0){
                yieldRateAc = data[i]["accumulate"].pass / (data[i]["accumulate"].pass + data[i]["accumulate"].fail)*100;
            }
            if((data[i]["uph-average"].pass + data[i]["uph-average"].fail) != 0){
                yieldRateAv = data[i]["uph-average"].pass / (data[i]["uph-average"].pass + data[i]["uph-average"].fail)*100;
            }
            $(".accumulate").append("<td><b>" + data[i]["accumulate"].pass + "</b></td>");
            if(yieldRateAc <= 97){
                $(".accumulate").append("<td class='bg-danger-custom yieldRate'><span>"+yieldRateAc.toFixed(2)+"%</span></td>");
            } else if(yieldRateAc > 97 && yieldRateAc < 100){
                $(".accumulate").append("<td class='bg-yellow yieldRate' style='color: #333333'><span>"+yieldRateAc.toFixed(2)+"%</span></td>");
            } else{
                $(".accumulate").append("<td class='bg-success-custom yieldRate'><span>"+yieldRateAc.toFixed(2)+"%</span></td>");
            }
            $(".uph-average").append("<td><b>" + data[i]["uph-average"].pass + "</b></td>");
            if(yieldRateAv <= 97){
                $(".uph-average").append("<td class='bg-danger-custom yieldRate'><span>"+yieldRateAv.toFixed(2)+"%</span></td>");
            } else if(yieldRateAv > 97 && yieldRateAv < 100){
                $(".uph-average").append("<td class='bg-yellow yieldRate' style='color: #333333'><span>"+yieldRateAv.toFixed(2)+"%</span></td>");
            } else{
                $(".uph-average").append("<td class='bg-success-custom yieldRate'><span>"+yieldRateAv.toFixed(2)+"%</span></td>");
            }
        }

        $(".loader").addClass("disableSelect");
        $("#tblDetail").append("<div id='phantomjsMark'></div>");
    }

    function activeQuarter(context){
        if(context.className.indexOf('selected') == -1){
            dataset.quarter = true;
            $('#btnQuarter').addClass('selected');
            $(".loader").removeClass("disableSelect");
            loadData();
        } else{
            dataset.quarter = false;
            $('#btnQuarter').removeClass('selected');
            $('.loader').removeClass('disableSelect');
            loadData();
        }
    }
    function activeYieldRate(context){
        if(context.className.indexOf('selected') == -1){
            $('#btnYieldRate').addClass('selected');
            dataset.yr = true;
            $('.loader').removeClass('disableSelect');
            loadData();
        } else{
            $('#btnYieldRate').removeClass('selected');
            // $('.yieldRate').addClass('hidden');
            // $('.uphValue').removeClass('hidden');
            dataset.yr = false;
            $('.loader').removeClass('disableSelect');
            loadData();
        }
    }

    function loadDataModelNames() {
        $.ajax({
            type: "GET",
            url: "/api/test/sfc/model",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                dataModelNames = data;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function showModal() {
        loadDataModelNames();
        var shift = dataset.shiftType;
        if (shift == "") {
            shift = "DAY";
        }
        $("#mydiv").addClass("show");
        $.ajax({
            type: "GET",
            url: "/api/test/uph",
            data: {
                factory: dataset.factory,
                shiftType: shift,
                workDate: dataset.workDate,
                customer: dataset.customer,
                modelName: dataset.modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#tblTarget tbody").html("");
                var stt = 0;
                var html = '';
                var listModelNames = '<select class="form-control selectModels">';
                for (i in dataModelNames) {
                    listModelNames += '<option value="' + dataModelNames[i] + '">' + dataModelNames[i] + '</option>';
                }
                listModelNames += '</select>';

                var listGroupNames = '<select class="form-control selectGroups"';
                for (i in dataGroupNames) {
                    listGroupNames += '<option value="' + dataGroupNames[i] + '">' + dataGroupNames[i] + '</option>';
                }
                listGroupNames += '</select>';

                if (data.length > 0) {
                    for (i in data) {
                        stt++;
                        html = '<tr id="row' + data[i].id + '">' +
                            '<td>' + stt + '</td>' +
                            '<td><div id="txtEditModel' + data[i].id + '">' + data[i].modelName + '</div></td>' +
                            '<td><div id="txtEditGroup' + data[i].id + '">' + data[i].groupName + '</div></td>' +
                            '<td><input id="txtEditUPH' + data[i].id + '" class="form-control hidden" name="uph" type="text" value="' + data[i].uph + '" /><span id="uph' + data[i].id + '">' + data[i].uph + '</span></td>' +
                            '<td>' +
                            '<button class="btn btn-warning" id="editRow' + data[i].id + '" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit(' + data[i].id + ')">' +
                            '<i class="icon icon-pencil"></i>' +
                            '</button>' +
                            '<button class="btn btn-danger" id="deleteRow' + data[i].id + '" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel(' + data[i].id + ')">' +
                            '<i class="icon icon-trash"></i>' +
                            '</button>' +
                            '<button class="btn btn-success hidden" id="confirmRow' + data[i].id + '" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel(' + data[i].id + ')">' +
                            '<i class="icon icon-checkmark"></i>' +
                            '</button><button class="btn btn-danger hidden" id="cancelRow' + data[i].id + '" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit(' + data[i].id + ')">' +
                            '<i class="icon icon-spinner11"></i>' +
                            '</button>' +
                            '</td>' +
                            '</tr>';
                        $("#tblTarget tbody").append(html);
                    }
                    dataset.noTarget = "false";
                } else {
                    $("#tblTarget tbody").html("<tr><td colspan='5'>NO TARGET!!</td></tr>");
                    dataset.noTarget = "true";
                }

            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function closeModal() {
        $("#mydiv").removeClass("show");
        $("#btnAddNew").removeAttr("disabled");
    }

    //Filter DragModal
    $("#txtSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblTarget tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    function addNewClick() {
        if (dataset.noTarget == "true") {
            $("#tblTarget tbody").html("");
        }
        var listModelNames = '<select class="form-control selectModelsAdd">';
        for (i in dataModelNames) {
            listModelNames += '<option value="' + dataModelNames[i] + '">' + dataModelNames[i] + '</option>';
        }
        listModelNames += '</select>';

        var listGroupNames = '<select class="form-control selectGroupsAdd">';
        for (i in dataGroupNames) {
            listGroupNames += '<option value="' + dataGroupNames[i] + '">' + dataGroupNames[i] + '</option>';
        }
        listGroupNames += '</select>';

        var html = '<tr id="newRow"><td></td><td id="newModel">' + listModelNames + '</td>' +
            '<td id="newGroup">' + listGroupNames + '</td>' +
            '<td id="newuph"><input id="txtAddUPH" class="form-control" type="text"/></td>' +
            '<td><button class="btn btn-success" title="Add" style="padding: 4px 10px;" onclick="addNew()"><i class="icon icon-checkmark"></i></button>' +
            '<button class="btn btn-danger" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelAddNew()"><i class="icon icon-cross"></i></button></td></tr>';
        $("#tblTarget tbody").append(html);
        $("#txtAddUPH").focus();
        $("#btnAddNew").attr("disabled", "disabled");
        $(".btn-warning").attr("disabled", "disabled");
    }

    function addNew() {
        var newModel = $('select.selectModelsAdd option:selected').val();
        var newGroup = $('select.selectGroupsAdd option:selected').val();
        var newUPH = Number($("#txtAddUPH").val());
        var shift = dataset.shiftType;
        if (shift == "") {
            shift = "DAY";
        }
        var data = {
            factory: dataset.factory,
            modelName: newModel,
            groupName: newGroup,
            workDate: dataset.workDate,
            shiftType: shift,
            workTime: 12,
            uph: newUPH,
            customer: dataset.customer,
            lineName: ""
        }

        $.ajax({
            type: 'POST',
            url: '/api/test/uph',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                showModal();
                loadData();
                $("#btnAddNew").removeAttr("disabled");
                $(".btn-warning").removeAttr("disabled");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function cancelAddNew() {
        $("#newRow").remove();
        $("#btnAddNew").removeAttr("disabled");
        $(".btn-warning").removeAttr("disabled");
    }

    function edit(stt) {
        $("#deleteRow" + stt).addClass("hidden");
        $("#editRow" + stt).addClass("hidden");
        $("#confirmRow" + stt).removeClass("hidden");
        $("#cancelRow" + stt).removeClass("hidden");

        $("#uph" + stt).addClass("hidden");
        $("#txtEditUPH" + stt).removeClass("hidden");
        $("#btnAddNew").attr("disabled", "disabled");

        $(".btn-warning").attr("disabled", "disabled");
        $("#txtSearch").attr("disabled", "disabled");
    }

    function cancelEdit(stt) {
        $("#deleteRow" + stt).removeClass("hidden");
        $("#editRow" + stt).removeClass("hidden");
        $("#confirmRow" + stt).addClass("hidden");
        $("#cancelRow" + stt).addClass("hidden");

        $("#uph" + stt).removeClass("hidden");
        $("#txtEditUPH" + stt).addClass("hidden");
        $("#btnAddNew").removeAttr("disabled");

        $(".btn-warning").removeAttr("disabled");
        $("#txtSearch").removeAttr("disabled");
    }

    function confirmModel(stt) {
        var confirm = window.confirm("Are you sure CHANGE?");
        if (confirm == true) {
            var shift = dataset.shiftType;
            if (shift == "") {
                shift = "DAY";
            }
            var editModel = $('select.selectModels option:selected').val();
            var editGroup = $('select.selectGroups option:selected').val();
            var editUPH = Number($("#txtEditUPH" + stt).val());
            var data = {
                factory: dataset.factory,
                modelName: editModel,
                groupName: editGroup,
                workDate: dataset.workDate + " 00:00:00",
                shiftType: shift,
                workTime: 12,
                uph: editUPH,
                customer: dataset.customer,
                lineName: ""
            }

            $.ajax({
                type: 'PUT',
                url: '/api/test/uph/' + stt,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    showModal();
                    loadData();
                    $("#btnAddNew").removeAttr("disabled");
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }

    function deleteModel(stt) {
        var del = window.confirm("Do you want to DELETE?");
        if (del == true) {
            $.ajax({
                type: 'DELETE',
                url: '/api/test/uph/' + stt,
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    showModal();
                    loadData();
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
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
                sa = txtArea1.document.execCommand("SaveAs", true, "UPH Tracking.xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblDetail', 'uph_table');
            $(this).attr('download', 'UPH Tracking (' + dataset.shiftType + ').xls')
            $(this).attr('href', blobURL);
        }
    });

    function setupCustomers() {
        window.location.href = "/manager-group?factory="+dataset.factory;
        $("#btnSetCus").addClass("selected");
        $("#btnSetGroup").removeClass("selected");
        //   $(".modal-dialog").removeClass("modal-xl");
        $.ajax({
            type: "GET",
            url: "/api/test/modelMeta",
            data: {
                factory: dataset.factory
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var html = '';
                var stt = 0;
                if (data != null || data != undefined) {
                    var thead = '<tr>' +
                        '<th style="width: 10%; text-align: center; background: #444;">STT</th>' +
                        '<th style="width: 15%; text-align: center; background: #444;">Customer</th>' +
                        '<th style="width: 25%; text-align: center; background: #444;">Model Name</th>' +
                        '<th style="width: 15%; text-align: center; background: #444;">Stage</th>' +
                        '<th style="width: 15%; text-align: center; background: #444;">Sub Stage</th>' +
                        '<th style="width: 20%; text-align: center; background: #444; z-index:10">Action</th>' +
                        '</tr>';
                    $("#tblModelMeta thead").html(thead);
                    $("#tblModelMeta tbody").html("");
                    for (i in data) {
                        stt++;
                        html = '<tr id="row' + data[i].id + '">' +
                            '<td>' + stt + '</td>' +
                            '<td><div id="sbEditCustomer' + data[i].id + '"></div><span id="txtCustomers' + data[i].id + '">' + data[i].customer + '</span></td>' +
                            '<td><span id="txtSetupModelName' + data[i].id + '">' + data[i].modelName + '</span></td>' +
                            '<td><span id="txtSetupStage' + data[i].id + '">' + data[i].stage + '</span><span id="txtSetupStage' + data[i].id + '" class="disableSelect">' + data[i].Stage + '</span></td>' +
                            '<td><span id="txtSetupSubStage' + data[i].id + '">' + data[i].subStage + '</span><span id="txtSetupSubStage' + data[i].id + '" class="disableSelect">' + data[i].subStage + '</span></td>' +
                            '<td>' +
                            '<button class="btn btn-warning" id="btnEditCustomer' + data[i].id + '" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="editCustomer(' + data[i].id + ')">' +
                            '<i class="icon icon-pencil"></i>' +
                            '</button>' +
                            '<button class="btn btn-success disableSelect" id="confirmEditCustomer' + data[i].id + '" title="Confirm" style="padding: 4px 10px;" onclick="confirmEditCus(' + data[i].id + ')">' +
                            '<i class="icon icon-checkmark"></i>' +
                            '</button><button class="btn btn-danger disableSelect" id="cancelEditCustomer' + data[i].id + '" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEditCus(' + data[i].id + ')">' +
                            '<i class="icon icon-spinner11"></i>' +
                            '</button>' +
                            '</td>' +
                            '</tr>';
                        $("#tblModelMeta tbody").append(html);
                    }
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function editCustomer(id) {
        $("#btnEditCustomer" + id).addClass("disableSelect");
        $("#confirmEditCustomer" + id).removeClass("disableSelect");
        $("#cancelEditCustomer" + id).removeClass("disableSelect");
        $("#txtCustomers" + id).addClass("disableSelect");
        $("#sbEditCustomer" + id).removeClass("disableSelect");
        $(".btn-warning").attr("disabled", "disabled");
        $(".select-shift").attr("disabled", "disabled");
        $("#txtFilterSetup").attr("disabled", "disabled");

        $.ajax({
            type: "GET",
            url: "/api/test/customer",
            data: {
                factory: dataset.factory
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var listCustomers = '<select class="form-control selectCustomerEdit" style="color: #fff; background: #333;">';
                for (i in data) {
                    listCustomers += '<option value="' + data[i] + '">' + data[i] + '</option>';
                }
                listCustomers += '</select>';
                $("#sbEditCustomer" + id).html(listCustomers);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });

    }

    function cancelEditCus(id) {
        $("#btnEditCustomer" + id).removeClass("disableSelect");
        $("#confirmEditCustomer" + id).addClass("disableSelect");
        $("#cancelEditCustomer" + id).addClass("disableSelect");
        $("#txtCustomers" + id).removeClass("disableSelect");
        $("#sbEditCustomer" + id).addClass("disableSelect");
        $(".btn-warning").removeAttr("disabled");
        $(".select-shift").removeAttr("disabled");
        $("#txtFilterSetup").removeAttr("disabled");
    }

    function confirmEditCus(id) {
        var confirm = window.confirm("Are you sure CHANGE?");
        if (confirm == true) {
            var editCustomer = $('select.selectCustomerEdit option:selected').val();
            var data = {
                factory: dataset.factory,
                modelName: $("#txtSetupModelName" + id).html(),
                customer: editCustomer,
                stage: $("#txtSetupStage" + id).html(),
                subStage: $("#txtSetupSubStage" + id).html()
            }

            $.ajax({
                type: 'PUT',
                url: '/api/test/'+dataset.factory+'/modelMeta/' + id,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    setupCustomers();
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }

    var listgroupSetup = [];
    var listSubStageSetup = [];

    function setupGroup() {
        $("#btnSetCus").removeClass("selected");
        $("#btnSetGroup").addClass("selected");
        $.ajax({
            type: "GET",
            url: "/api/test/"+dataset.factory+"/groupMeta",
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var html = '';
                var stt = 0;
                if (data != null || data != undefined) {
                    var thead = '<tr>' +
                        '<th style="width: 6%; text-align: center; background: #444;">STT</th>' +
                        '<th style="width: 15%; text-align: center; background: #444;">Customer</th>' +
                        '<th style="width: 20%; text-align: center; background: #444;">Stage</th>' +
                        '<th style="width: 20%; text-align: center; background: #444;">Sub-Stage</th>' +
                        '<th style="width: 20%; text-align: center; background: #444;">Group Name</th>' +
                        '<th style="width: 10%; text-align: center; background: #444;">Step</th>' +
                        '<th style="width: 10%; text-align: center; background: #444;">Remark</th>' +
                        '<th style="width: 12%; text-align: center; background: #444; z-index:10">Action</th>' +
                        '</tr>';
                    $("#tblModelMeta thead").html(thead);
                    $("#tblModelMeta tbody").html("");
                    for (i in data) {
                        stt++;
                        if (listgroupSetup.indexOf(data[i].groupName) == -1) {
                            listgroupSetup.push(data[i].groupName);
                        }
                        if (listSubStageSetup.indexOf(data[i].subStage) == -1) {
                            listSubStageSetup.push(data[i].subStage);
                        }

                        if (data[i].remark == null) {
                            data[i].remark = "NONE";
                        }
                        if (data[i].remark == 0) {
                            data[i].remark = "IN/OUT";
                        }
                        if (data[i].remark == 1) {
                            data[i].remark = "IN";
                        }
                        if (data[i].remark == 2) {
                            data[i].remark = "OUT";
                        }

                        html = '<tr id="row' + data[i].id + '">' +
                            '<td>' + stt + '</td>' +
                            '<td><span id="txtSetupCustomer' + data[i].id + '">' + data[i].customer + '</span></td>' +
                            '<td><span id="txtSetupStage' + data[i].id + '">' + data[i].stage + '</span></td>' +
                            '<td><div id="sbEditSubStage' + data[i].id + '"></div><span id="txtSubStages' + data[i].id + '">' + data[i].subStage + '</span></td>' +
                            '<td><div id="sbEditGroup' + data[i].id + '"></div><span id="txtGroups' + data[i].id + '">' + data[i].groupName + '</span></td>' +
                            '<td><span id="txtSetupStep' + data[i].id + '">' + data[i].step + '</span><input type="number" id="txtEditStep' + data[i].id + '" class="form-control disableSelect" value="' + data[i].step + '" style="color: #fff; background: #333;" /></td>' +
                            '<td><span id="txtRemark' + data[i].id + '">' + data[i].remark + '</span><div id="sbEditRemark' + data[i].id + '"></div></td>' +
                            '<td>' +
                            '<button class="btn btn-warning" id="btnEditGroup' + data[i].id + '" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="editGroup(' + data[i].id + ')">' +
                            '<i class="icon icon-pencil"></i>' +
                            '</button>' +
                            '<button class="btn btn-success disableSelect" id="confirmEditGroup' + data[i].id + '" title="Confirm" style="padding: 4px 10px;" onclick="confirmEditGroup(' + data[i].id + ')">' +
                            '<i class="icon icon-checkmark"></i>' +
                            '</button><button class="btn btn-danger disableSelect" id="cancelEditGroup' + data[i].id + '" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEditGroup(' + data[i].id + ')">' +
                            '<i class="icon icon-spinner11"></i>' +
                            '</button>' +
                            '</td>' +
                            '</tr>';
                        $("#tblModelMeta tbody").append(html);
                    }
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function editGroup(id) {
        $("#btnEditGroup" + id).addClass("disableSelect");
        $("#confirmEditGroup" + id).removeClass("disableSelect");
        $("#cancelEditGroup" + id).removeClass("disableSelect");
        $("#txtGroups" + id).addClass("disableSelect");
        $("#txtSubStages" + id).addClass("disableSelect");
        $("#txtRemark" + id).addClass("disableSelect");
        $("#txtSetupStep" + id).addClass("disableSelect");
        $("#sbEditGroup" + id).removeClass("disableSelect");
        $("#sbEditSubStage" + id).removeClass("disableSelect");
        $("#sbEditRemark" + id).removeClass("disableSelect");
        $("#txtEditStep" + id).removeClass("disableSelect");
        $(".btn-warning").attr("disabled", "disabled");
        $(".select-shift").attr("disabled", "disabled");
        $("#txtFilterSetup").attr("disabled", "disabled");

        var listGroups = '<select class="form-control selectGroupEdit" style="color: #fff; background: #333;">';
        for (i in listgroupSetup) {
            listGroups += '<option value="' + listgroupSetup[i] + '">' + listgroupSetup[i] + '</option>';
        }
        listGroups += '</select>';
        $("#sbEditGroup" + id).html(listGroups);

        var listSubStages = '<select class="form-control selectSubStageEdit" style="color: #fff; background: #333;">';
        for (i in listSubStageSetup) {
            listSubStages += '<option value="' + listSubStageSetup[i] + '">' + listSubStageSetup[i] + '</option>';
        }
        listSubStages += '</select>';
        $("#sbEditSubStage" + id).html(listSubStages);

        var sbRemark = '<select class="form-control selectRemarkEdit" style="color: #fff; background: #333;">' +
            '<option value="">NONE</option>' +
            '<option value="0">IN/OUT</option>' +
            '<option value="1">IN</option>' +
            '<option value="2">OUT</option></select>';
        $("#sbEditRemark" + id).html(sbRemark);
    }

    function cancelEditGroup(id) {
        $("#btnEditGroup" + id).removeClass("disableSelect");
        $("#confirmEditGroup" + id).addClass("disableSelect");
        $("#cancelEditGroup" + id).addClass("disableSelect");
        $("#txtGroups" + id).removeClass("disableSelect");
        $("#txtSubStages" + id).removeClass("disableSelect");
        $("#txtRemark" + id).removeClass("disableSelect");
        $("#txtSetupStep" + id).removeClass("disableSelect");
        $("#sbEditGroup" + id).addClass("disableSelect");
        $("#sbEditSubStage" + id).addClass("disableSelect");
        $("#sbEditRemark" + id).addClass("disableSelect");
        $("#txtEditStep" + id).addClass("disableSelect");
        $(".btn-warning").removeAttr("disabled");
        $(".select-shift").removeAttr("disabled");
        $("#txtFilterSetup").removeAttr("disabled");
    }

    function confirmEditGroup(id) {
        var confirm = window.confirm("Are you sure CHANGE?");
        if (confirm == true) {
            var editCustomer = $("#txtSetupCustomer" + id).html();
            var editStage = $("#txtSetupStage" + id).html();
            var editSubStage = $("select.selectSubStageEdit option:selected").val();
            var editGroup = $("select.selectGroupEdit option:selected").val();
            var editStep = Number($("#txtEditStep" + id).val());
            var editRemark = $("select.selectRemarkEdit option:selected").val();
            var data = {
                factory: dataset.factory,
                customer: editCustomer,
                stage: editStage,
                subStage: editSubStage,
                groupName: editGroup,
                step: editStep,
                remark: editRemark
            }

            $.ajax({
                type: 'PUT',
                url: '/api/test/'+dataset.factory+'/groupMeta/' + id,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    setupGroup();
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }

    //Filter Model Setup
    $("#txtFilterSetup").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblModelMeta tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(document).ready(function() {
        $('input[name=timeSpan]').on('change', function() {
            dataset.workDate = this.value;
            $(".loader").removeClass("disableSelect");
            dataset.lineName = "";
            dataset.mo = "";
            window.localStorage.setItem('dataset', JSON.stringify(dataset));
            $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
            $('.dropdown-select').find('.dropdown-toggle').html('Line <span class="caret"></span>');

            if(dataset.factory == "nbb" || dataset.factory == "s03"){
                dataset.modelName = "";
                $('.dropdown-select-model').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');
                loadSelectModel();
                loadData();
            } else{
                loadSelectModel();
            }

            closeModal();

            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
        });
    });
</script>
    <script type="text/javascript" src="/assets/custom/js/nbb/dragModal.js"></script>