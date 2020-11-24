<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<style>
    .header>p{
        padding: 5px 0px;
        text-align: center;
        font-size: 20px;
        font-weight: bold;
        background: #DDDDDD;
        color: #272727;
        border-bottom: 1px solid #3E3E3E;
        text-transform: uppercase;
    }
    .row-body .btn-border{
        margin: 0px 0px 5px;
        border: 1px solid #272727;
        background: #DDDDDD;
        color: #272727;
    }
    #tblWorkCount thead th{
        font-weight: bold;
        border: 1px solid #272727;
        text-align: center;
        padding: 5px 8px;
        background-color: #DDDDDD;
        position: -webkit-sticky;
        position : sticky;
        top : -1px;
    }
    #tblWorkCount tbody td{
        border: 1px solid #272727;
        text-align: center;
        padding: 5px 8px;
    }
    #tblWorkCount .work-day{
        font-weight: bold;
        color: #28A745;
        cursor: pointer;
    }
    #tblWorkCount .off-day{
        font-weight: bold;
        color: #F44337;
        /* cursor: pointer; */
    }
    #tblWorkCount{
        table-layout: fixed;
        max-width: 15000px;
        margin: 0px;
    }

    /* here is the trick */
    .table-sticky tbody:nth-of-type(1) tr:nth-of-type(1) td {
        border-top: none !important;
    }
    .table-sticky thead th {
        border-top: none !important;
        border-bottom: none !important;
        box-shadow: inset 0 2px 0 #272727,
                    inset 0 -1px 0 #272727;
        z-index: 1;
    }
    #tblWorkCount tr th:nth-of-type(1){
        left: 0px;
        top: -1px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 1px 2px 0 #272727, inset 0px -1px 0 #272727
    }
    #tblWorkCount tr th:nth-of-type(2){
        left: 100px;
        top: -1px;
        z-index: 10;
        position: sticky;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 2px 0 #272727, inset 0px -1px 0 #272727;
    }
    #tblWorkCount tr th:nth-of-type(3){
        left: 190px;
        top: -1px;
        z-index: 10;
        position: sticky;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 2px 0 #272727, inset 0px -1px 0 #272727;
    }
    #tblWorkCount tr th:nth-of-type(4){
        left: 240px;
        top: -1px;
        z-index: 10;
        position: sticky;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 2px 0 #272727,
                    inset -1px -1px 0 #272727;
    }
    #tblWorkCount tr th:nth-of-type(5), #tblWorkCount tr td:nth-of-type(5){
        border-left: none !important;
    }
    #tblWorkCount tr td:nth-of-type(1){
        left: 0px;
        z-index: 1;
        font-weight: 600;
        background-color: #EEEEEE;
        position: sticky;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #272727, inset 0px 0 0 #272727
    }
    #tblWorkCount tr td:nth-of-type(2){
        left: 100px;
        z-index: 1;
        font-weight: 400;
        background-color: #EEEEEE;
        position: sticky;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #272727, inset 0px 0 0 #272727;
    }
    #tblWorkCount tr td:nth-of-type(3){
        left: 190px;
        z-index: 1;
        background-color: #EEEEEE;
        position: sticky;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #272727, inset 0px 0 0 #272727;
    }
    #tblWorkCount tr td:nth-of-type(4){
        left: 240px;
        z-index: 1;
        background-color: #EEEEEE;
        position: sticky;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #272727,
                    inset -1px 0 0 #272727;
    }
    .row-body textarea{
        height: calc(100vh - 245px);
        resize: none;
        text-align: center;
        font-weight: 600;
    }
    .popover{
        margin-top:85px;
        background-color: #FFFFFF;
        border:1px solid #272727;
        color:#272727;
        box-shadow: 0.5rem 0.5rem 2rem #272727;
    }
    .popover-content {
        padding: 10px;
    }
    .loader {
        display: block;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) url('/assets/images/loadingg.gif') 50% 50% no-repeat;
    }
</style>

<div class="loader hidden"></div>
<div class="panel panel-re panel-flat row" style="margin-bottom: 0px; min-height: calc(100vh - 100px);">
    <div class="col-lg-12 no-padding">
        <div class="header">
            <p>WORK COUNT</p>
        </div>
    </div>
    <div class="row no-margin">
        <div class="col-sm-12">
            <div class="input-group" style="background-color: #DDDDDD; color: #272727; height: 26px; margin: -5px 0px 5px 0px; border: 1px solid #272727">
                <span class="input-group-addon" style="padding: 0px 5px;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px;">
            </div>
        </div>
    </div>
    <div class="row row-body no-margin" style="max-height: calc(100vh - 200px);">
        <div class="col-sm-1 col-xs-2" style="text-align: center; width: 10%;">
            <div class="input-group btn-border">
                <!-- <span class="input-group-addon" style="padding: 0px 5px;"><i class="fa fa-users"></i></span> -->
                <textarea type="text" class="form-control" autofocus="true" name="listId" spellcheck="false"></textarea>
            </div>
            <button class="btn btn-border" style="padding: 5px;" onclick="fillterClick()"> <i class="fa fa-search"></i> Fillter</button>
        </div>
        <div class="col-sm-11 col-xs-10 table-responsive no-padding" style="width: 90%; max-height: calc(100vh - 200px);">
            <table id="tblWorkCount" class="table table-sticky pre-scrollable">
                <thead></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>

