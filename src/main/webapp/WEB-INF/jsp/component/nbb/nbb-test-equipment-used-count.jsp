<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />

<script type="text/javascript" src="/assets/js/hr/dataTables.min.js"></script>

<div class="loader"></div>

<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">

        <div class="panel panel-overview" id="header">
            <b><span id="titlePage"></span><span> Test Equipment Overview </span><span id="txtDateTime"></span></b>
        </div>

        <div class="row" style="margin: unset; padding: unset">

            <div class='drlMenu selectCustomer'>
                <div class="dropdown dropdown-select-customer">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Select Customer
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer">

                    </ul>
                </div>
            </div>

            <div class='drlMenu selectStage'>
                <div class="dropdown dropdown-select-stage">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                        Select Stage
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage">

                    </ul>
                </div>
            </div>

        </div>

        <div class="row" style="margin: unset;">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);color: #ccc; padding: unset;">
                <table id="tblDetail" class="table table-xxs table-bordered table-sticky" style="text-align: center">
                </table>
            </div>
        </div>

    </div>
</div>

<script>
    var dataset = {
        factory: '${factory}',
        customer: 'MISTRAL',
        stage: '',
        group: '',
        line: '',
        station: ''
    };
    var firstLoad = true;

    init();

    function init() {
        loadSelectCustomer();
        loadData();
    }

    function loadData() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/equipment/used/count",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                stage: dataset.stage,
                group: dataset.group,
                line: dataset.line,
                station: dataset.station
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#tblDetail").html("");

                var tdata = '<thead><tr>';
                tdata += '<th>stage</th>';
                tdata += '<th>group</th>';
                tdata += '<th>line</th>';
                tdata += '<th>station</th>';
                tdata += '<th>equipment</th>';
                tdata += '<th>used times</th>';
                tdata += '<th>elapsed times</th>';
                tdata += '</tr></thead>';

                tdata += '<tbody>';

                for (var stage in data["data"]) {
                    var dataStage = data["data"][stage];
                    for (var group in dataStage) {
                        var dataGroup = dataStage[group];
                        for (var line in dataGroup) {
                            var dataLine = dataGroup[line];
                            for (var station in dataLine) {
                                var dataStation = dataLine[station];
                                for (var equipmentIndex in dataStation) {
                                    tdata += '<tr><td>' + stage + '</td><td>' + group + '</td><td>' + line + '</td><td>' + station + '</td><td>' + dataStation[equipmentIndex]["equipment"] + '</td><td>' + dataStation[equipmentIndex]["numberUsed"] + '</td><td>' + dataStation[equipmentIndex]["elapsedTime"] + '</td></tr>';
                                }
                            }
                        }
                    }
                }

                tdata += '</tbody>';

                if (firstLoad == false) {
                    $('#tblDetail').DataTable().destroy();
                } else {
                    firstLoad = false;
                }
                $("#tblDetail").html(tdata);
                $('#tblDetail').DataTable();

                $(".loader").addClass("disableSelect");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                // dataset.timeout = setTimeout(loadData, 30000, dataset);
            }
        });
    }

    function loadSelectCustomer(allFlag) {
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/customer",
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var list = '';
                if (allFlag != undefined && allFlag) {
                    list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">All</a></li>';
                }
                for (i in data) {
                    list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">' + data[i] + '</a></li>';
                }
                $(".select-customer").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
        $("#titlePage").html(dataset.customer.toUpperCase());
    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        dataset.customer = $(this).html();

        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');

        dataset.stage = "";
        $('.dropdown-select-stage').find('.dropdown-toggle').html('Select Stage <span class="caret"></span>');
        loadSelectStage();

        $('.selectLine').removeClass("disableSelect");
        $('.dropdown-select').find('.dropdown-toggle').html('Select Line <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");

        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    function loadSelectStage() {
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/stage",
            data: {
                customer: dataset.customer
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
                for (i in data) {
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">' + data[i] + '</a></li>';
                }
                $(".select-stage").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

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

        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
</script>