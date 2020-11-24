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

<script>
    function setupCustomers() {
        $("#btnSetCus").addClass("selected");
        $("#btnSetGroup").removeClass("selected");
        //   $(".modal-dialog").removeClass("modal-xl");
        $.ajax({
            type: "GET",
            url: "/api/test/"+dataset.factory+"/modelMeta",
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
            url: "/api/test/"+dataset.factory+"/customer",
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
</script>