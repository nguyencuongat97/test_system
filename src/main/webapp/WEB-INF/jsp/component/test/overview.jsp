<div class="row" style="padding: 10px; background-color: #fff;">
    <div>
        <span style="padding-right: 20px;">
            <img src="/assets/images/custom/icon-free.png">
            <span><b>Total Warning: </b></span>
            <span><label name="total-warning">10</label></span>
        </span>
        <span style="padding-right: 20px;">
            <img src="/assets/images/custom/icon-on-duty.png">
            <span><b>TE on duty: </b></span>
            <span><label name="te-on-duty">5</label></span>
        </span>
        <span>
            <img src="/assets/images/custom/icon-free.png">
            <span><b>TE available: </b></span>
            <span><label name="te-free">2</label></span>
        </span>
    </div>
    <div class="col-sm-11">
        <div id="layout" style="position: relative; overflow: auto;">
            <img class="imageLayout" src="/assets/images/custom/b04.png" style="width: 1280px; height: 550px; position: relative;">
        </div>
    </div>
    <div id="available" class="col-sm-1" style="padding: 10px 0; background-color: #e0e0e0;">

    </div>
</div>

<script src="/assets/js/plugins/socket/socket.io.js"></script>
<script>

    //var socket = io('http://10.224.81.70:2019');

    initTester();
    initAutoMachine();

    function initTester() {
        $.ajax({
            type: 'GET',
            url: '/api/test/line/status',
            data: {
                factory: 'B04'
            },
            success: function(data){
                if (!$.isEmptyObject(data)) {
                    var html = '';
                    var free = 0;
                    var onduty = 0;
                    for (i in data) {
                        if (data[i].lineName == '') {
                            var resourceList = data[i].resourceList;
                            //html += '<div class="line" style="top: 0px; left: 1162px; width: 118px; height: 549px; padding: 10px 0">';
                            for (j in resourceList) {
                                var resource = '<div><b>ID: </b><span>'+resourceList[j].employeeNo+'</span></div>' +
                                               '<div><b>Name: </b><span>'+resourceList[j].name+'</span></div>' +
                                               '<div><b>Success/Action: </b><span>'+resourceList[j].taskSuccess+'/'+resourceList[j].taskNumber+'</span></div>' +
                                               '<div><b>Speed: </b><span>'+resourceList[j].processingTime+'</span></div>';

                                //html += '<div data-popup="popover" data-trigger="hover" data-placement="left" data-html="true" data-content="'+resource+'" style="width: 118px; height: 65px; text-align: center;">' +
                                //            '<img src="/assets/images/custom/icon-free.png">' +
                                //            '<div>'+resourceList[j].name+'</div>' +
                                //        '</div>';

                                $('#available').append('<div data-popup="popover" data-trigger="hover" data-placement="left" data-html="true" data-content="'+resource+'" style="width: 100%; height: 65px; text-align: center;">' +
                                                           '<img src="/assets/images/custom/icon-free.png">' +
                                                           '<div>'+resourceList[j].name+'</div>' +
                                                       '</div>');
                            }
                            //html += '</div>';

                            free = resourceList.length;
                        } else {
                            var x = data[i].left;
                            var y = data[i].top;
                            var w = data[i].width;
                            var h = data[i].height;

                            var groupList = data[i].groupList;
                            var count = groupList.length;

                            var resourceList = data[i].resourceList;

                            var resource = '';
                            for (j in resourceList) {
                                resource += '<div><b>ID: </b><span>'+resourceList[j].employeeNo+'</span></div>' +
                                            '<div><b>Name: </b><span>'+resourceList[j].name+'</span></div>' +
                                            '<div><b>On Duty: </b><span>'+resourceList[j].tracking.modelName+'|'+resourceList[j].tracking.groupName+'|'+resourceList[j].tracking.stationName+'</span></div>' +
                                            '<div><b>Success/Action: </b><span>'+resourceList[j].taskSuccess+'/'+resourceList[j].taskNumber+'</span></div>' +
                                            '<div><b>Speed: </b><span>'+resourceList[j].processingTime+'</span></div>';
                            }

                            if (resourceList.length > 0) {
                                html += '<div data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+resource+'" style="top: '+data[i].iconTop+'px; left: '+data[i].iconLeft+'px; width: '+w+'px; position: absolute; z-index: 52; text-align: center;"><img src="/assets/images/custom/icon-on-duty.png"></div>';
                            } else if (data[i].side != null) {
                                html += '<div style="top: '+(data[i].side == 'B' ? data[i].iconTop : (data[i].iconTop - 20))+'px; left: '+data[i].iconLeft+'px; width: '+w+'px; position: absolute; z-index: 52; text-align: center;">' +
                                            '<div>'+data[i].lineName+'</div>' +
                                            '<button class="btn legitRipple" name="live" style="width: 22px; height: 22px; padding: 0; margin: 0px 1px;background-color: #95aff8;border: 1px solid #000;" data-href="10.224.91.14:554/Streaming/Channels/1" onclick="openVLC(this.dataset.href);"><i class="fa fa-youtube-play"></i></button>' +
                                        '</div>';
                            }

                            html += '<div class="line" name="'+data[i].lineName+'" style="top: '+y+'px; left: '+x+'px; width: '+w+'px; height: '+h+'px; ">';
                            for (j in groupList) {
                                var groupName = groupList[j].groupName;
                                var value = '<div><b>Line: </b><span>'+groupList[j].lineName+'</span></div>' +
                                            '<div><b>Model: </b><span>'+groupList[j].modelName+'</span></div>' +
                                            '<div><b>Station: </b><span>'+groupList[j].groupName+'</span></div>' +
                                            '<div><b>Output/Plan: </b><span>'+groupList[j].wip+'/'+groupList[j].plan+'</span></div>' +
                                            '<div><b>Retest Rate: </b><span>'+groupList[j].retestRate.toFixed(2)+' %</span></div>' +
                                            '<div><b>First Pass Rate: </b><span>'+groupList[j].firstPassRate.toFixed(2)+' %</span></div>';
                                            //'<div><b>Abnormal/Total: </b><span>3/7</span></div>';

                                html += '<div class="station '+groupList[j].status+'" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" style="width: 100%; height: '+(100/count)+'%; "></div>';
                            }
                            html += '</div>';
                            onduty += resourceList.length;
                        }
                    }

                    $('#layout').append(html);
                    $('[data-popup=popover]').popover({
                        template: '<div class="popover" style="width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3;"><div class="arrow"></div><div class="popover-content"></div></div>'
                    });
                    $('label[name="te-free"]').html(free);
                    $('label[name="te-on-duty"]').html(onduty);

                    insertMachine();

                } else {

                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });

        $.ajax({
            type: "GET",
            url: "/api/test/station/status/count2",
            data: {
                factory: 'B04'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if (!$.isEmptyObject(data)) {
                    $('label[name="total-warning"]').html((data.LOCKED + data.WARNING));
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function initAutoMachine() {
        var request = {
            machineType: "global",
            content: {
                request: "yieldRate",
                countBy: "product",
                dbName: "B04_SOLDRING_VI_01",
                timeSpan: {
                    //start: moment.utc().format(),
                    start: "2019-06-10T00:30:00.000Z",
                    end: ""
                },
                combine: -1
            }
        }

        //socket.emit('machineData', request, function(data) {
        //    //console.log(data);
        //    var yieldRate = 0;
        //    if (data.seriesData.length > 0) {
        //        yieldRate = data.seriesData[0].pass * 100 / data.seriesData[0].total;
        //    }
        //
        //    var value = '<div><b>Machine: </b><span>SOLDERING VI</span></div>' +
        //                '<div><b>Yield Rate: </b><span>'+yieldRate.toFixed(2)+' %</span></div>';
        //
        //    var html = '<div class="line" style="top: 88px; left: 829px; width: 33px; height: 110px; ">' +
        //                    (data.seriesData.length > 0 ? '<div class="station '+convertStatus(yieldRate, 95, 98)+'" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" style="width: 100%; height: 100%; "></div>' : '') +
        //               '</div>';
        //    $('#layout').append(html);
        //    $('[data-popup=popover]').popover({
        //        template: '<div class="popover" style="width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3;"><div class="arrow"></div><div class="popover-content"></div></div>'
        //    });
        //});
    }

    function convertStatus(yieldRate, errorSpec, warningSpec) {
        if (yieldRate < errorSpec) {
            return 'error';
        }
        if (yieldRate < warningSpec) {
            return 'warning';
        }
        return 'normal';
    }

    function insertMachine() {
        var html = '<div class="station AOI2" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">A2</span></div>'
                 + '<div class="station AOI1" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">A1</span></div>'
                 + '<div class="station H1" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">H1</span></div>'
                 + '<div class="station H2" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">H2</span></div>'
                 + '<div class="station H3" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">H3</span></div>'
                 + '<div class="station H4" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">H4</span></div>'
                 + '<div class="station H5" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">H5</span></div>'
                 + '<div class="station G1" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">G1</span></div>'
                 + '<div class="station G2" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">G2</span></div>'
                 + '<div class="station G3" style="width: 100%; height: '+(100/10)+'%;"><span class="mc_name">G3</span></div>';
         for(i=0; i<9; i++){
             $('.line[name="C'+(i+1)+'"]').html(html);
         }
         loadAOI2();
         loadAOI1();
         loadMounter();
        setInterval(insertMachine, 300000);
    }

    function loadAOI1() {
        $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/api/aoi1/status",
            data: {
                factory: 'B04',
                //timeSpan: '2019/08/13 19:30 - 2019/08/14 07:30'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                //loadAOI2();
                if (!$.isEmptyObject(data)) {
                    var lines = Object.keys(data);
                    for (i in lines) {
                        var machine = data[lines[i]];
                        var machine_name = Object.keys(machine);
                        if(machine[machine_name].totalQty == 0 && machine[machine_name].firstPass == 0){
                            var value = '<div><b>Machine: </b><span>'+machine_name+'-SMT'+lines[i].slice(1,2)+'</span></div>' +
                                        '<div><b>Total Qty: </b><span>'+machine[machine_name].totalQty+'</span></div>' +
                                        '<div><b>Total Fail: </b><span>'+machine[machine_name].totalFail+'</span></div>' +
                                        '<div><b>First Pass: </b><span>'+machine[machine_name].firstPass+'</span></div>' +
                                        '<div><b>First Pass Rate: </b><span>'+machine[machine_name].firstPassRate.toFixed(2)+'%</span></div>';

                            var html = '<div class="" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" style="width: 100%; height: '+(100)+'%; padding-left: 1px;"><span class="mc_name">A1</span></div>';
                        }
                        else{
                            var value = '<div><b>Machine: </b><span>'+machine_name+'-SMT'+lines[i].slice(1,2)+'</span></div>' +
                                        '<div><b>Total Qty: </b><span>'+machine[machine_name].totalQty+'</span></div>' +
                                        '<div><b>Total Fail: </b><span>'+machine[machine_name].totalFail+'</span></div>' +
                                        '<div><b>First Pass: </b><span>'+machine[machine_name].firstPass+'</span></div>' +
                                        '<div><b>First Pass Rate: </b><span>'+machine[machine_name].firstPassRate.toFixed(2)+'%</span></div>';

                            var html = '<div class="'+convertStatus(machine[machine_name].firstPassRate, 25, 50)+'" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" style="width: 100%; height: '+(100)+'%; padding-left: 1px;"><span class="mc_name">A1</span></div>';
                        }
                        $('.line[name="'+lines[i]+'"] .'+machine_name+'').html(html);
                    }
                    $('[data-popup=popover]').popover({
                        template: '<div class="popover" style="width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3;"><div class="arrow"></div><div class="popover-content"></div></div>'
                    });
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadAOI2(){
        $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/api/aoi2/status",
            data: {
                factory: 'B04',
                //timeSpan: '2019/08/13 19:30 - 2019/08/14 07:30'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                //loadMounter();
                if (!$.isEmptyObject(data)) {
                    var lines = Object.keys(data);
                    for (i in lines) {
                        var machine = data[lines[i]];
                        var machine_name = Object.keys(machine);
                        if(machine[machine_name].totalQty == 0 && machine[machine_name].firstPass == 0){
                            var value = '<div><b>Machine: </b><span>'+machine_name+'-SMT'+lines[i].slice(1,2)+'</span></div>' +
                                        '<div><b>Total Qty: </b><span>'+machine[machine_name].totalQty+'</span></div>' +
                                        '<div><b>Total Fail: </b><span>'+machine[machine_name].totalFail+'</span></div>' +
                                        '<div><b>First Pass: </b><span>'+machine[machine_name].firstPass+'</span></div>' +
                                        '<div><b>First Pass Rate: </b><span>'+machine[machine_name].firstPassRate.toFixed(2)+'%</span></div>';

                            var html = '<div class="" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" style="width: 100%; height: '+(100)+'%; padding-left: 1px;"><span class="mc_name">A2</span></div>';
                        }
                        else{
                            var value = '<div><b>Machine: </b><span>'+machine_name+'-SMT'+lines[i].slice(1,2)+'</span></div>' +
                                        '<div><b>Total Qty: </b><span>'+machine[machine_name].totalQty+'</span></div>' +
                                        '<div><b>Total Fail: </b><span>'+machine[machine_name].totalFail+'</span></div>' +
                                        '<div><b>First Pass: </b><span>'+machine[machine_name].firstPass+'</span></div>' +
                                        '<div><b>First Pass Rate: </b><span>'+machine[machine_name].firstPassRate.toFixed(2)+'%</span></div>';

                            var html = '<div class="'+convertStatus(machine[machine_name].firstPassRate, 25, 50)+'" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" style="width: 100%; height: '+(100)+'%; padding-left: 1px;"><span class="mc_name">A2</span></div>';
                        }
                        $('.line[name="'+lines[i]+'"] .'+machine_name+'').html(html);
                    }
                    $('[data-popup=popover]').popover({
                        template: '<div class="popover" style="width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3;"><div class="arrow"></div><div class="popover-content"></div></div>'
                    });
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });

    }


   //============= Direct Update Line C2 =================================//

   function loadMounter() {
       $.ajax({
           type: "GET",
           url: "http://10.224.81.70:8888/aoi-analytics/api/mounter/status",
           data: {
               factory: 'B04',
               //timeSpan: '2019/08/13 19:30 - 2019/08/14 07:30'
           },
           contentType: "application/json; charset=utf-8",
           success: function(data){
               if (!$.isEmptyObject(data)) {
                   for (i in data) {
                       var line_name = data[i].lineName;
                       var machine_name = data[i].machineName.slice(6,8);

                       var value = '<div><b>Machine: </b><span>'+data[i].machineName+'</span></div>' +
                                   '<div><b>Mounting Time: </b><span>'+data[i].mountingTime.toFixed(2)+'s</span></div>' +
                                   '<div><b>Halt Time: </b><span>'+data[i].haltTime.toFixed(2)+'s</span></div>' +
                                   '<div><b>Cycle Time: </b><span>'+data[i].avgCycle.toFixed(2)+'s</span></div>' +
                                   '<div><b>Output: </b><span>'+data[i].output+'</span></div>' +
                                   '<div><b>Pickup Rate: </b><span>'+data[i].pickupRate.toFixed(2)+'%</span></div>' +
                                   '<div><b>Error Qty: </b><span>'+data[i].errorNum+'</span></div>' +
                                   '<div><b>Top 3 Error: </b>';
                       var top3 = data[i].top3Error
                       for(j in top3){
                           value += '<span>'+j+': '+top3[j]+'; </span>';
                       }
                            value += '</div>';

                       var html = '<div class="'+convertStatus(data[i].pickupRate, 96, 99)+'" data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+value+'" data-container="body" style="width: 100%; height: '+(100)+'%; padding-left: 1px;"><span class="mc_name">'+machine_name+'</span></div>';
                       $('.line[name="'+line_name+'"] .'+machine_name+'').html(html);
                   }
                   $('[data-popup=popover]').popover({
                       template: '<div class="popover" style="min-width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3;"><div class="arrow"></div><div class="popover-content"></div></div>'
                   });
               }
           },
           failure: function(errMsg) {
               console.log(errMsg);
           }
       });
   }


   //============= /direct Update Line C2 =================================//


</script>

<style>
.line {
    position: absolute;
    background-color: #727272;
    border: 1px solid #4e7af3;
}

.station {
    position: relative;
    background-color: #727272;
    border: 1px solid #fff;
}
.station:hover{
    border: 1px solid #4e7af3;
    /*transform: scale(1.1);*/
    /*-webkit-filter: brightness(125%);*/
    z-index: 50;
}
.mc_name{
    font-size: 10px;
    position: absolute;
}

.error {
    /*background-color: #e6717c;*/
    background-color: #e75849;
    animation-name: aim;
    animation-duration: 1s;
    animation-direction: alternate;
    animation-iteration-count: infinite;
}
.warning {
    /*background-color: #ffda6a;*/
    background-color: #ffaa00;
    animation-name: aim;
    animation-duration: 1s;
    animation-direction: alternate;
    animation-iteration-count: infinite;
}
.normal {
    /*background-color: #acc284;*/
    background-color: #02ba6b;
}
@keyframes aim {
    from {
        -webkit-filter: brightness(75%);
    }
    to {
        -webkit-filter: brightness(125%);
    }
}
</style>