<!-- Modal Detail -->
<div class="panel-detail" >
    <div class="panel-content panel">
        <div class="panel-heading" style="padding: 8px 15px;">
            <h5 class="text-bold" name="station-name" style="margin-top: 5px; margin-bottom: 5px;">Station Name</h5>
            <button type="button" class="close" style="position: absolute;right: 10px;top: 10px;z-index: 1;" onclick="hiddenPanelDetail();hiddenPanelRight();"><i class="icon-cross"></i></button>
        </div>
        <div class="panel-body"  style="padding: 0px 15px 15px 15px;" >
            <div style="text-align: center; margin: 10px 0px;">
                <div class="btn-group btn-group-justified" name="btn-group-error-code" data-toggle="buttons">
                </div>
            </div>
            <div style="margin: 10px 0px;">
                <div class="row">
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-8">Retest Rate</label>
                            <label class="col-xs-4 text-bold" name="retest-rate"></label>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-8">Input</label>
                            <label class="col-xs-4 text-bold" name="wip"></label>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-8">First Pass Rate</label>
                            <label class="col-xs-4 text-bold" name="first-pass-rate"></label>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-8">First Fail</label>
                            <label class="col-xs-4 text-bold" name="first-fail"></label>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-8">Hit Rate</label>
                            <label class="col-xs-4 text-bold" name="hit-rate"></label>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-8">Second Fail</label>
                            <label class="col-xs-4 text-bold" name="second-fail"></label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modalDetail"></div>
        </div>
    </div>
</div>
<script>
    function showPanelDetail() {
        $('.panel-detail').addClass("show");
    }
    function hiddenPanelDetail() {
        $('.panel-detail').removeClass("show");
    }
</script>
<!-- /Modal Detail-->