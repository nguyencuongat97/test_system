<link rel="stylesheet" href="/assets/css/custom/notify-list.css">
<link rel="stylesheet" href="/assets/css/custom/style.css">

<div class="col-lg-4 col-sm-12"></div>
<div class="col-lg-4 col-sm-12">
    <div class="row" style="background-color: #fff;">
        <div class="col-sm-12" style="padding: 10px; text-align: center;">
            <h3 class="text-bold" style="margin: unset;">MY TASKS</h3>
        </div>
        <div class="col-sm-12">
            <div class="input-group" style="margin: 5px 0px;">
                <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="color: inherit;">
            </div>
        </div>
        <div class="col-sm-12" style="padding: 10px">
            <div class="btn-group btn-group-justified" name="btn-group-status" data-toggle="buttons">
                <label class="btn bg-yellow active" data-status="on-going">
                    <input type="radio" name="on-going" value="ON_GOING">ON GOING<div name="count-on-going">0</div>
                </label>
                <label class="btn bg-green" data-status="success">
                    <input type="radio" name="success" value="SUCCESS">SUCCESS<div name="count-success">0</div>
                </label>
                <label class="btn bg-red" data-status="failed">
                    <input type="radio" name="failed" value="FAILED" data-error-desc="">FAILED<div name="count-failed">0</div>
                </label>
                <label class="btn bg-gray" data-status="timeout">
                    <input type="radio" name="timeout" value="TIMEOUT" data-error-desc="">TIMEOUT<div name="count-timeout">0</div>
                </label>
            </div>
        </div>
        <div class="col-sm-12 panel-scroll">
            <ul class="media-list" id="tasks" >
                <!--
                <li class="notify border-left-lg border-left-warning notify-warning" data-id="7441" data-factory="B04" data-model-name="40-1563-0003" data-group-name="FT1" data-station-name="0881-03" onclick="notifyOnclick(this)" style="height: 100px">
                    <div class="notify-title">L12B04-PT001<div class="notify-annotation">2019-01-10 09:40:08</div></div>
                    <div class="notify-body"><span>RETEST RATE(5.45%) &gt; 3% and F.P.R(96%) &lt; 97%</span></div>
                    <div class="notify-btn">ASSIGNED<div class="notify-annotation">2019-01-10 09:40:08</div></div>
                </li>
                -->
            </ul>
        </div>
    </div>
</div>

<script>

var dataset = {
    domain: '${domain}',
    factory: '${factory}',
    employee: 'V0946495'
}

$('.btn-group[name="btn-group-status"]>label').on('click', function() {
    showTaskByStatus(this.dataset.status);
});

