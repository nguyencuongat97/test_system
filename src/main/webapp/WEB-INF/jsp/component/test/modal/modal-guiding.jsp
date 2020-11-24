<div id="modal-guiding" class="modal fade modal-gradient">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-title">
                <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                    <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                    <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">GUIDING</h6>
                </div>
            </div>
            <div class="modal-body" style="padding-top: 0px;">
                <ul class="guiding-steps" style="padding: 0px 10px;">
                    <li class="media-list" >
                        <div class="step-title bg-primary" style="padding: 10px 14px;margin: 10px 0px;">
                            <span class="text-bold">STEP 1:</span><span>Check text fixture at HDMI connector</span>
                        </div>
                        <div class="step-image"><img src="" alt="" style="width: 100%;"></div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<style>
ul li.media-list::before {
    content: "";
    display: block;
    height: 1px;
    width: 100%;
    margin: 15px 0px;
    background: #bbb;
}
</style>

<script>
    function loadImageGuiding() {
        var html = '';
        var steps = dataset.solution.steps;
        for (i in steps) {
            html += '<li class="media-list" >' +
                         '<div class="step-title" style="padding: 10px 14px; margin: 10px 0px; background-color: #dddddd;">' +
                             '<span class="text-bold">STEP ' + (Number(i) + 1) + ': </span><span>' + steps[i].text + '</span>' +
                         '</div>' +
                         '<div class="step-image"><img src="' + steps[i].image + '" alt="" style="width: 100%;"></div>' +
                     '</li>'
        }
        $('.guiding-steps').html(html);
    }
</script>