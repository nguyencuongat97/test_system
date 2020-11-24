<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<link rel="stylesheet" href="/assets/css/custom/style.css" />



<style>
    .table-hover > tbody > tr:hover {
        background-color: #272727 !important;
    }
    .filter{
        padding: 5px 10px; 
        border-radius: 10px; 
        color: #ccc; 
        background: #272727; 
        border: 1px solid #fff; 
        height: 35px; 
        width: 30%;
        float: left;
        margin-left: 10px;
    }
    .table-custom > tr > td {
        padding: 5px 10px;
    }

    #mydiv {
        display: none;
        position: absolute;
        z-index: 9;
        background-color: #f1f1f1;
        text-align: center;
        border: 1px solid #d3d3d3;
        width: 500px;
        left: 400px;
        top: 150px;
    }

    #mydiv.show{
        display: block;
    }

    #mydivheader {
        padding: 2px;
        cursor: move;
        z-index: 10;
        background-color: #272727;
        color: #fff;
    }

    #tblDetail{
        font-size: 18px !important;
        color: #333333;
    }

    .table-sticky thead th {
        position: -webkit-sticky;
        position : sticky;
        top : -1px;
        background: #e6e6e6;
        color: #333333;
    }
    /* here is the trick */
    .table-sticky tbody:nth-of-type(1) tr:nth-of-type(1) td {
        border-bottom: none !important;
    }
    .table-sticky thead th {
        border-top: none !important;
        border-bottom: none !important;
        box-shadow: inset 0 1px 0 #fff,
                    inset 0 -1px 0 #fff;
    }
</style>
<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <!-- <div class="row" style="margin-bottom: 15px; margin-top: 10px;">
            <div class="input-group col-sm-3" style="float: left; padding: 8px 16px;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #ccc;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: #ccc;">
            </div>
        </div> -->
        <div id="content-data" class="row" style="background: #272727; font-size: 28px; position: relative;">
            <div class="col-sm-12" style="padding-top: 10px; position: sticky; top: 50px; z-index: 9; background: #272727">
                <input id="filterByMachine" class="form-control filter" type="text" placeholder="Machine" onchange="filter()" />
                <input id="filterByDeviceName" class="form-control filter" type="text" placeholder="Slot/Nozzle" onchange="filter()" />
                <input id="filterByDeviceSubName" class="form-control filter" type="text" placeholder="Feeder/Head" onchange="filter()" />
                <a class="btn" onclick="cancel()" title="Reload" style="float: right;">
                    <i class="fa fa-refresh" aria-hidden="true" style="color: #fff; font-size: 18px;"></i>
                </a>
                <a class="btn" onclick="filter()" title="Search" style="float: right;">
                    <i class="fa fa-search" aria-hidden="true" style="color: #fff; font-size: 18px;"></i>
                </a>
                
            </div>
            <div class="col-sm-6">
                 <h3 style="color: #ddd">Offset List</h3>
                <table class="table table-custom table-bordered table-hover" style="background: #333; color: #ccc; text-align: center;">
                    <tbody id="offset">
                    </tbody>
                </table>
            </div>
            <div class="col-sm-6">
                 <h3 style="color: #ddd">Fail List</h3>
                <table class="table table-custom table-bordered table-hover" style="background: #333; color: #ccc; text-align: center;">
                    <tbody id="fail">
                    </tbody>
                </table>
            </div>

<div id="mydiv" class="">
  <div id="mydivheader">
    <span id="txtDetail">I Love You</span>
    <a title="Close" style="float: right; margin-right: 5px; margin-top: -10px; color: #fff;" onclick="closeModal()" >
        <i class="icon icon-cross"></i>
    </a>
  </div>
  <div class="table-responsive pre-scrollable" style="max-height: 250px; color: #ccc;">
        <table id="tblDetail" class="table table-bordered table-sticky">
            <thead></thead>
            <tbody></tbody>           
        </table>
    </div>
    <div id="mydivfooter" style="text-align: right;">
    </div>
