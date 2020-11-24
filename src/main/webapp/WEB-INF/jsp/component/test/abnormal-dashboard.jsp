<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!-- <link rel="stylesheet" href="/assets/css/custom/station-dashboard.css" /> -->
<style>
    #tblDetail{
        background-color: #fff;
        font-size: 21px;
        font-weight: 400;
    }
    #tblDetail thead th{
        background-color: #545b62;
        color: #ffffff;
        text-align: center;
        text-transform: uppercase;
        font-weight: bold;
    }
    .rowR td{
        text-align: center;
        color: #ffffff;
        background-color: #D9001B;
    }
    .rowY td{
        text-align: center;
        color: #272727;
        background-color: #F59A23;
    }
    .bootstrap-select button{
        height: 26px;
        padding: 4px !important;
        color: #FFFFFF;
    }
    .loader{
        display: block;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) 
        url('/assets/images/loadingg.gif') 
        50% 50% 
        no-repeat;
    }
</style>
<div class="loader" class="hidden"></div>
<div class="row">
    <div class="panel panel-overview" style="margin:unset; background-color:#272727; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <span id="titleCustomer" style="font-size:16px;"></span><span style="font-size:16px;"> ABNORMAL DASHBOARD</span>
    </div>
    <div class="col-xs-6 col-sm-3">
        <div class="input-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
    </div>
    <div class="col-xs-3 col-sm-1">
        <div class="form-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;" >
            <select class="form-control bootstrap-select" name="selectCustomer">
                <option value="Z1">Z1</option>
                <option value="ELAINE">ELAINE</option>
                <option value="NQ">NQ</option>
            </select>
        </div>
    </div>
    <div class="col-xs-3 col-sm-1">
        <div class="form-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;" >
            <select class="form-control bootstrap-select" name="selectStage"></select>
        </div>
    </div>
    <div class="col-xs-12" id="old-style" style="margin: 10px 0px;">
        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
            <table class="table table-xxs table-bordered table-sticky" id="tblDetail">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Line</th>
                        <th>Group</th>
                        <th>Reason</th>
                        <th>Time</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- <tr class="rowR">
                        <td>1</td>
                        <td>SMT-RF-COND</td>
                        <td>10 Failure Devices Consecutive</td>
                        <td>2020-03-16 16:18:00</td>
                    </tr>
                    <tr class="rowR">
                        <td>2</td>
                        <td>SMT-HVAC</td>
                        <td>20 Failure Devices Same Error (ERR001)</td>
                        <td>2020-03-16 17:18:00</td>
                    </tr>
                    <tr class="rowR">
                        <td>3</td>
                        <td>SMT-HVAC</td>
                        <td>20 Failure Devices Same Error (ERR003)</td>
                        <td>2020-03-16 17:18:00</td>
                    </tr>
                    <tr class="rowY">
                        <td>4</td>
                        <td>SMT-HVAC</td>
                        <td>3 Failure Devices Consecutive</td>
                        <td>2020-03-16 18:18:00</td>
                    </tr>
                    <tr class="rowY">
                        <td>5</td>
                        <td>SMT-HVAC</td>
                        <td>5 Failure Devices Same Error (ERR002)</td>
                        <td>2020-03-16 19:18:00</td>
                    </tr> -->
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    var dataset = {
        factory: '${factory}',
        customer: 'Z1'
    };

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
                var selector = $("select[name='selectStage']");
                selector.children('option').remove();
                var options = "<option value=''>Select All</option>";
                for(i in data){
                    if(data[i] == dataset.stage){
                        options+='<option value=' + data[i] + ' selected>' + data[i] + '</option>';
                    }
                    else
                        options+='<option value=' + data[i] + '>' + data[i] + '</option>';
                }
                selector.append(options);
                selector.selectpicker('refresh');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadData(){
        clearTimeout(dataset.timeout);
        $('.loader').removeClass('hidden');
        $.ajax({
            type: "GET",
            url: "/api/test/tracking/list",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $('#tblDetail>tbody').html('');
                if(!$.isEmptyObject(data)){
                    var html = '';
                    var stt = 1;
                    for(i in data){
                        if(data[i].type == "LOCKED_C" || data[i].type == "LOCKED_D"){
                            html += '<tr class="rowR"><td>'+stt+'</td>';
                        }
                        else if(data[i].type == "LOCKED_A" || data[i].type == "LOCKED_B"){
                            html += '<tr class="rowY"><td>'+stt+'</td>';
                        } else html += '<tr><td>'+stt+'</td>';
                        html += '<td>'+data[i].lineName+'</td>'
                             +  '<td>'+data[i].groupName+'</td>'
                             +  '<td>'+data[i].message+'</td>'
                             +  '<td>'+data[i].createdAt+'</td></tr>';
                        stt++;
                    }
                    $('#tblDetail>tbody').html(html);
                }
                $('.loader').addClass('hidden');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                dataset.timeout = setTimeout(loadData, 60000);
            }
        });
    }

    $('select[name=selectCustomer]').on('change', function() {
        dataset.customer = this.value;
        $('#titleCustomer').html(dataset.modelName);
        dataset.stage = '';
        loadSelectStage();
        loadData();
    });

    $('select[name=selectStage]').on('change', function() {
        dataset.stage = this.value;
        loadData();
    });

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                $('input[name=timeSpan]').data('daterangepicker').setStartDate(new Date(startDate));
                $('input[name=timeSpan]').data('daterangepicker').setEndDate(new Date(endDate));
                $('input[name=timeSpan]').on('change', function () {
                    dataset.timeSpan = this.value;
                    loadData();
                });

                dataset.timeSpan = startDate + ' - ' + endDate;
                $('#titleCustomer').html(dataset.modelName);
                loadData();
                loadSelectStage();

                delete current;
                delete startDate;
                delete endDate;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    getTimeNow();
</script>