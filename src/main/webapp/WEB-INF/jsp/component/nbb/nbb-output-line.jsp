<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />

<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span id="titlePage"></span><span class="text-uppercase"> ${factory}</span><span> Output By Line </span><span id="txtDateTime"></span></b>
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
    <div class="row" style="color: #333333; margin: unset; padding: 5px;">
        <button class="btn btn-lg" style="float: left; padding: 5px 10px; color: #ccc; background: #272727; border: 1px solid #fff;" onclick="addNewClick()">
        <i class="fa fa-plus"></i> Add
    </button>
    </div>
    <div class="table-responsive pre-scrollable" style="color: #333333; max-height: 500px;">
        <table id="tblTarget" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px;">
            <thead>
                <tr>
                    <th style="width: 10%; text-align: center">STT</th>
                    <th style="width: 30%; text-align: center">Model Name</th>
                    <th style="width: 20%; text-align: center">Group Name</th>
                    <th style="width: 20%; text-align: center">UPH</th>
                    <th style="width: 20%; text-align: center">Action</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
    <div id="mydivfooter" style="text-align: right;">
    </div>
</div>

<script type="text/javascript" src="/assets/custom/js/nbb/loadSelection.js"></script>
<script type="text/javascript" src="/assets/custom/js/nbb/getTimeNow.js"></script>
<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if (dataset == null || dataset == undefined || dataset.path != "${path}") {
        dataset = {
            shiftType: "",
            customer: "Vento",
            stage: "FAT",
            modelName: "",
            search: "",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    } else {
        dataset.search = "";
    }

    var dataModelNames = [];
    var dataGroupNames = [];

    function init() {
        // loadDataByModel();
        loadSelectCustomer();
        loadSelectStage();
        loadSelectMO();
        loadSelectLine();
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
        } else $('.dropdown-select-stage').find('.dropdown-toggle').html('Stage Name <span class="caret"></span>');
        if (dataset.modelName != "") {
            $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');
        } else $('.dropdown-select-model').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');

    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        dataset.customer = $(this).html();

        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        dataset.lineName = "";
        dataset.modelName = "";
        dataset.stage = "";
        dataset.mo = "";
        $("#titlePage").html(dataset.customer);
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
        closeModal();
        $('.selectStage').removeClass("disableSelect");
        $('.selectModel').addClass("disableSelect");
        $('.dropdown-select-stage').find('.dropdown-toggle').html('Stage Name <span class="caret"></span>');
        loadSelectStage();
        $('.dropdown-select').find('.dropdown-toggle').html('Line Name <span class="caret"></span>');
        loadSelectLine();
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
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
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
        closeModal();

        $('.selectModel').removeClass("disableSelect");
        $('.dropdown-select-model').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');
        loadSelectModel();
        dataset.lineName = "";
        dataset.mo = "";
        $('.dropdown-select').find('.dropdown-toggle').html('Line Name <span class="caret"></span>');
        loadSelectLine();
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
        loadSelectMO();
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
        loadSelectMO();
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
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

        dataset.lineName = "";
        dataset.mo = "";
        $('.dropdown-select').find('.dropdown-toggle').html('Line Name <span class="caret"></span>');
        loadSelectLine();
        $('.dropdown-select-mo').find('.dropdown-toggle').html('MO Number <span class="caret"></span>');
        loadSelectMO();
        loadDataByModel();
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
        loadSelectLine();
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
        closeModal();
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
        loadDataByModel();
        closeModal();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        var getD = moment(dataset.workDate).format("MM/DD");
        $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
    }

    function loadDataByModel() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/group/outputOfDate",
            data: {
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                mo: dataset.mo,
                lineName: dataset.lineName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#tblDetail").html("");
                var header = Object.keys(data);
                dataGroupNames = header;
                var thead = '<thead><tr><th width="8%">Line</th>';
                for (i in header) {
                    if (header[i] != "HEADER")
                        thead += '<th>' + header[i] + '</th>';
                }
                thead += '</tr></thead><tbody>';
                if (dataset.shiftType == "DAY") {
                    var dataTime = Object.keys(data["HEADER"]);
                    thead += '<tr class="target"><td>UPH Target</td></tr>';
                    thead += '<tr class="accumulate"><td>Accumulated</td></tr>';
                    for (i in dataTime) {
                        thead += '<tr class="time' + i + '">' +
                            '<td><a data-href="/nbb-uph-by-line?customer=' + dataset.customer + '&lineName=' + dataTime[i] + '&shiftType=' + dataset.shiftType + '&workDate=' + dataset.workDate + '" onclick="openWindow(this.dataset.href);">' + dataTime[i] + '</a></td></tr>';
                    }
                    thead += '</tbody>';
                    $("#tblDetail").append(thead);

                    for (i in data) {
                        if (i != "HEADER") {
                            for (j in dataTime) {
                                if (data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined) {
                                    $(".time" + j).append("<td class='noData'>N/A</td>");
                                } else {
                                    $(".time" + j).append("<td><b>" + data[i][dataTime[j]] + "</b></td>");
                                }
                            }
                            if (data[i]["uph-target"] == 0 || data[i]["uph-target"] == undefined) data[i]["uph-target"] = "N/A";
                            if (data[i]["accumulate"] == 0 || data[i]["accumulate"] == undefined) data[i]["accumulate"] = "N/A";
                            $(".target").append("<td class='noData'>" + data[i]["uph-target"] + "</td>");
                            $(".accumulate").append("<td><b>" + data[i]["accumulate"] + "</b></td>");
                        }
                    }

                } else if (dataset.shiftType == "") {
                    var dataTime = Object.keys(data["HEADER"]);
                    thead += '<tr class="target"><td>UPH Target</td></tr>';
                    thead += '<tr class="accumulate"><td>Accumulated</td></tr>';
                    for (i in dataTime) {
                        thead += '<tr class="time' + i + '">' +
                            '<td><a data-href="/nbb-uph-by-line?customer=' + dataset.customer + '&lineName=' + dataTime[i] + '&shiftType=' + dataset.shiftType + '&workDate=' + dataset.workDate + '" onclick="openWindow(this.dataset.href);">' + dataTime[i] + '</a></td></tr>';
                    }
                    $("#tblDetail").append(thead);

                    for (i in data) {
                        if (i != "HEADER") {
                            for (j in dataTime) {
                                if (data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined) {
                                    $(".time" + j).append("<td class='noData'>N/A</td>");
                                } else {
                                    $(".time" + j).append("<td><b>" + data[i][dataTime[j]] + "</b></td>");
                                }
                            }
                            if (data[i]["uph-target"] == 0 || data[i]["uph-target"] == undefined) data[i]["uph-target"] = "N/A";
                            if (data[i]["accumulate"] == 0 || data[i]["accumulate"] == undefined) data[i]["accumulate"] = "N/A";
                            $(".target").append("<td class='noData'>" + data[i]["uph-target"] + "</td>");
                            $(".accumulate").append("<td><b>" + data[i]["accumulate"] + "</b></td>");
                        }
                    }
                } else {
                    var dataTime = Object.keys(data["HEADER"]);
                    thead += '<tr class="target"><td>UPH Target</td></tr>';
                    thead += '<tr class="accumulate"><td>Accumulated</td></tr>';
                    for (i in dataTime) {
                        thead += '<tr class="time' + i + '">' +
                            '<td><a data-href="/nbb-uph-by-line?customer=' + dataset.customer + '&lineName=' + dataTime[i] + '&shiftType=' + dataset.shiftType + '&workDate=' + dataset.workDate + '" onclick="openWindow(this.dataset.href);">' + dataTime[i] + '</a></td></tr>';
                    }
                    $("#tblDetail").append(thead);

                    for (i in data) {
                        if (i != "HEADER") {
                            for (j in dataTime) {
                                if (data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined) {
                                    $(".time" + j).append("<td class='noData'>N/A</td>");
                                } else {
                                    $(".time" + j).append("<td><b>" + data[i][dataTime[j]] + "</b></td>");
                                }
                            }
                            if (data[i]["uph-target"] == 0 || data[i]["uph-target"] == undefined) data[i]["uph-target"] = "N/A";
                            if (data[i]["accumulate"] == 0 || data[i]["accumulate"] == undefined) data[i]["accumulate"] = "N/A";
                            $(".target").append("<td class='noData'>" + data[i]["uph-target"] + "</td>");
                            $(".accumulate").append("<td><b>" + data[i]["accumulate"] + "</b></td>");
                        }
                    }
                }
                $(".loader").addClass("disableSelect");
                $("#tblDetail").append("<div id='phantomjsMark'></div>");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                dataset.timeout = setTimeout(loadDataByModel, 30000, dataset);
            }
        });
    }

    function loadDataModelNames() {
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/model",
            data: {
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
            url: "/api/test/" + dataset.factory + "/uph",
            data: {
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
                        if ((data[i].modelName).indexOf(dataset.search) != -1 || (data[i].groupName).indexOf(dataset.search) != -1) {
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
                        } else html = '';
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
    }
    $('input[type=search]').on('input', function() {
        clearTimeout(this.delay);
        this.delay = setTimeout(function() {
            $(this).trigger('search');
        }.bind(this), 600);
    }).on('search', function() {
        dataset.search = ($("#txtSearch").val()).toString().toUpperCase();
        $("#btnAddNew").removeAttr("disabled");
        showModal();
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
            url: '/api/test/' + dataset.factory + '/uph',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                showModal();
                loadDataByModel();
                $("#btnAddNew").removeAttr("disabled");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function cancelAddNew() {
        $("#newRow").remove();
        $("#btnAddNew").removeAttr("disabled");
    }

    function edit(stt) {
        $("#deleteRow" + stt).addClass("hidden");
        $("#editRow" + stt).addClass("hidden");
        $("#confirmRow" + stt).removeClass("hidden");
        $("#cancelRow" + stt).removeClass("hidden");

        $("#uph" + stt).addClass("hidden");
        $("#txtEditUPH" + stt).removeClass("hidden");
        $("#btnAddNew").attr("disabled", "disabled");
    }

    function cancelEdit(stt) {
        $("#deleteRow" + stt).removeClass("hidden");
        $("#editRow" + stt).removeClass("hidden");
        $("#confirmRow" + stt).addClass("hidden");
        $("#cancelRow" + stt).addClass("hidden");

        $("#uph" + stt).removeClass("hidden");
        $("#txtEditUPH" + stt).addClass("hidden");
        $("#btnAddNew").removeAttr("disabled");
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
                url: '/api/test/' + dataset.factory + '/uph/' + stt,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    showModal();
                    loadDataByModel();
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
                url: '/api/test/' + dataset.factory + '/uph/' + stt,
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    showModal();
                    loadDataByModel();
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
                sa = txtArea1.document.execCommand("SaveAs", true, "${factory} Output By Line.xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblDetail', 'uph_table');
            $(this).attr('download', '${factory} Output By Line (' + dataset.shiftType + ').xls')
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
            $('.dropdown-select-model').find('.dropdown-toggle').html('Select ModelName <span class="caret"></span>');
            loadSelectModel();
            $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
            loadSelectMO();

            loadDataByModel();
            closeModal();

            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
        });
    });
</script>

<script type="text/javascript" src="/assets/custom/js/nbb/dragModal.js"></script>