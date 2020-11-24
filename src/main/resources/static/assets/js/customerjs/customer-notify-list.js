function loadNotifyList(factory) {
    $.ajax({
        type: "GET",
        url: "/api/test/warningNotification",
        data: {
            factory: factory
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $('#notify-warning-content .notify').remove();
            $("#waring-title").html(data.length);

            if (!$.isEmptyObject(data)) {
                for (i in data) {
                    var notifyItem = "<li class=\"panel border-left-lg border-left-warning notify notify-warning";
                    if (i > 0) {
                        notifyItem += " hidden";
                    }

                    notifyItem = notifyItem + "\" data-type=\"" + data[i].type + "\" data-id=\"" + data[i].id + "\" data-factory=\"" + data[i].factory + "\" data-model-name=\"" + data[i].modelName + "\" data-group-name=\"" + data[i].groupName + "\" data-station-name=\"" + data[i].stationName + "\" onclick=\"notifyOnclickHandler(this)\" >\n" +
                                 "<div class=\"notify-title\">" + data[i].stationName  + "<div class=\"notify-annotation\">" + data[i].createdAt + "</div></div>\n" +
                                 "<div class=\"notify-body\"><span class=\"text-bold\">Detail: </span><span>" + data[i].detail + "</span></div>" +
                                 "<div class=\"notify-body text-primary\"><span class=\"text-bold\">Suggest: </span><span>" + data[i].message + "</span></div>" +
                                 "<div class=\"notify-btn\">" +
                                 "<button class=\"btn btn-primary btn-hidden\" onclick=\"moveToDetailPage(this.parentElement.parentElement)\">DETAIL</button>" +
                                 "<button class=\"btn btn-primary btn-hidden\" onclick=\"moveToGuidingPage(this.parentElement.parentElement)\">GUIDING</button>" +
                                 "</div>" +
                                 "</li>";
                    $('#notify-warning-content').append(notifyItem);
                }
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
         complete: function() {
             setTimeout(loadNotifyList, 180000, factory);
         }
    });
}

function loadStationLocked(factory) {
    $.ajax({
        type: "GET",
        url: "/api/test/lockedNotification",
        data: {
            factory: factory
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $('#notify-error-content .notify').remove();
            $("#error-title").html(data.length);

            if (!$.isEmptyObject(data)) {
                for (i in data) {
                    var notifyItem = "<li class=\"panel border-left-lg border-left-danger notify notify-error";
                    if (i > 0) {
                        notifyItem += " hidden";
                    }

                    notifyItem = notifyItem + "\" data-type=\"" + data[i].type + "\" data-id=\"" + data[i].id + "\" data-factory=\"" + data[i].factory + "\" data-model-name=\"" + data[i].modelName + "\" data-group-name=\"" + data[i].groupName + "\" data-station-name=\"" + data[i].stationName + "\" onclick=\"notifyOnclickHandler(this)\" >\n" +
                                 "<div class=\"notify-title\">" + data[i].stationName  + "<div class=\"notify-annotation\">" + data[i].createdAt + "</div></div>\n" +
                                 "<div class=\"notify-body\"><span class=\"text-bold\">Detail: </span><span>" + data[i].detail + "</span></div>" +
                                 "<div class=\"notify-body text-primary\"><span class=\"text-bold\">Suggest: </span><span>" + data[i].message + "</span></div>" +
                                 "<div class=\"notify-btn\">" +
                                 "<button class=\"btn btn-primary btn-hidden\" onclick=\"moveToDetailPage(this.parentElement.parentElement)\">DETAIL</button>" +
                                 "<button class=\"btn btn-primary btn-hidden\" onclick=\"moveToGuidingPage(this.parentElement.parentElement)\">GUIDING</button>" +
                                 "</div>" +
                                 "</li>";
                    $('#notify-error-content').append(notifyItem);
                }
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
         complete: function() {
             setTimeout(loadStationLocked, 30000, factory);
         }
    });
}

function showMore(context) {
    var status = context.dataset.status;
    var errorContent = $("." + context.dataset.tag);
    if (status == "hidden") {
        for (i=0; i<errorContent.length; i++) {
            errorContent[i].classList.remove("hidden");
        }
        context.dataset.status = 'show';
    }

    if (status == "show") {
        for (i=1; i<errorContent.length; i++) {
            errorContent[i].classList.add("hidden");
        }
        context.dataset.status = 'hidden';
    }
}

function selectTab(context) {
    var tab = context.dataset.tab;
    $('a[href="' + tab + '"]').tab('show');
}

function removeNotify(id) {
    var context = $('.notify[data-id=' + id + ']');
    if (context[0].classList.contains("notify-warning")) {
        var warning = Number($('#warning-title').html())
        $('#warning-title').html(warning - 1);
    }
    if (context[0].classList.contains("notify-error")) {
        var error = Number($('#error-title').html())
        $('#error-title').html(error - 1);
    }
    context.remove();
}

function moveToDetailPage(context) {
    window.open("http://10.224.81.70:8888/customer/customer-detail" + "?factory=" + context.dataset.factory + "&modelName=" + context.dataset.modelName + "&groupName=" + context.dataset.groupName + "&stationName=" + context.dataset.stationName + "&timeSpan=" + dataset.timeSpan);
}

function moveToGuidingPage(context) {
    window.open("http://10.224.81.70:8888/troubleshooting-guide" + "?factory=" + context.dataset.factory + "&modelName=" + context.dataset.modelName + "&groupName=" + context.dataset.groupName + "&stationName=" + context.dataset.stationName);
}

function notifyOnclickHandler(context) {
    window.open(getUrl(context.dataset.type, context.dataset.factory, context.dataset.modelName, context.dataset.groupName, context.dataset.stationName));
}