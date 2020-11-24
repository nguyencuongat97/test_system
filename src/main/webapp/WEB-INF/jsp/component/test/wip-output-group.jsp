<link rel="stylesheet" href="/assets/custom/css/nbb/nbb-mo-history.css" />
<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/heatmap.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/accessibility.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<div class="loader hidden"></div>
<div class="panel panel-re panel-flat row"  style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header" style="font-size: 23px;">
            <b><span id="titlePage"></span><span> WIP OUTPUT GROUP </span><span id="txtDateTime"></span></b>
        </div>
        <div class="row no-margin no-padding">
            <div class="col-xs-6 col-md-1 no-padding">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control daterange-single" side="right" name="timeSpan" style="border-bottom: 0px !important; color: #fff;">
                </div>
            </div>

            <div class="col-xs-6 col-md-2 btn-group" style="margin-bottom: 5px;">
                <button id="a-shift" class="btn btn-default select-shift selected" onclick="loadDataTypeShift('')">All</button>
                <button id="d-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('DAY')">Day</button>
                <button id="n-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('NIGHT')">Night</button>
            </div>
            <div class='col-md-1 col-xs-3 selectCustomer' style="margin-bottom: 5px;">
                <div class="dropdown dropdown-select-customer">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Select Customer
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer"></ul>
                </div>
            </div>
            <div class='col-md-1 col-xs-3 selectStage' style="margin-bottom: 5px;">
                <div class="dropdown dropdown-select-stage">
                    <button class="btn dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                        Stage
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage">

                    </ul>
                </div>
            </div>

            <div class='col-md-2 col-xs-5 selectModel' style="margin-bottom: 5px;">
                <select class="form-control bootstrap-select" name="modelName" data-live-search="true" data-focus-off="true"></select>
            </div>
        </div>

        <div class="row no-margin">
            <div id="container1" style="width:100%; height: calc(100vh - 220px);"></div>
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
                var list = '';
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

                    var selector = $("select[name='modelName']");
                    selector.children('option').remove();
                    var options = "<option value=''>Select All</option>";
                    for(i in data){
                        options+='<option value=' + data[i] + '>' + data[i] + '</option>';
                    }
                    selector.append(options);
                    selector.val(dataset.modelName);
                    selector.selectpicker('refresh');
                } else{
                    var selector = $("select[name='modelName']");
                    selector.children('option').remove();
                    var options = "<option value=''>Select All</option>";
                    selector.append(options);
                    selector.val(dataset.modelName);
                    selector.selectpicker('refresh');
                    loadDataTypeShift(dataset.shiftType);
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

     $('.dropdown-select-customer').on( 'click', '.select-customer li a', function() { 
        dataset.customer = $(this).html();
        $("#titlePage").html(dataset.customer.toUpperCase());
        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        dataset.stage = "";
        dataset.modelName = "";

        $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        loadSelectStage();
        loadData();
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

        $('.dropdown-select-model').find('.dropdown-toggle').html('Select Model <span class="caret"></span>');
        loadSelectModel();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        loadData();
    });

    $('select[name=modelName]').on('change', function() {
        dataset.modelName = this.value;
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        loadData();
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
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        var getD = moment(dataset.workDate).format("MM/DD");
        $("#txtDateTime").html(getD + " ("+dataset.shiftType+")");
    }
    function loadData(){
        $.ajax({
            type: "GET",
            url: "/api/test/wip-output-group",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $(".loader").removeClass("hidden");
                $('#container1').html('');
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
                        // label.onclick = function() {
                        //     loadGroupHistory(this.textContent);
                        // }
                    });
                }
            }
        },

        title: {
            text: '',
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
                            // loadDetail(wipGroupClick);
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
        $('input[name=timeSpan]').on('change', function() {
            dataset.workDate = this.value;
            window.localStorage.setItem('dataset', JSON.stringify(dataset));
            init();
            $('#container1').html('');
            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " (" + dataset.shiftType + ")");
        });
    });
</script>