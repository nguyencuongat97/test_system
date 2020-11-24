<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />

<!-- <div class="loader"></div> -->
<div class="panel panel-re panel-flat row"  style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
         <div class="panel panel-overview" id="header">
            <b><span id="titlePage" class="disableSelect"></span><span> MO MATERIAL FLOW </span><span id="txtDateTime" class="disableSelect"></span></b>
        </div>

        <div class="row" style="margin: unset;">
            
            <div class="container">
                <div class="row">
                    <section>
                    <div class="wizard">
                        <div class="wizard-inner">
                            <div class="connecting-line"></div>
                            <ul class="nav nav-tabs" role="tablist">

                                <li role="presentation" class="active">
                                    <a href="#step1" data-toggle="tab" aria-controls="step1" role="tab" title="Step 1">
                                        <span class="round-tab">
                                            <!-- <i class="glyphicon glyphicon-folder-open"></i> --> 1
                                        </span>
                                    </a>
                                </li>

                                <li role="presentation" class="disabled">
                                    <a href="#step2" data-toggle="tab" aria-controls="step2" role="tab" title="Step 2">
                                        <span class="round-tab">
                                            <!-- <i class="glyphicon glyphicon-pencil"></i> --> 2
                                        </span>
                                    </a>
                                </li>
                                <li role="presentation" class="disabled">
                                    <a href="#step3" data-toggle="tab" aria-controls="step3" role="tab" title="Step 3">
                                        <span class="round-tab">
                                            <!-- <i class="glyphicon glyphicon-picture"></i> --> 3
                                        </span>
                                    </a>
                                </li>
                                <li role="presentation" class="disabled">
                                    <a href="#step4" data-toggle="tab" aria-controls="step4" role="tab" title="Step 4">
                                        <span class="round-tab">
                                            <!-- <i class="glyphicon glyphicon-picture"></i> --> 4
                                        </span>
                                    </a>
                                </li>

                                <li role="presentation" class="disabled">
                                    <a href="#complete" data-toggle="tab" aria-controls="complete" role="tab" title="Complete">
                                        <span class="round-tab">
                                            <!-- <i class="glyphicon glyphicon-ok"></i> --> Finish
                                        </span>
                                    </a>
                                </li>
                            </ul>
                        </div>

                        <form role="form">
                            <div class="tab-content">
                                <div class="tab-pane active" role="tabpanel" id="step1">
                                    <h3>Step 1</h3>
                                    <p>This is step 1</p>
                                    <ul class="list-inline pull-right">
                                        <li><button type="button" class="btn btn-primary next-step">Save and continue</button></li>
                                    </ul>
                                </div>
                                <div class="tab-pane" role="tabpanel" id="step2">
                                    <h3>Step 2</h3>
                                    <p>This is step 2</p>
                                    <ul class="list-inline pull-right">
                                        <li><button type="button" class="btn btn-default prev-step">Previous</button></li>
                                        <li><button type="button" class="btn btn-primary next-step">Save and continue</button></li>
                                    </ul>
                                </div>
                                <div class="tab-pane" role="tabpanel" id="step3">
                                    <h3>Step 3</h3>
                                    <p>This is step 3</p>
                                    <ul class="list-inline pull-right">
                                        <li><button type="button" class="btn btn-default prev-step">Previous</button></li>
                                        <li><button type="button" class="btn btn-primary next-step">Save and continue</button></li>
                                    </ul>
                                </div>
                                <div class="tab-pane" role="tabpanel" id="step4">
                                    <h3>Step 4</h3>
                                    <p>This is step 4</p>
                                    <ul class="list-inline pull-right">
                                        <li><button type="button" class="btn btn-default prev-step">Previous</button></li>
                                        <li><button type="button" class="btn btn-default next-step">Skip</button></li>
                                        <li><button type="button" class="btn btn-primary btn-info-full next-step">Save and continue</button></li>
                                    </ul>
                                </div>
                                <div class="tab-pane" role="tabpanel" id="complete">
                                    <h3>Complete</h3>
                                    <p>You have successfully completed all steps.</p>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </form>
                    </div>
                </section>
            </div>
            </div>

        </div>
    </div>
</div>

<style>

.wizard .nav-tabs {
    position: relative;
    margin: 40px auto;
    margin-bottom: 0;
    border-bottom-color: #fff;
}

.wizard > div.wizard-inner {
    position: relative;
}

.connecting-line {
    height: 2px;
    background: #fff;
    position: absolute;
    width: 80%;
    margin: 0 auto;
    left: 0;
    right: 0;
    top: 50%;
    /* z-index: 1; */
}

.wizard .nav-tabs > li.active > a, .wizard .nav-tabs > li.active > a:hover, .wizard .nav-tabs > li.active > a:focus {
    color: #555555;
    cursor: default;
    border: 0;
    border-bottom-color: transparent;
}

span.round-tab {
    width: 50px;
    height: 50px;
    line-height: 50px;
    display: inline-block;
    border-radius: 100px;
    background: #fff;
    border: 2px solid #2196F3;
    z-index: 2;
    position: absolute;
    left: 0;
    text-align: center;
    /* font-size: 25px; */
}
span.round-tab i{
    color:#2196F3;
}
.wizard li.active span.round-tab {
    background: #fff;
    border: 2px solid #4CAF50;
    
}
.wizard li.active span.round-tab i{
    color: #5bc0de;
}

span.round-tab:hover {
    color: #333;
    border: 2px solid #4CAF50;
}

.wizard .nav-tabs > li {
    width: 20%;
}

.wizard li:after {
    content: " ";
    position: absolute;
    left: 46%;
    opacity: 0;
    margin: 0 auto;
    bottom: 0px;
    border: 5px solid transparent;
    border-bottom-color: #5bc0de;
    transition: 0.1s ease-in-out;
}

.wizard li.active:after {
    content: " ";
    position: absolute;
    left: 46%;
    opacity: 1;
    margin: 0 auto;
    bottom: 0px;
    border: 10px solid transparent;
    border-bottom-color: #4CAF50;
}

.wizard .nav-tabs > li a {
    width: 50px;
    height: 50px;
    margin: 20px auto;
    border-radius: 100%;
    padding: 0;
}

    .wizard .nav-tabs > li a:hover {
        background: transparent;
    }

.wizard .tab-pane {
    position: relative;
    padding-top: 50px;
}

.wizard h3 {
    margin-top: 0;
}

@media( max-width : 585px ) {

    .wizard {
        width: 90%;
        height: auto !important;
    }

    span.round-tab {
        font-size: 16px;
        width: 50px;
        height: 50px;
        line-height: 50px;
    }

    .wizard .nav-tabs > li a {
        width: 50px;
        height: 50px;
        line-height: 50px;
    }

    .wizard li.active:after {
        content: " ";
        position: absolute;
        left: 35%;
    }
}
</style>


<script>

$(document).ready(function () {
    //Initialize tooltips
    $('.nav-tabs > li a[title]').tooltip();
    
    //Wizard
    $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {

        var $target = $(e.target);
    
        if ($target.parent().hasClass('disabled')) {
            return false;
        }
    });

    $(".next-step").click(function (e) {

        var $active = $('.wizard .nav-tabs li.active');
        $active.next().removeClass('disabled');
        nextTab($active);

    });
    $(".prev-step").click(function (e) {

        var $active = $('.wizard .nav-tabs li.active');
        prevTab($active);

    });
});

function nextTab(elem) {
    $(elem).next().find('a[data-toggle="tab"]').click();
}
function prevTab(elem) {
    $(elem).prev().find('a[data-toggle="tab"]').click();
}
</script>