function init() {
    $.ajax({
        type: "GET",
        url: "/api/test/tracking/employee",
        data: {
            factory: dataset.factory,
            employee: dataset.employee,
            timeSpan: dataset.timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if (!$.isEmptyObject(data)) {
                var html = '';
                var countOnGoing = 0;
                var countSuccess = 0;
                var countFailed = 0;
                var countTimeout = 0;
                for (i in data) {
                    html += '<li class="notify border-left-lg '+getStatus(data[i].status)+'" data-id="'+data[i].id+'" data-factory="'+data[i].factory+'" data-model-name="'+data[i].modelName+'" data-group-name="'+data[i].groupName+'" data-station-name="'+data[i].stationName+'" onclick="notifyOnclick(this)" style="height: 100px">' +
                                 '<div class="notify-title"><div class="notify-header">'+data[i].stationName+'</div><div>'+data[i].type+'</div><div class="notify-annotation">'+data[i].createdAt+'</div></div>' +
                                 '<div class="notify-body"><span>'+data[i].message+'</span></div>' +
                                 '<div class="notify-top-right">'+
                                     (data[i].status == 'NOTIFIED' ? '<div class="notify-header">-</div><div>'+data[i].status+'</div>'+'<div class="notify-annotation">'+data[i].notifiedAt+'</div></div>' : '') +
                                     (data[i].status == 'TIMEOUT' ? '<div class="notify-header">-</div><div>'+data[i].status+'</div>'+'<div class="notify-annotation">'+data[i].notifiedAt+'</div></div>' : '') +
                                     (data[i].status == 'ASSIGNED' ? '<div class="notify-header">'+toString(data[i].employee)+'</div>'+'<div>'+data[i].status+'</div>'+'<div class="notify-annotation">'+data[i].assignedAt+'</div></div>' : '') +
                                     (data[i].status == 'ARRIVED' ? '<div class="notify-header">'+toString(data[i].employee)+'</div>'+'<div>'+data[i].status+'</div>'+'<div class="notify-annotation">'+data[i].arrivedAt+'</div></div>' : '') +
                                     (data[i].status == 'CONFIRMED' ? '<div class="notify-header">'+toString(data[i].employee)+'</div>'+'<div>'+data[i].status+'</div>'+'<div class="notify-annotation">'+data[i].confirmedAt+'</div></div>' : '') +
                                     (data[i].status == 'UNLOCKED' ? '<div class="notify-header">'+toString(data[i].employee)+'</div>'+'<div>'+data[i].status+'</div>'+'<div class="notify-annotation">'+data[i].unlockedAt+'</div></div>' : '') +
                                     ((data[i].status == 'CLOSED' || data[i].status == 'REOPEN' || data[i].status == 'OUTDATED' || data[i].status == 'GIVE_UP') ? '<div class="notify-header">'+toString(data[i].employee)+'</div>'+'<div>'+data[i].status+'</div>'+'<div class="notify-annotation">'+data[i].closedAt+'</div></div>' : '') +
                                 '</div>' +
                             '</li>';

                    if (getStatus(data[i].status) == 'on-going')
                        countOnGoing ++;
                    else if (getStatus(data[i].status) == 'success')
                        countSuccess ++;
                    else if (getStatus(data[i].status) == 'failed')
                        countFailed ++;
                    else
                        countTimeout ++;
                }
                $('#tasks').html(html);
                $('div[name="count-on-going"]').html(countOnGoing);
                $('div[name="count-success"]').html(countSuccess);
                $('div[name="count-failed"]').html(countFailed);
                $('div[name="count-timeout"]').html(countTimeout);
            }
            showTaskByStatus('on-going');
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}

function getStatus(status) {
    if (status == 'ASSIGNED' || status == 'ARRIVED' || status == 'UNLOCKED' || status == 'CONFIRMED')
        return 'on-going';
    if (status == 'CLOSED')
        return 'success';
    if (status == 'REOPEN')
        return 'failed';
    if (status == 'GIVE_UP' || status == 'OUTDATED')
        return 'timeout';
}

function showTaskByStatus(status) {
    $('#tasks>li').addClass('hidden');
    $('#tasks>li.'+status).removeClass('hidden');
}

function notifyOnclick(context) {
    dataset.trackingId = context.dataset.id;
    dataset.factory = context.dataset.factory;
    dataset.modelName = context.dataset.modelName;
    dataset.groupName = context.dataset.groupName;
    dataset.stationName = context.dataset.stationName;

    var href = dataset.domain + '/icivet/task?' + 'trackingId=' + dataset.trackingId;
    hrefWindow(href);
}

function getTimeNow(){
    $.ajax({
        type: "GET",
        url: "/api/time/now",
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var current = new Date(data);
            var endDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
            var startDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
            $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
            $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));

            $('input[name=timeSpan]').on('change', function () {
                dataset.timeSpan = this.value;
                init();
            });

            dataset.timeSpan = startDate + ' - ' + endDate;

            if (cm.useCivet ==  true) {
                cm.cleanCache();

                cm.getCurrentUser({
                    success : function (res) {
                        dataset.employee = res.value.emp_no;
                        dataset.employeeName = res.value.emp_name;
                        init();
                    }
                });
            } else {
                console.log( "Not a sweet environment." );
                init();
            }


            delete current;
            delete startDate;
            delete endDate;
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}

$(document).ready(function() {
    getTimeNow();
});

</script>