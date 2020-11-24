<!-- CPK -->
<div class="loader"></div>
<div class="row" style="padding: 10px; background-color: #fff;">
    <div class="input-group" style="margin: 10px 0px 10px 0px;">
        <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
        <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
    </div>

    <div class="panel panel-overview" style="padding: 5px 15px; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <div class="row">
            <div class="col-lg-2">
                <select class="form-control bootstrap-select" name="modelName" >
                </select>
            </div>
            <div class="col-lg-2">
                <select class="form-control bootstrap-select" name="groupName" >
                </select>
            </div>
            <div class="col-lg-2">
                <select class="form-control bootstrap-select" name="stationName" >
                </select>
            </div>
            <div class="col-lg-6">
                <select class="form-control bootstrap-select" name="parameter" >
                </select>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-6">
            <div class="panel panel-flat panel-body" id="chart-cpk-content" style="height: 350px; padding: 5px;"></div>
        </div>
        <div class="col-lg-6">
            <div class="panel panel-flat panel-body" id="chart-spc-content" style="height: 350px; padding: 5px"></div>
        </div>
    </div>
</div>
<style>
    .loader{
        display: none;
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
<script src="/assets/js/customerjs/customer-common.js"></script>
<script src="/assets/js/customerjs/customer-cpk.js"></script>
<script>

    var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
        groupName: '${groupName}',
        stationName: '${stationName}',
        parameter: true
    };

    if (dataset.modelName != '') {
        loadModels(dataset, true, function(dataset) {
            loadGroups(dataset, true, function(dataset) {
                loadStations(dataset, true, function(dataset) {
                    loadParameters(dataset, true, loadCpkAndSpcChart);
                }, true);
            });
        });
    } else {
        loadModels(dataset, false, function(dataset) {
            loadGroups(dataset, false, function(dataset) {
                loadStations(dataset, false, function(dataset) {
                    loadParameters(dataset, false, loadCpkAndSpcChart);
                }, true);
            });
        });
    }

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    $(document).ready(function() {
      
        getTimeNow();

        $('select[name=modelName]').on('change', function() {
            dataset.modelName = this.value;
            loadGroups(dataset, false, function(dataset) {
                loadStations(dataset, false, function(dataset) {
                    loadParameters(dataset, false, loadCpkAndSpcChart);
                }, true);
            });
        });

        $('select[name=parameter]').on('change', function() {loadCpkAndSpcChart(dataset);});

        $('select[name=groupName]').on('change', function() {
            dataset.groupName = this.value;
            loadStations(dataset, false, function(dataset) {
                loadParameters(dataset, false, loadCpkAndSpcChart);
            }, true);
        });

        $('select[name=stationName]').on('change', function() {
            dataset.stationName = this.value;
            loadCpkAndSpcChart(dataset);
        });

        $('input[name=timeSpan]').on('change', function() {
            dataset.timeSpan = this.value;
            loadModels(dataset, true, function(dataset) {
                loadGroups(dataset, true, function(dataset) {
                    loadStations(dataset, true, function(dataset) {
                        loadParameters(dataset, true, loadCpkAndSpcChart);
                    }, true);
                });
            });
        });
    

    });
    //function notifyOnclick(context) {
    //    dataset = {
    //        type: context.dataset.type,
    //        notifyId: context.dataset.id,
    //        factory: context.dataset.factory,
    //        modelName: context.dataset.modelName,
    //        groupName: context.dataset.groupName,
    //        stationName: context.dataset.stationName
    //    };
    //
    //    loadModels(dataset, true, function(dataset) {
    //        loadParameters(dataset, true, function(dataset) {
    //            loadStations(dataset, true, loadCpkAndSpcChart, true);
    //        });
    //    });
    //}

    function loadCpkAndSpcChart(dataset) {
        var selectedParameter = $("select[name='parameter']")
        var parameter = selectedParameter.val();

        var lowSpec = 0;
        var highSpec = 0;
        if (parameter != null) {
            lowSpec = Number(selectedParameter[0].selectedOptions[0].dataset.low);
            highSpec = Number(selectedParameter[0].selectedOptions[0].dataset.high);
        }

        var timeSpan = $('input[name="timeSpan"]').val();

        drawCpkChart('chart-cpk-content', {
                factory: dataset.factory,
                modelName: dataset.modelName,
                groupName: dataset.groupName,
                stationName: dataset.stationName,
                timeSpan: timeSpan,
                parameter: parameter
            });
        drawSpcChart('chart-spc-content', lowSpec, highSpec, {
                 factory: dataset.factory,
                 modelName: dataset.modelName,
                 groupName: dataset.groupName,
                 stationName: dataset.stationName,
                 timeSpan: timeSpan,
                 parameter: parameter
             });

    }
</script>
<!-- /CPK -->