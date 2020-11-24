<script>

var dataset = {
    domain: '${domain}',
    trackingId: '${trackingId}',
}

if (cm.useCivet ==  true) {
    cm.cleanCache();

    cm.getCurrentUser({
        success : function (res) {
            dataset.employee = res.value.emp_no;
            $.ajax({
                type: "POST",
                url: "/api/test/tracking/handle",
                data: jQuery.param({
                    trackingId: dataset.trackingId,
                    employee: dataset.employee
                }),
                success: function(data){
                    if (data == "success") {
                        alert("You are assigned this task!");
                    }
                    var href = dataset.domain + '/icivet/task?' + 'trackingId=' + dataset.trackingId
                    hrefWindow(href);
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                    alert("You can't take this task!");
                    cm.closeWindow();
                }
            });
        }
    });
} else {
    console.log( "Not a sweet environment." );
}

</script>