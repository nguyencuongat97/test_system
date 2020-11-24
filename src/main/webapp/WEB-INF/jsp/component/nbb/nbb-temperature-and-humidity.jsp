<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<style>
    #tblChart td{
        padding: .5rem .75rem;
    }
    #tblChart th{
        background: #4f81bd;
        color: white;
        padding: .75rem .25rem;
        text-align: center;
    }
    #tblChart tbody tr:nth-of-type(odd){
        background-color: #b8cce4;
    }
    #tblChart tbody tr{
        background: #dce6f1;
    }
    #tblChart tbody tr:hover{
        color: #212529;
        background-color: rgba(0,0,0,.075);
    }
    /* #idBtnHumidity, #idBtnCPK{
        padding: 4px 15px;
        border-radius: 3px;
        background: #fff;
        margin-left: 7px;
        margin-top: 3px;
        box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.12), 0 3px 4px rgba(0, 0, 0, 0.24);
        color: #333;
        text-transform: capitalize;
    } */
</style>
    <link rel="stylesheet" href="/assets/css/custom/style.css" />
    <link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
    <link rel="stylesheet" href="/assets/custom/css/nbb/nbb-temperature-and-humidity.css" />
    <script src="/assets/js/custom/jquery-ui.js"></script>
    <div class="loader"></div>
    <div class="panel panel-re panel-flat row" style="overflow: hidden;">
        <div class="col-lg-12 nav-tools">
            <div class="col-lg-10">
                <input type="button" class="btn btn-border" id="btnChangeView" onclick="changeView()" value="Show Without Name" />
                <span style="font-size: 16px;font-weight: bold;color: #fff;">&rarr;</span>
                <div class="btn-group">
                    <select class="btn btn-border btn-with-name slShowWithName" onchange="showWithName()">
                    <option value="all">Humidity + Temperature</option>
                    <option value="humidity">Humidity</option>
                    <option value="temperature">Temperature</option>
                </select>
                </div>
                <div class="btn-group">
                    <select class="btn btn-border btn-without-name slShowWithoutName" onchange="showWithName()" style="display: none;">
                    <option value="temperature">Temperature</option>
                    <option value="humidity">Humidity</option>
                </select>
                </div>
            </div>
            <div class="col-lg-2" style="text-align: center;">
                <button class="btn btn-border" id="btnEditLayout" data-toggle="modal" data-target="#modal-sm">Layout</button>
                <button class="btn btn-border" id="btnAddNew" onclick="showDivSave('add')"><i class="fa fa-plus"></i></button>
                <button class="btn btn-border" id="btnEdit" onclick="showDivSave('edit')"><i class="fa fa-pencil"></i></button>
            </div>

        </div>
        <div>
            <button class="btn" id="btnSetup" onclick="showNavTools()"> <i class="fa fa-cog"></i> </button>
        </div>
        <div style="width: 100%; padding: 0; margin: 0;">
            <!-- <img id="imgLayout" src="/assets/images/custom/DemoLine.png" /> -->
            <img id="imgLayout" />
        </div>
        <div id="view-content">
            <div id="btnTest" class="div-drag ui-widget-content" style="top: 30px; left: 250px;">
                <div class="btn legitRipple btnView bg-danger pulse" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="ABC" data-container="body"><i class="fa fa-frown-o"></i></div>
            </div>

            <div id="btnTest2" class="div-drag ui-widget-content" style="left: 30px; top: 250px;">
                <div class="btn legitRipple btnView bg-success"><i class="fa fa-smile-o"></i></div>
            </div>
        </div>
        <div id="view-edit">
            <div class="row" id="lblNotify" style="text-align: center; font-size: 16px;">
                <strong>Click to Device to EDIT!</trong>
        </div>
        <div class="row">
            <div class="form-group">
                <div class="col-xs-3">
                    <label for="ipCode" class="col-sm-2 col-form-label" style="margin-top: 10px; font-weight: bold;">Code: </label>
                    <div class="col-sm-10">
                        <input type="text" class="input-setup" id="ipCode" placeholder="Code">
                    </div>
                </div>
                <div class="col-xs-3">
                    <label for="ipName" class="col-sm-2 col-form-label" style="margin-top: 10px; font-weight: bold;">Device: </label>
                    <div class="col-sm-10">
                        <input type="text" class="input-setup" id="ipName" placeholder="Device Name">
                    </div>             
                </div>
                <div class="col-xs-3">
                    <label for="ipName" class="col-sm-2 col-form-label" style="margin-top: 10px; font-weight: bold;">Layout: </label>
                    <div class="col-sm-10">
                        <input type="text" class="input-setup" id="ipLocation" placeholder="Layout">
                    </div>
                </div>
                <div class="col-xs-3" style="padding: 4px 20px; text-align: right;">
                    <button id="btnSaveAdd" class="btn btn-border btn-success"><i class="fa fa-check"></i> Save</button>
                    <button id="btnConfirm" class="btn btn-border btn-success"><i class="fa fa-check"></i> Save</button>
                    <button id="btnDelete" class="btn btn-border btn-danger"><i class="fa fa-trash-o"></i> Delete</button>
                    <button class="btn btn-border btn-default" onclick="hideDivSave()"><i class="fa fa-undo"></i> Cancel</button>
                </div>
                <div style="display: none;">
                    <span id="spLocX">5</span>
                    <span id="spLocY">5</span>
                </div>
            </div>
        </div>
        
    </div>
