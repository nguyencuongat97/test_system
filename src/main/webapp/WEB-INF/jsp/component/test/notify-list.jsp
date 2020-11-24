<!-- NOTIFY LIST -->

<link rel="stylesheet" href="/assets/css/custom/notify-list.css">

<div class="panel-scroll">
    <ul class="media-list" id="notify-error-content" >
        <li class="panel panel-flat bg-danger-600" style="margin-bottom: 5px;">
            <div class="category-title notify-title">
                <span><i class="fa fa-lock"></i><span style="font-size: 11px; font-weight: bold;"> LOCK (<span id="error-title"></span>)</span></span>
                <ul class="icons-list">
                    <li><a data-status="hidden" data-tag="notify-error" onclick = "showMore(this)"><i class="icon-arrow-right13"></i></a></li>
                </ul>
            </div>
        </li>
        <li class="panel border-left-lg border-left-danger notify notify-error" data-id="7441" data-factory="B04" data-model-name="P02X001.00" data-group-name="PT" data-station-name="L12B04-PT001" onclick="notifyOnclick(this)">
        	<div class="notify-title">L12B04-PT001<div class="notify-annotation">2019-01-10 09:40:08</div></div>
        	<div class="notify-body"><span class="text-bold">Detail:</span><span> WIP(51) &gt; 50 and RETEST RATE(5.45%) &gt; 3% and F.P.R(96%) &lt; 97%</span></div>
        	<div class="notify-body text-primary"><span class="text-bold">Suggest:</span><span> FR0031 - Check HDMI cabble -> Change if it is broken</span></div>
        	<div class="notify-btn">
        		<button class="btn btn-default style-classic btn-hidden legitRipple" data-tab="#basic-tab2" onclick="selectTab(this)">DETAIL</button>
        		<button class="btn btn-default style-classic btn-hidden legitRipple" data-tab="#basic-tab3" onclick="selectTab(this)">GUIDING</button>
        	</div>
        </li>
        <li class="panel border-left-lg border-left-danger notify notify-error hidden" data-id="7441" data-factory="B04" data-model-name="40-5024" data-group-name="FT1" data-station-name="40-5024-S06" onclick="notifyOnclick(this)">
            <div class="notify-title">40-5024-S06<div class="notify-annotation">2019-01-10 08:23:01</div></div>
            <div class="notify-body">Model 40-5024 at station 40-5024-S06 has top 3 error LG450</div>
            <div class="notify-btn">
                <button class="btn btn-default style-classic btn-hidden legitRipple" data-tab="#basic-tab2" onclick="selectTab(this)">DETAIL</button>
                <button class="btn btn-default style-classic btn-hidden legitRipple" data-tab="#basic-tab3" onclick="selectTab(this)">GUIDING</button>
            </div>
        </li>
    </ul>
    <ul class="media-list" id="notify-warning-content" >
        <li class="panel panel-flat bg-warning-600" style="margin-bottom: 5px;">
            <div class="category-title notify-title">
                <span><i class="fa fa-warning"></i><span style="font-size: 11px; font-weight: bold;"> WARNING (<span id="waring-title"></span>)</span></span>
                <ul class="icons-list">
                    <li><a data-status="hidden" data-tag="notify-warning" onclick = "showMore(this)"><i class="icon-arrow-right13"></i></a></li>
                </ul>
            </div>
        </li>
    </ul>
</div>

<script src="/assets/js/custom/notify-list.js"></script>
<script>
    // init
    loadNotifyList('${factory}');
    loadStationLocked('${factory}');
</script>

<!-- /NOTIFY LIST -->