<!-- <button onclick="topFunction()" id="myBtn" title="Về đầu trang">Về đầu trang</button> -->

<script>
    var dataset = {};
    function fillterClick(){
        dataset.employeesNo = $('textarea[name=listId]').val().replace(/\n/g," ");
        dataset.timeSpan = $('input[name=timeSpan]').val();
        loadData();
    }
    function loadData() {
        $('.loader').removeClass('hidden');
        $.ajax({
            type: 'GET',
            url: '/api/hr/employee/foreign/clock/h%C3%ADtory',
            data: {
                empNoStr: dataset.employeesNo,
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                $('#tblWorkCount>thead').html('');
                $('#tblWorkCount>tbody').html('');
                if(!$.isEmptyObject(result)){
                    var data = result.data;
                    console.log(data);
                    var thead = '<tr><th style="width: 100px;">Employee ID </br> 工號</th><th style="width: 90px;">Name </br>姓名</th><th style="width: 50px;">Work Day</th><th  style="width: 50px;">Off Day</th>';
                    for(i in data){
                        for(j in data[i].shortDutyResult){
                            let time = j.slice(5,10);
                            thead += '<th style="width: 60px;">' + time + '</th>';
                        }
                        thead += '</tr>';
                        break;
                    }
                    $('#tblWorkCount>thead').html(thead);
                    var tbody = '';
                    for(i in data){
                        tbody += '<tr><td>' + i + '</td><td>' + data[i].name + '</td><td class="work-day">' + data[i].totalWorkDay + '</td><td class="off-day">' + data[i].totalOffDay + '</td>';
                        let dataDutyResult = data[i].dutyResult;
                        let dataShortDutyResult = data[i].shortDutyResult;
                        for(j in dataShortDutyResult){
                            if(dataShortDutyResult[j]){
                                var resource = '<div style=\'margin-bottom: 5px; text-align: center;\'><b>' + convertNull(dataDutyResult[j].F_DUTYDATE) + '</b></div>' +
                                '<div style=\'margin-bottom: 5px; text-align: center;\'><b>' + convertNull(dataDutyResult[j].F_CLASSNO) + '</b></div>' +

                                '<div style=\'margin-top: 5px;\'><b>Begin Work: </b><span>' + convertNull(dataDutyResult[j].F_BEGINWORK) + '</span></div>' +
                                '<div style=\'margin-bottom: 5px;\'><b>Begin Rest: </b><span>' + convertNull(dataDutyResult[j].F_BEGINREST) + '</span></div>' +

                                '<div style=\'margin-top: 5px;\'><b>End Rest: </b><span>' + convertNull(dataDutyResult[j].F_ENDREST) + '</span></div>' +
                                '<div style=\'margin-bottom: 5px;\'><b>End Work: </b><span>' + convertNull(dataDutyResult[j].F_ENDWORK) + '</span></div>' +

                                '<div style=\'margin-top: 5px;\'><b>Overtime From: </b><span>' + convertNull(dataDutyResult[j].F_OVERTIMEFROM) + '</span></div>' +
                                '<div style=\'margin-bottom: 5px;\'><b>Overtime To: </b><span>' + convertNull(dataDutyResult[j].F_OVERTIMETO) + '</span></div>' +
                                '<div style=\'margin-top: 5px; text-align: center\'><span>' + convertNull(dataDutyResult[j].F_MODIFYRESULT) + ' (Edited: ' + convertNull(dataDutyResult[j].F_ISMODIFYRESULT) + ')</span></div>';
                                tbody += '<td class="work-day" data-popup="popover" data-trigger="hover" data-placement="left" data-html="true" data-content="' + resource + '"><i class="fa fa-check"></i></td>';
                            } else{
                                // tbody += '<td class="off-day">×</td>';
                                tbody += '<td class="off-day"></td>';
                            }
                        }
                        tbody += '</tr>';
                    }
                    $('#tblWorkCount>tbody').html(tbody);
                }
            },
            error: function () {
                alert("Fail!");
            },
            complete: function () {
                $('[data-popup=popover]').popover({
                    template: '<div class="popover"><div class="arrow"></div><div class="popover-content"></div></div>'
                    , container: 'body'
                });
                $('.loader').addClass('hidden');
            }
        });
    }

    function convertNull(str){
        var value = '';
        if(str == null || str == "null"){
            var value = '';
        } else{
            value = str;
        }
        return value;
    }

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/01") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(current);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    $(document).ready(function () {
        getTimeNow();
    });
</script>