</div>

<div class="modal fade" id="modal-sm">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Change Layout </h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row editLayout">
                    <div class="col-sm-8">
                        <select class="btn btn-border" id="slChangeLayout" onchange="changeLayout()" style="width: 80%;">
                            <option disabled="disabled" selected="selected">Select Layout</option>
                            <option value="bn3_1f">BN3-1F</option>
                            <option value="bn3_2f">BN3-2F</option>
                            <option value="bn3_3f">BN3-3F</option>
                            <option value="nbb">NBB</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <button class="btn btn-border" id="addNewLayout">Upgrade</button>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="display: none;">
                <div class="row addLayout">
                    <div class="col-sm-12" style="padding: 0;">
                            <input type="file" class="form-control btn-border" id="fileLayout">
                    </div>
                    <div class="form-group col-sm-12">
                            <input type="text" class="form-control" id="txtLocation" placeholder="Location" disabled="disabled" />
                    </div>
                    <div class="col-sm-12">
                        <button id="btnSubmitLayout" class="btn btn-border" onclick="uploadFileLayout()"><i class="fa fa-upload"></i> Upload</button>
                        <button id="btnCancelLayout" class="btn btn-border"><i class="fa fa-undo"></i> Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal chart-->
<div id="modalChart" class="modal fade" role="dialog">
	<div class="modal-dialog modal-xl" style="font-size: 13px;background:#fff;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modalChart_title text-bold" style="margin-top: 5px; margin-bottom: 5px;"></h6>
            </div>
        	<div class="modal-body" style="background-image: linear-gradient(#e0e0e0, white) !important;padding-top: .5rem;">
                <div class="row">
                    <div class="col-xs-12 col-sm-12" style="margin:0;padding: 0;">
                        <div class="panel input-group" style="height: 26px; margin: 5px 0px;">
                            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
                        </div>
                    </div>
                    <!-- <div class="col-xs-3 col-sm-3" style="margin:0;padding: 0;text-align: right;">
                        <a class="btn" id="idBtnHumidity" onclick="functionShowTab1()">
                            DATA
                        </a>
                        <a class="btn" id="idBtnCPK" onclick="functionShowCPK()">
                            CPK
                        </a>
                    </div> -->
                </div>
                <div class="row class-tab-1">
                    <div class="col-xs-9 col-sm-9" style="margin:0;padding: 0;">
                        <!-- <div class="panel panel-flat panel-body chart-sm" id="lineChart"style="height: 450px !important;margin: 0;"></div> -->
                        <div class="panel panel-flat panel-body chart-sm" id="Humidity_chart"style="height: 450px !important;margin: 0;"></div>
                        <div class="panel panel-flat panel-body chart-sm" id="Temperature_chart"style="height: 450px !important;margin: 0;"></div>
                    </div>
                    <div class="col-xs-3 col-sm-3" style="margin:0;padding: 0;">
                        <div class="panel panel-flat panel-body" style="margin: 0 .15rem;padding:0;margin-left: .5rem;">
                            <div class="col-sm-12 table-responsive pre-scrollable" style="min-height: 900px;color: #292929;padding: unset;background: white;font-size: 11px;">
                                <table id="tblChart" class="table table-xxs table-striped table-sticky table-hover" style="text-align: center">
                                    <thead>
                                        <th>Time</th>
                                        <th>Humidity</th>
                                        <th>Temperature</th>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- <div class="row class-tab-2">
                    <div class="col-xs-9 col-sm-9" style="margin:0;padding: 0;">
                        <div class="panel panel-flat panel-body chart-sm" id="Humidity_chart_cpk"style="height: 450px !important;margin: 0;"></div>
                        <div class="panel panel-flat panel-body chart-sm" id="Temperature_chart_cpk"style="height: 450px !important;margin: 0;"></div>
                    </div>
                    <div class="col-xs-3 col-sm-3" style="margin:0;padding: 0;">
                        <div class="panel panel-flat panel-body" style="margin: 0 .15rem;padding:0;margin-left: .5rem;">
                            <div class="col-sm-12 table-responsive pre-scrollable" style="min-height: 900px;color: #292929;padding: unset;background: white;font-size: 11px;">
                                <table id="tblChart_cpk" class="table table-xxs table-striped table-sticky table-hover" style="text-align: center">
                                    <thead>
                                        <th>Time</th>
                                        <th>Humidity</th>
                                        <th>Temperature</th>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div> -->
            </div>
        </div>
	</div>