</div>

        </div>
    </div>
</div>

<script>
    var dataset = {}
    loadLine();

    function loadLine(){
        $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/aoi/offset/static/accumulation/result",
            // data: {
            //     factory: dataset.factory
            // },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                
                var offsetList = data.offsetList;
                var failList = data.failList;
                
                $('#offset').html("");
                var offset = '';
                if(offsetList.length != 0)
                {
                    var len = offsetList.length;
                    if(offsetList.length > 5){
                        len = 5
                    }
                    for(var i=0; i<offsetList.length; i++){ 
                        offset += '<tr title="Click to show more detail" onclick="showModal(\'offset\',\'' + offsetList[i].id + '\')"><td>'+offsetList[i].elementIndex+'</td>'
                               + '<td>'+offsetList[i].mounterName+'</td>';
                        if(offsetList[i].deviceType === "slot"){
                            offset += '<td>Slot: <b>'+offsetList[i].deviceName+'</b> <br>'
                                   + 'Feeder: <b>' + offsetList[i].deviceSubName + '</b></td>';
                        }
                        else if(offsetList[i].deviceType === "nozzle"){
                            offset += '<td>Nozzle: <b>'+offsetList[i].deviceName+'</b> <br>'
                                   + 'Head: <b>' + offsetList[i].deviceSubName + '</b></td>';
                        }
                    }
                }
                else offset = '<tr><td>NO DATA!</td></tr>';
                $('#offset').html(offset);

                $('#fail').html("");
                var fail = '';
                if(failList.length != 0){
                    var len = failList.length;
                    if(failList.length > 5){
                        len = 5
                    }
                    for(var i=0; i<failList.length; i++){
                        fail += '<tr title="Click to show more detail" onclick="showModal(\'fail\',\'' + failList[i].id + '\')"><td>'+failList[i].elementIndex+'</td>'
                             + '<td>'+failList[i].mounterName+'</td>';
                        if(failList[i].deviceType === "slot"){
                            fail += '<td>Slot: <b>'+failList[i].deviceName+'</b> <br>'
                                 + 'Feeder: <b>' + failList[i].deviceSubName + '</b></td>';
                        }
                        else if(failList[i].deviceType === "nozzle"){
                            fail += '<td>Nozzle: <b>'+failList[i].deviceName+'</b> <br>'
                                 + 'Head: <b>' + failList[i].deviceSubName + '</b></td>';
                        }
                    }
                }
                else fail = '<tr><td>NO DATA!</td></tr>';
                
                $('#fail').html(fail);
                setInterval(loadLine, 300000);
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    function filter(){
        $.ajax({
            type: 'GET',
            url: 'http://10.224.81.70:8888/aoi-analytics/smt/aoi/offset/static/accumulation/result',
            // data: {
            //     factory: dataset.factory
            // },
            success: function(data){
                var machine = ($("#filterByMachine").val()).toUpperCase();
                var deviceName = ($("#filterByDeviceName").val()).toUpperCase();
                var deviceSubName = ($("#filterByDeviceSubName").val()).toUpperCase();
                
                var offsetList = data.offsetList;
                var failList = data.failList;

                $('#offset').html("");
                var offset = '';
                if(offsetList.length != 0){
                    for(i in offsetList){
                        if(offsetList[i].mounterName.indexOf(machine) != -1 && offsetList[i].deviceName.indexOf(deviceName) != -1 && offsetList[i].deviceSubName.indexOf(deviceSubName) != -1){
                            offset += '<tr title="Click to show more detail" onclick="showModal(\'offset\',\'' + offsetList[i].id + '\')"><td>'+offsetList[i].elementIndex+'</td>'
                                   + '<td>'+offsetList[i].mounterName+'</td>';
                            if(offsetList[i].deviceType === "slot"){
                                offset += '<td>Slot: <b>'+offsetList[i].deviceName+'</b> <br>'
                                       + 'Feeder: <b>' + offsetList[i].deviceSubName + '</b></td>';
                            }
                            else if(offsetList[i].deviceType === "nozzle"){
                                offset += '<td>Nozzle: <b>'+offsetList[i].deviceName+'</b> <br>'
                                       + 'Head: <b>' + offsetList[i].deviceSubName + '</b></td>';
                            }
                        }                    
                    }
                } else offset = '<tr><td>NO DATA!</td></tr>';         
                $('#offset').html(offset);

                $('#fail').html("");
                var fail = '';
                if(failList.length != 0){
                    for(i in failList){
                        if(failList[i].mounterName.indexOf(machine) != -1 && failList[i].deviceName.indexOf(deviceName) != -1 && failList[i].deviceSubName.indexOf(deviceSubName) != -1){
                            fail += '<tr title="Click to show more detail" onclick="showModal(\'fail\',\'' + failList[i].id + '\')"><td>'+failList[i].elementIndex+'</td>'
                                 + '<td>'+failList[i].mounterName+'</td>';
                            if(failList[i].deviceType === "slot"){
                                fail += '<td>Slot: <b>'+failList[i].deviceName+'</b> <br>'
                                     + 'Feeder: <b>' + failList[i].deviceSubName + '</b></td>';
                            }
                            else if(failList[i].deviceType === "nozzle"){
                                fail += '<td>Nozzle: <b>'+failList[i].deviceName+'</b> <br>'
                                     + 'Head: <b>' + failList[i].deviceSubName + '</b></td>';
                            }
                        }
                    }
                } else fail = '<tr><td>NO DATA!</td></tr>';  
                $('#fail').html(fail);

            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function cancel() {
        location.reload();
    }

    function showModal(type, index){
        $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/aoi/offset/static/accumulation/result",
            // data: {
            //     factory: dataset.factory
            // },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var offsetList = data.offsetList;
                var failList = data.failList;
                if(type == "offset"){
                    var txtDetail = '';
                    for(i in offsetList){
                        if(offsetList[i].id == Number(index)){
                            txtDetail = offsetList[i].mounterName + " | " + offsetList[i].deviceName + " | " + offsetList[i].deviceSubName;
                            
                            var thead = "<tr><th>Location Code</th><th>Block</th><th>Rate X</th><th>Rate Y</th></tr>";
                            var tb = '';
                            var tf = '<div class="dropup dropdown-select">'
                                + '<button  style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">Select Repair Reason!<span class="caret"></span></button>'
                                + '<ul style=" left: unset; right:0;" class="dropdown-menu table-responsive pre-scrollable" role="menu" aria-labelledby="dropdownMenu1">';
                            var detailInfoList = offsetList[i].detailInfoList;
                            for(j in detailInfoList){
                                tb += '<tr><td>'+detailInfoList[j].locationCode+'</td><td>'+detailInfoList[j].block+'</td><td>'+(detailInfoList[j].rateX*100).toFixed(2)+'%</td><td>'+(detailInfoList[j].rateY*100).toFixed(2)+'%</td></tr>';              
                            }
                            var dataReason = offsetList[i].repairReasonList;
                            for(k in dataReason){
                                tf += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">'+dataReason[k]+'</a></li>';
                            }
                            tf += '</ul> <button disabled="disabled" style="background: #333; color:#FFF" class="btn btn-default btnConfirm" onclick="updateData()">OK <span class="fa fa-check-circle"></span></button></div>';
                            $("#tblDetail thead").html(thead);
                            $("#tblDetail tbody").html(tb);
                            $("#mydivfooter").html(tf);


                            $('.dropdown-select').on( 'click', '.dropdown-menu li a', function() { 
                                var target = $(this).html();       
                                $(this).parents('.dropdown-menu').find('li').removeClass('active');
                                $(this).parent('li').addClass('active');
                                $(this).parents('.dropdown-select').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
                                $('.btnConfirm').removeAttr("disabled");
                                dataset.factory = offsetList[i].factory;
                                dataset.lineName = offsetList[i].line;
                                dataset.mounterName = offsetList[i].mounterName;
                                dataset.deviceType = offsetList[i].deviceType;
                                dataset.deviceName = offsetList[i].deviceName;
                                dataset.deviceSubName = offsetList[i].deviceSubName;
                                dataset.repairReason = target;
                                // updateData();         
                            });
                        }
                    }
                    $("#txtDetail").html(txtDetail);
                    $("#mydiv").addClass("show");
                }

                if(type == "fail"){
                    var txtDetail = '';
                    for(i in failList){
                        if(failList[i].id == Number(index)){
                            txtDetail = failList[i].mounterName + " | " + failList[i].deviceName + " | " + failList[i].deviceSubName;
                            
                            var thead = "<tr><th>Location Code</th><th>Block</th><th>Error Code</th><th>Qty</th></tr>";
                            var tb = '';
                            var tf = '<div class="dropup dropdown-select">'
                                + '<button  style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">Select Repair Reason!<span class="caret"></span></button>'
                                + '<ul style=" left: unset; right:0;" class="dropdown-menu table-responsive pre-scrollable" role="menu" aria-labelledby="dropdownMenu1">';
                            var detailInfoList = failList[i].detailInfoList;
                            for(j in detailInfoList){
                                tb += '<tr><td>'+detailInfoList[j].locationCode+'</td><td>'+detailInfoList[j].block+'</td><td>'+detailInfoList[j].errorCode+'</td><td>'+detailInfoList[j].qty+'</td></tr>';
                            }
                            var dataReason = failList[i].repairReasonList;
                            for(k in dataReason){
                                tf += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">'+dataReason[k]+'</a></li>';
                            }
                            tf += '</ul> <button disabled="disabled" style="background: #333; color:#FFF" class="btn btn-default btnConfirm" onclick="updateData()">OK <span class="fa fa-check-circle"></span></button></div>';
                            $("#tblDetail thead").html(thead);
                            $("#tblDetail tbody").html(tb);
                            $("#mydivfooter").html(tf);

                            $('.dropdown-select').on( 'click', '.dropdown-menu li a', function() { 
                                var target = $(this).html();       
                                $(this).parents('.dropdown-menu').find('li').removeClass('active');
                                $(this).parent('li').addClass('active');
                                $(this).parents('.dropdown-select').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
                                $('.btnConfirm').removeAttr("disabled");
                                dataset.factory = offsetList[i].factory;
                                dataset.lineName = offsetList[i].line;
                                dataset.mounterName = offsetList[i].mounterName;
                                dataset.deviceType = offsetList[i].deviceType;
                                dataset.deviceName = offsetList[i].deviceName;
                                dataset.deviceSubName = offsetList[i].deviceSubName;
                                dataset.repairReason = target;
                                // updateData();         
                            });
                        }
                    }
                    $("#txtDetail").html(txtDetail);
                    $("#mydiv").addClass("show");
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    function updateData(){
         $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/aoi/offset/mark/checked/device",
            data: {
                factory: dataset.factory,
                line: dataset.lineName,
                mounterName: dataset.mounterName,
                deviceType: dataset.deviceType,
                deviceName: dataset.deviceName,
                deviceSubName: dataset.deviceSubName,
                repairReason: dataset.repairReason
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                // console.log(data);
                // cancel();
                closeModal();
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    function closeModal(){
        $("#mydiv").removeClass("show");
    }

</script>

<script>
//Make the DIV element draggagle:
dragElement(document.getElementById("mydiv"));

function dragElement(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  if (document.getElementById(elmnt.id + "header")) {
    /* if present, the header is where you move the DIV from:*/
    document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
  } else {
    /* otherwise, move the DIV from anywhere inside the DIV:*/
    elmnt.onmousedown = dragMouseDown;
  }

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
  }

  function closeDragElement() {
    /* stop moving when mouse button is released:*/
    document.onmouseup = null;
    document.onmousemove = null;
  }
}
</script>