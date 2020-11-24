<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<style>
    body{
        font-size: 14px;
    }
    .loader {
        display: none;
        position: fixed;
        z-index: 1051;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) url('/assets/images/loadingg.gif') 50% 50% no-repeat;
    }
    #tblCopyICData td:nth-child(2) {color: blue;cursor: pointer;
    }
    .Tim {background: #7030a0;}
    .Cam {background: #ff6600;}
    .Xanhla {background: #00b050;}
    .Xanhlam {background: #00b0f0;}
    .Vang {background: #ffff00;}
    .Do {background: #ff0000;}
    .Bac{background: #c9c9c9;}
</style>
<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="margin-bottom: 0px; min-height: calc(100vh - 100px);">
    <div class="col-lg-12">
        <div class="row" style="margin: 0 0 5px 0; padding: 0">
            <div class="panel panel-overview" id="header" style="font-size: 16px;">
                <span class="text-uppercase">Copy IC Data</span>
            </div>
            <div class="no-padding" style="margin: 0 0 5px 0;">
                <div class="col-md-12 col-xs-12 no-padding">
                    <div class="panel input-group" style="margin-bottom: 5px;">
                        <span class="input-group-addon" style="padding: 0px 4px;"><i class="icon-calendar22"></i></span>
                        <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                            style="border-bottom: 0px !important; height: 27px">
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin-bottom: 5px">
            <div class="col-sm-12">
                <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                    <table id="tblCopyICData" class="table table-small table-bordered table-sticky table-hover"
                        style="text-align: center">
                        <thead>
                            <tr>
                                <th>No</th>
                                <th>MODEL</th>
                                <th>PLAN</th>
                                <th>Machine number</th>
                                <th>PASS_QTY</th>
                                <th>FAIL_QTY</th>
                                <th>TOTAL_QTY</th>
                                <th>PASS/TOTAL (%)</th>
                                <!-- <th>REMARK</th> -->
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- MODAL Detail-->
<div class="modal fade" id="modalModel" role="dialog">
    <div class="loader"></div>
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <span id="spModelName"></span>
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross"></i></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                    <table id="tblModel" class="table table-small table-bordered table-sticky"
                        style="margin-bottom: 10px;">
                        <thead>
                            <tr>
                                <th>MODEL</th>
                                <th>MACHINE</th>
                                <th>PART NUMBER</th>
                                <th>CHECKSUM</th>
                                <th>COLOR</th>
                                <th>BIN FILE</th>
                                <th>PASS_QTY</th>
                                <th>FAIL_QTY</th>
                                <th>TOTAL_QTY</th>
                                <th>PASS/TOTAL (%)</th>
                                <!-- <th>REMARK</th> -->
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    var dataset = {
        factory: '${factory}'
    };

    function loadData() {
        $('.loader').css('display', 'block');
        $.ajax({
            type: "GET",
            url: "/api/test/copyic-data/all",
            data: {
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('#tblCopyICData>tbody').html('');
                if (data) {
                    var html = '';
                    var passTotal=0;
                    for (i in data) {
                        passTotal=(Number(data[i].passQty) / Number(data[i].totalQty) * 100).toFixed(2);
                        if(passTotal=='NaN'){
                            passTotal=0;
                        }
                        html += '<tr><td>' + (Number(i) + 1) + '</td>'
                            + '<td onclick="loadModel(\'' + data[i].model + '\')">' + data[i].model + '</td>'
                            + '<td>' + toStringg(data[i].plan,'') + '</td>'
                            + '<td>' + data[i].machineNumber + '</td>'
                            + '<td>' + toStringg(data[i].passQty,0)+ '</td>'
                            + '<td>' + toStringg(data[i].failQty,0) + '</td>'
                            + '<td>' + toStringg(data[i].totalQty,0) + '</td>'
                            + '<td>' + passTotal + '</td>'
                        //  +  '<td>'+data[i].failAvgCycle.toFixed(2)+'</td>'+
                        '</tr>';
                    }
                    $('#tblCopyICData>tbody').html(html);
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').css('display', 'none');
                dataset.timeout = setTimeout(loadData, 300000);
            }
        });
    }

    function loadModel(modelName) {
        $('#spModelName').html('<b>' + modelName + '</b>');
        $('#modalModel').modal('show');
        $('.loader').css('display', 'block');
        $.ajax({
            type: "GET",
            url: "/api/test/copyic-data/",
            data: {
                timeSpan: dataset.timeSpan,
                modelName: modelName,
            },
            // data: {
            //     timeSpan: '2020/03/01 07:30 - 2020/04/10 19:30',
            //     modelName: 'U10C100T30',
            // },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('#tblModel>tbody').html('');
                if (data) {
                    var html = '';
                    var passTotal;
                    for (i in data) {
                        passTotal = (Number(data[i].passQty) / Number(data[i].totalQty) * 100).toFixed(2);
                        if(passTotal=='NaN'){
                            passTotal=0;
                        }
                        html = '<tr><td class="tdModel">' + data[i].model + '</td>'
                            + '<td>' + data[i].machine + '</td>'
                            + '<td>' + data[i].partNumber + '</td>'
                            + '<td>' + data[i].checksum + '</td>'
                            + '<td class="color' + i + '">' + data[i].icColor + '</td>'
                            + '<td>' + toStringg(data[i].binFile,'') + '</td>'
                            + '<td>' + toStringg(data[i].passQty,0)+ '</td>'
                            + '<td>' + toStringg(data[i].failQty,0) + '</td>'
                            + '<td>' + toStringg(data[i].totalQty,0) + '</td>'
                            + '<td>' + passTotal + '</td>' +
                            '</tr>';
                        $('#tblModel>tbody').append(html);
                        if(data[i].icColor=='Do'){
                            $('.color' + i).addClass('Do')
                        }
                        else if(data[i].icColor=='Cam'){
                            $('.color' + i).addClass('Cam')
                        }
                        else if(data[i].icColor=='Vang'){
                            $('.color' + i).addClass('Vang')
                        }
                        else if(data[i].icColor=='Tim'){
                            $('.color' + i).addClass('Tim')
                        }
                        else if(data[i].icColor=='Xanh la'){
                            $('.color' + i).addClass('Xanhla')
                        }
                        else if(data[i].icColor=='Xanh lam'){
                            $('.color' + i).addClass('Xanhlam')
                        }
                        else if(data[i].icColor=='Bac'){
                            $('.color' + i).addClass('Bac')
                        }
                        
                    }

                    for (i in data) {
                        var classRow = $('.tdModel');
                        if (classRow.length > 1) {
                            for (let j = 0; j < classRow.length; j++) {
                                classRow[0].rowSpan = classRow.length;
                                if (j > 0) {
                                    classRow[j].className += ' hidden';
                                }
                            }
                        }
                    }

                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').css('display', 'none');
            }
        });
    }



    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
                $('input[name=timeSpan]').on('change', function () {
                    dataset.timeSpan = this.value;
                    loadData();
                });

                dataset.timeSpan = startDate + ' - ' + endDate;
                loadData();

                delete current;
                delete startDate;
                delete endDate;
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    getTimeNow();
</script>