</div>
<script>
    // var checkClick;
    // $('.class-tab-2').addClass('hidden');
    // $('#idBtnHumidity').css('background',"#b3afaf");
    // function functionShowTab1(){
    //     checkClick=0;
    //     $('.class-tab-1').removeClass('hidden');
    //     $('.class-tab-2').addClass('hidden');
    //     $('#idBtnHumidity').css('background',"#b3afaf");
    //     $('#idBtnCPK').css('background',"#ffffff");
    // }
    // function functionShowCPK(){
    //     checkClick=1;
    //     $('.class-tab-2').removeClass('hidden');
    //     $('.class-tab-1').addClass('hidden');
    //     $('#idBtnCPK').css('background',"#b3afaf");
    //     $('#idBtnHumidity').css('background',"#ffffff");
    // }
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if(dataset == null || dataset == undefined || dataset.layout == null || dataset.layout == undefined || dataset.layout == ""){
        dataset = {
            layout : "bn3_1f"
        };
        $('#imgLayout').attr('src','/ws-data/image/temperature/bn3_1f.jpg');
    } else $('#imgLayout').attr('src','/ws-data/image/temperature/'+dataset.layout+'.jpg');
    
    
    function changeLayout(){
        var layout = $('#slChangeLayout').val();
        $('#imgLayout').attr('src','/ws-data/image/temperature/'+layout+'.jpg');
        dataset.layout = layout;
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        loadDataWithName();
    }
    var dataSTT = {};
    // loadData();
    function loadData() {
        $.ajax({
            type: "GET",
            url: "/api/test/temperature",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if (!$.isEmptyObject(data)) {
                    $(".loader").removeClass("disableSelect");
                    $('#view-content').html('');
                    for (i in data) {
                        if(data[i].location == dataset.layout){
                            var html = '';
                            if(data[i].locX != null || data[i].locY != null){
                                if(data[i].latestData != null){
                                    if(data[i].latestData.deviceLocation == null){
                                        data[i].latestData.deviceLocation = "";
                                        data[i].location = "";
                                    }
                                    var value = '<div><b>Device: </b><span>'+data[i].latestData.deviceCode+'</span></div>' +
                                                '<div><b>Temperature: </b><span>'+data[i].latestData.temperature+'&deg;C</span></div>' +
                                                '<div><b>Humidity: </b><span>'+data[i].latestData.humidity+'%</span></div>';
                                    if(data[i].latestData.humidity > 50 || data[i].latestData.temperature > 23){
                                        html = '<div id="dragDiv'+data[i].id+'" class="div-drag ui-widget-content" style="top: '+data[i].locX+'px; left: '+data[i].locY+'px;">'
                                        + '<div class="btn btnView bg-danger pulse btnHumi" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" style=" display: none;" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" data-container="body"><span>'+data[i].latestData.humidity+'%</span></div>'
                                        + '<div class="btn btnView bg-danger pulse btnTemp" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" data-container="body"><span>'+data[i].latestData.temperature+'&deg;C</span></div>'
                                        + '<div id="editSelect'+data[i].id+'" data-id="'+data[i].id+'" data-name="'+data[i].name+'" data-code="'+data[i].code+'" data-location="'+data[i].location+'" data-locX="'+data[i].locX+'" data-locY="'+data[i].locY+'" onclick="editDevice('+data[i].id+')" class="btn btnView bg-danger pulse btnEdit" style=" display: none;"><span>'+data[i].latestData.temperature+'&deg;C</span></div></div>';
                                    }else{
                                        html = '<div id="dragDiv'+data[i].id+'" class="div-drag ui-widget-content" style="top: '+data[i].locX+'px; left: '+data[i].locY+'px;">'
                                        + '<div class="btn btnView bg-success btnHumi" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" style=" display: none;" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" data-container="body"><span>'+data[i].latestData.humidity+'%</span></div>'
                                        + '<div class="btn btnView bg-success btnTemp" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" data-container="body"><span>'+data[i].latestData.temperature+'&deg;C</span></div>'
                                        + '<div id="editSelect'+data[i].id+'" data-id="'+data[i].id+'" data-name="'+data[i].name+'" data-code="'+data[i].code+'" data-location="'+data[i].location+'" data-locX="'+data[i].locX+'" data-locY="'+data[i].locY+'" onclick="editDevice('+data[i].id+')" class="btn btnView bg-success btnEdit" style=" display: none;"><span>'+data[i].latestData.temperature+'&deg;C</span></div></div>';
                                    }   
                                } else{
                                    var value = '<div><b>Device: </b><span>'+data[i].code+'</span></div>' +
                                                '<div><b>Temperature: </b><span>NO DATA</span></div>' +
                                                '<div><b>Humidity: </b><span>NO DATA</span></div>';
                                    html = '<div id="dragDiv'+data[i].id+'" class="div-drag ui-widget-content" style="top: '+data[i].locX+'px; left: '+data[i].locY+'px;">'
                                        + '<div class="btn btnView bg-grey btnHumi" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" style=" display: none;" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" data-container="body"><span>? %</span></div>'
                                        + '<div class="btn btnView bg-grey btnTemp" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" data-container="body"><span>? &deg;C</span></div>'
                                        + '<div id="editSelect'+data[i].id+'" data-id="'+data[i].id+'" data-name="'+data[i].name+'" data-code="'+data[i].code+'" data-location="'+data[i].location+'" data-locX="'+data[i].locX+'" data-locY="'+data[i].locY+'" onclick="editDevice('+data[i].id+')" class="btn btnView bg-grey btnEdit" style=" display: none;"><span>? &deg;C</span></div></div>';
                                }

                                $('#view-content').append(html);
                            }
                        }
                    }
                    $('[data-popup=popover]').popover({
                        template: '<div class="popover" style="min-width: 170px; background-color: #ffffff; border: 1px solid #4e7af3;"><div class="arrow"></div><div class="popover-content"></div></div>'
                    });
                    $(".loader").addClass("disableSelect");
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    loadDataWithName();
    function loadDataWithName() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/temperature",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if (!$.isEmptyObject(data)) {
                    $(".loader").removeClass("disableSelect");
                    $('#view-content').html('');
                    for (i in data) {
                        if(data[i].location == dataset.layout){
                            var html = '';
                            if(data[i].locX != null || data[i].locY != null){
                                var logY = getResponsiveY(data[i].locY);
                                var logX = getResponsiveX(data[i].locX);
                                var lineName = '';
                                if(data[i].code.length > 6){
                                    lineName = data[i].code.slice(7);
                                } else lineName = data[i].code;
                                if(data[i].latestData != null){
                                    if(data[i].latestData.deviceLocation == null){
                                        data[i].latestData.deviceLocation = "";
                                        data[i].location = "";
                                    }
                                    html = '<div id="dragDiv'+data[i].id+'" class="div-drag ui-widget-content" style="top: '+logX+'px; left: '+logY+'px;">'
                                    + '<div class="btn btnViewName btnHumi" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" style=" display: none;"><span>Line : '+lineName+'</span></br><img class="imgIcon" src="/assets/images/custom/icon-humidity.png" /><span class="spDigital">'+data[i].latestData.humidity+'</span><span> %</span></div>'
                                    + '<div class="btn btnViewName btnTemp" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" style=" display: none;"><span>Line : '+lineName+'</span></br><img class="imgIcon" src="/assets/images/custom/icon-temperature.png" /><span class="spDigital">'+data[i].latestData.temperature+'</span> <span style="font-size: 1rem; font-weight: 600"> &deg;C</span></div>'
                                    + '<div class="btn btnViewName btnAll" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')"><span>Line : '+lineName+'</span></br><img class="imgIcon" src="/assets/images/custom/icon-humidity.png" /><span class="spDigital">'+data[i].latestData.humidity+'</span><span> %</span></br><img class="imgIcon" src="/assets/images/custom/icon-temperature.png" /><span class="spDigital">'+data[i].latestData.temperature+'</span><span> &deg;C</span></div>'
                                    + '<div id="editSelect'+data[i].id+'" data-id="'+data[i].id+'" data-name="'+data[i].name+'" data-code="'+data[i].code+'" data-location="'+data[i].location+'" data-locX="'+data[i].locX+'" data-locY="'+data[i].locY+'" onclick="editDevice('+data[i].id+')" class="btn btnViewName btnEdit" style="display: none;"><span>Line: '+lineName+'</span></br><img class="imgIcon" src="/assets/images/custom/icon-humidity.png" /><span class="spDigital">'+data[i].latestData.humidity+'</span><span style="font-size: 1rem; font-weight: 600"> %</span></br><img class="imgIcon" src="/assets/images/custom/icon-temperature.png" /><span class="spDigital">'+data[i].latestData.temperature+'</span><span> &deg;C</span></div></div>';
                                } else{
                                    html = '<div id="dragDiv'+data[i].id+'" class="div-drag ui-widget-content" style="top: '+logX+'px; left: '+logY+'px;">'
                                        + '<div class="btn btnViewName bg-grey btnHumi" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" style=" display: none;"><span>Line : '+lineName+'</span></br><span><img class="imgIcon" src="/assets/images/custom/icon-humidity.png" />: ? %</span></div>'
                                        + '<div class="btn btnViewName bg-grey btnTemp" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')" style=" display: none;"><span>Line : '+lineName+'</span></br><span><img class="imgIcon" src="/assets/images/custom/icon-temperature.png" />: ? &deg;C</span></div>'
                                        + '<div class="btn btnViewName bg-grey btnAll" onclick="showMyChart(\''+data[i].latestData.deviceCode+'\')"><span>Line : '+lineName+'</span></br><span><img class="imgIcon" src="/assets/images/custom/icon-humidity.png" />: ? %</span></br><span><img class="imgIcon" src="/assets/images/custom/icon-temperature.png" />: ? &deg;C</span></div>'
                                        + '<div id="editSelect'+data[i].id+'" data-id="'+data[i].id+'" data-name="'+data[i].name+'" data-code="'+data[i].code+'" data-location="'+data[i].location+'" data-locX="'+data[i].locX+'" data-locY="'+data[i].locY+'" onclick="editDevice('+data[i].id+')" class="btn btnViewName bg-grey btnEdit" style=" display: none;"><span>Line: '+lineName+'</span></br><img class="imgIcon" src="/assets/images/custom/icon-humidity.png" /><span>? %</span></br><img class="imgIcon" src="/assets/images/custom/icon-temperature.png" /><span>? &deg;C</span></div></div>';
                                }

                                $('#view-content').append(html);
                            }
                        }
                    }
                    $('[data-popup=popover]').popover({
                        template: '<div class="popover" style="min-width: 170px; background-color: #ffffff; border: 1px solid #4e7af3;"><div class="arrow"></div><div class="popover-content"></div></div>'
                    });
                    $(".loader").addClass("disableSelect");
                    var checkEmpty = $('.btnHumi');
                    if(checkEmpty.length == 0){
                        var fixHeight = getResponsiveY(0);
                        var fixWidth = getResponsiveX(0);
                    }
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
               dataset.timeout = setTimeout(loadDataWithName, 300000, dataset.layout);
            }
        });
    }

    var per;
    fixResponse();
    function fixResponse(){
        var scrWidth = $( document ).width();
        var t = scrWidth - 1366;
        var widthImg = 1366 + t;
        $('#imgLayout').css('width',widthImg);
        per = widthImg/1366;

        var heightImg = 525*per;
        $('#imgLayout').css('height',heightImg);
    }
    function getResponsiveY(y){
        var r = y*per;
        return r;
    }
    function getResponsiveX(x){
        var r = x*per;
        return r;
    }
    function getEditResponsiveY(y){
        var r= y/per;
        return r;
    }
    function getEditResponsiveX(x){
        var r = x/per;
        return r;
    }

    function showNavTools(){
        if($('.nav-tools').css('display') == 'block'){
            $('.nav-tools').css('display','none');
            $('#btnSetup').removeClass('active');
        } else{
            $('.nav-tools').css('display','block');
            $('#btnSetup').addClass('active');
        }
    }

    function changeView(){
        var value = $('#btnChangeView').val().toLowerCase();
        if(value == "show without name"){
            $('.btn-with-name').css('display','none');
            $('.btn-without-name').css('display','inline-block');
            $('#btnChangeView').val('Show With Name');
            loadData();
        } else if(value == "show with name"){
            $('.btn-with-name').css('display','inline-block');
            $('.btn-without-name').css('display','none');
            $('#btnChangeView').val('Show Without Name');
            loadDataWithName();
        }
    }

    function showWithName(){
        var btnC = $('#btnChangeView').val().toLowerCase();
        if(btnC == "show without name"){
            var value = $('.slShowWithName').val();
            if(value == "all"){
                $('.btnHumi').css('display','none');
                $('.btnTemp').css('display','none');
                $('.btnAll').css('display','block');
            } else if(value == "temperature"){
                $('.btnHumi').css('display','none');
                $('.btnTemp').css('display','block');
                $('.btnAll').css('display','none');
            } else if(value == "humidity"){
                $('.btnHumi').css('display','block');
                $('.btnTemp').css('display','none');
                $('.btnAll').css('display','none');
            }
        } else if(btnC == "show with name"){
            var value = $('.slShowWithoutName').val();
            if(value == "temperature"){
                $('.btnHumi').css('display','none');
                $('.btnTemp').css('display','block');
                $('.btnAll').css('display','none');
            } else if(value == "humidity"){
                $('.btnHumi').css('display','block');
                $('.btnTemp').css('display','none');
                $('.btnAll').css('display','none');
            }
        }

    }

    function showWithoutName(value){
        if(value == "temperature"){
            $('.btnHumi').css('display','none');
            $('.btnTemp').css('display','block');
        } else{
            $('.btnHumi').css('display','block');
            $('.btnTemp').css('display','none');
        }
    }

    function showDivSave(type){ 
        $('#btnSetup').css('display','none');
        $('#view-edit').css('display','block');
        showNavTools();
        $('#ipCode').focus();
        if(type == 'edit'){
            $('#lblNotify').css('display','block');
            $('#btnDelete').css('display','inline-block');
            $('#btnSaveAdd').css('display','none');
            $('#btnConfirm').css('display','inline-block');
            $('.btnEdit').css('display','block');
            $('.btnHumi').css('display','none');
            $('.btnTemp').css('display','none');
            $('.btnAll').css('display','none');
        } else if(type == 'add'){
            $('#lblNotify').css('display','none');
            $('#btnDelete').css('display','none');
            $('#btnSaveAdd').css('display','inline-block');
            $('#btnConfirm').css('display','none');
            var html = '';
            if($('#btnChangeView').val().toLowerCase() == 'show with name'){
                html = '<div id="dragDivNew" class="div-drag ui-widget-content" style="top: 5px; left: 5px;">'
                     + '<div class="btn btnView bg-info pulse"><span>NEW</span></div></div>';
            } else{
                html = '<div id="dragDivNew" class="div-drag ui-widget-content" style="top: 5px; left: 5px;">'
                     + '<div class="btn btnViewName pulse" style="text-align:center; min-height: 5.5rem;"><span>NEW</span></div></div>';
            }          
            $('#view-content').append(html);
            $('#dragDivNew').draggable({
                containment: '#imgLayout',
                drag: function(){
                    var x =  $(this).position();
                    $('#spLocX').html(x.top);
                    $('#spLocY').html(x.left);
                }
            });
        }
    }

    function hideDivSave(){
        window.location.reload();
    }

    function addNewDevice(){
        if($('#ipCode').val() != ''){
            var x = Number($('#spLocX').html());
            var y = Number($('#spLocY').html());
            var locX = getEditResponsiveX(x);
            var locY = getEditResponsiveY(y);
            var dataAdd = {
                "code": $('#ipCode').val(),
                "name": $('#ipName').val(),
                "layout": dataset.layout,
                "locX": locX,
                "locY": locY
            }

            $.ajax({
                type: 'POST',
                url: '/api/test/temperature',
                data: JSON.stringify(dataAdd),
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    if(data){
                        alert('ADD NEW SUCCESS!');
                        window.location.reload();
                    }
                    else alert('ADD NEW FAIL!');
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });

        }else alert('Input Code!!'); 
    }

    function deleteDevice(id){
        $.ajax({
            type: 'DELETE',
            url: '/api/test/temperature/'+id,
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(data){
                    alert('DELETE SUCCESS!');
                    window.location.reload();
                }
                else alert('DELETE FAIL!');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function editDevice(id){
        $('#ipCode').val($('#editSelect'+id)[0].dataset.code);
        $('#ipName').val($('#editSelect'+id)[0].dataset.name);
        $('#ipLocation').val($('#editSelect'+id)[0].dataset.location);
        $('#spLocX').html($('#editSelect'+id)[0].dataset.locX);
        $('#spLocY').html($('#editSelect'+id)[0].dataset.locY);
        if($('.div-drag').draggable()) {
            $('.div-drag').draggable('destroy');
        }
        $('#dragDiv'+id).draggable({
            containment: '#imgLayout',
            drag: function(){
                var x =  $(this).position();
                $('#spLocX').html(x.top);
                $('#spLocY').html(x.left);
            }
        });

        dataSTT.idEdit = id;
        dataSTT.idDelete = id;

    }

    function confirmEdit(id){
        if($('#ipCode').val() != ''){
            var x = Number($('#spLocX').html());
            var y = Number($('#spLocY').html());
            var locX = getEditResponsiveX(x);
            var locY = getEditResponsiveY(y);
            var dataEdit = {
                "code": $('#ipCode').val(),
                "name": $('#ipName').val(),
                "layout": $('#ipLocation').val(),
                "locX": locX,
                "locY": locY
            }
            $.ajax({
                type: 'PUT',
                url: '/api/test/temperature/'+id,
                data: JSON.stringify(dataEdit),
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    if(data){
                        alert('EDIT SUCCESS!');
                        window.location.reload();
                    }
                    else alert('EDIT FAIL!');
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });

        }else alert('Input Code!!'); 
    }

    $('#btnSaveAdd').on('click', function(){
        addNewDevice();
    });
    $('#btnConfirm').on('click', function(){
        confirmEdit(dataSTT.idEdit);
    });
    $('#btnDelete').on('click', function(){
        deleteDevice(dataSTT.idDelete);
    });

    $('#addNewLayout').on('click', function(){
        $('.modal-footer').css('display','block');
        $('.modal-body').css('display','none');
        $("#txtLocation").val(dataset.layout);
    });
    $('#btnCancelLayout').on('click', function(){
        $('.modal-footer').css('display','none');
        $('.modal-body').css('display','block');
    });

    function uploadFileLayout(){
        var file = $("#fileLayout").prop('files')[0];
        var location = $("#txtLocation").val().toLowerCase();
        if(file != undefined && location != ""){
            if(file.type.indexOf('image') > -1){
                // alert("Upload Comingsoon");
                var form = new FormData();
                form.append("location", location);
                form.append("uploadedFile", file);

                $.ajax({
                    "async": true,
                    "crossDomain": true,
                    "url": "/api/test/temperature/layout",
                    "method": "POST",
                    "processData": false,
                    "contentType": false,
                    "mimeType": "multipart/form-data",
                    "data": form,
                    "fileName": location,
                    success: function(response){
                        if(response){
                            alert('ADD NEW SUCCESS!');
                            dataset.layout = this.fileName;
                            window.localStorage.setItem('dataset', JSON.stringify(dataset));
                            $('#imgLayout').attr('src','/ws-data/image/temperature/'+this.fileName+'.jpg');
                            window.location.reload();
                        }
                        else alert('ADD NEW FAIL!');
                    },
                    failure: function(errMsg) {
                        console.log(errMsg);
                    },
                });
            } else{
                alert("INPUT AN IMAGE FILE!");
            }
        } else alert("Input File!");
    }
    var dataH=[],dataT=[],dataMinH=[],dataMaxH=[],dataMaxT=[],dataMinT=[];
    var deviceCode;
    function showMyChart(ideviceCoded){
        deviceCode=ideviceCoded;
		$('#modalChart').modal('show');
        $('.modalChart_title').html(ideviceCoded);

        $.ajax({
            type: "GET",
            url: "/api/test/temperature/tracking/by/day",
            data: {
                deviceCode: ideviceCoded,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                dataH=[],dataT=[];
                if (res) {
                    $('#tblChart tbody').html('');
                    var htmlTd='';
                    // console.log(res)
                    var data=res.data;
                    for (i=0;i<data.length;i++) {
                        var strdate;
                        var dataDate = data[i].createdAt;
                        strdate = dataDate.slice(5, dataDate.length);

                        htmlTd+='<tr><td>'+data[i].createdAt+'</td><td>'+data[i].humidity+'%</td><td>'+data[i].temperature+'°C</td></tr>'
                        dataH.push({ name: strdate, y: data[i].humidity });
                        dataT.push({ name: strdate, y: data[i].temperature });
                    }
                    $('#tblChart tbody').html(htmlTd);
                    loadChart(dataH,'Humidity',30,60,58,45,32,'%');
                    loadChart(dataT,'Temperature',18,28,27,23,19,'°C');
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
        // $.ajax({
        //     type: "GET",
        //     url: "/api/test/temperature/cpk/by/day",
        //     data: {
        //         deviceCode: ideviceCoded,
        //         timeSpan: dataset.timeSpan
        //     },
        //     contentType: "application/json; charset=utf-8",
        //     success: function (res) {
        //         dataH=[],dataT=[];
        //         if (res) {
        //             $('#tblChart_cpk tbody').html('');
        //             var htmlTd_cpk='';
        //             var data=res.data;
        //             for (i=0;i<data.length;i++) {
        //                 var strdate, strValue,strTemp;
        //                 var dataDate = data[i].createdAt;
        //                 var datavalue = data[i].humidityCpk;
        //                 var dataTempCPK = data[i].temperatureCpk;
        //                 strdate = dataDate.slice(5, dataDate.length);
        //                 strValue = datavalue.toFixed(4);
        //                 strTemp = dataTempCPK.toFixed(4);
        //                 htmlTd_cpk+='<tr><td>'+data[i].createdAt+'</td><td>'+strValue+'</td><td>'+strTemp+'</td></tr>'
        //                 dataH.push({ name: strdate, y: Number(strValue) });
        //                 dataT.push({ name: strdate, y: Number(strTemp) });
        //             }
        //             $('#tblChart_cpk tbody').html(htmlTd_cpk);
        //             loadChart_cpk(dataH,'Humidity',1.33,'');
        //             loadChart_cpk(dataT,'Temperature',1.33,'');
        //         }
        //     },
        //     failure: function (errMsg) {
        //         console.log(errMsg);
        //     }
        // });

    }
    function loadChart(data,title,min,max,ucl,cl,lcl,unit,dataMin,dataMax) {
        Highcharts.chart(title+'_chart', {
            chart: {
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: title +' '+ deviceCode,
                style: {
                    fontSize: '14px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category',
            },
            yAxis: [{
                plotLines: [{
                    value: min,
                    dashStyle: 'shortdash',
                    color: 'red',
                    zIndex:5,
                    label: {
                        text: 'LSL: '+min+unit,
                        style: {
                            color: 'red',
                            }
                        },
                    },
                    {
                    value: ucl,
                    dashStyle: 'shortdash',
                    color: 'orange',
                    zIndex:2,
                    label: {
                        text: 'UCL: '+ucl+unit,
                        style: {
                            color: 'orange',
                            }
                        },
                    },
                    {
                    value: cl,
                    dashStyle: 'shortdash',
                    color: '#3399cc',
                    zIndex:3,
                    label: {
                        text: 'CL: '+cl+unit,
                        style: {
                            color: '#3399cc',
                            }
                        },
                    },
                    {
                    value: lcl,
                    dashStyle: 'shortdash',
                    color: 'orange',
                    zIndex:4,
                    label: {
                        text: 'LCL: '+lcl+unit,
                        style: {
                            color: 'orange',
                            }
                        },
                    },
                    {
                    value: max,
                    dashStyle: 'shortdash',
                    color: 'red',
                    zIndex:1,
                    label: {
                        text: 'USL: '+max+unit,
                        style: {
                            color: 'red',
                            }
                        },
                }],
               
                softMin: min,
                softMax: max,
                labels: {
                    format: '{value}'+unit,
                },
                title: {
                    text: '',
                },
                },
            ],

            tooltip: {
                shared: true,
                pointFormat: '{point.y:0f}'+unit
            },
            // colors: color,
            legend: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                line: {
                    states: {
                        hover: {
                            lineWidth: 2
                        }
                    },
                    marker: {
                        enabled: false
                    }
                },
                series: {
                    stacking: '',
                    turboThreshold:500000,
                    point: {
                        events: {
                        }
                    }
                }
            },
            series: [
                {
                    name: '',
                    type: 'line',
                    data: data,
                    threshold: max,
                    negativeColor: 'green',
                    color: 'red',
                    tooltip: {
                        valueDecimals: 2
                    }
                }]
        });
    }
    // function loadChart_cpk(data,title,max,unit,dataMax) {
    //     Highcharts.chart(title+'_chart_cpk', {
    //         chart: {
    //             style: {
    //                 fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
    //             }
    //         },
    //         title: {
    //             text: title +' '+ deviceCode + ' CPK',
    //             style: {
    //                 fontSize: '14px',
    //                 fontWeight: 'bold',
    //                 textTransform:'upperCase'
    //             }
    //         },
    //         xAxis: {
    //             type: 'category',
    //         },
    //         yAxis: [{
    //             plotLines: [
    //                 {
    //                 value: max,
    //                 dashStyle: 'shortdash',
    //                 color: 'red',
    //                 zIndex:1,
    //                 label: {
    //                     text: max+unit,
    //                     style: {
    //                         color: 'red',
    //                         }
    //                     },
    //             }],
    //             softMax: max,
    //             labels: {
    //                 format: '{value}'+unit,
    //             },
    //             title: {
    //                 text: '',
    //             },
    //             },
    //         ],

    //         tooltip: {
    //             shared: true,
    //             pointFormat: '{point.y:0f}'+unit
    //         },
    //         legend: {
    //             enabled: false
    //         },
    //         navigation: {
    //             buttonOptions: {
    //                 enabled: false
    //             }
    //         },
    //         credits: {
    //             enabled: false
    //         },
    //         plotOptions: {
    //             line: {
    //                 states: {
    //                     hover: {
    //                         lineWidth: 2
    //                     }
    //                 },
    //                 marker: {
    //                     enabled: false
    //                 }
    //             }
    //         },
    //         series: [
    //             {
    //                 name: '',
    //                 type: 'line',
    //                 data: data,
    //                 threshold: max,
    //                 negativeColor: 'red',
    //                 color: 'green',
    //                 tooltip: {
    //                     valueDecimals: 2
    //                 }
    //             }]
    //     });
    // }
    getTimeNow();
    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).add(-7,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
                $('input[name=timeSpan]').on('change', function () {
                    dataset.timeSpan = this.value;
                    showMyChart(deviceCode)
                });

                dataset.timeSpan = startDate + ' - ' + endDate;
                delete current;
                delete startDate;
                delete endDate;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
</script>