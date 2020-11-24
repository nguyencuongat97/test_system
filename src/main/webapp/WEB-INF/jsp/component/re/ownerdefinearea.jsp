<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="container-fluid" style="text-align: center">
    <div class="row" style="padding: 15px 0px; position: relative;">
        <div class="col-md-12">
            <img src="/assets/images/custom/re/ownerda.png" alt="" width="100%">
            <div class="line-r1" onclick="reOwnerDefine()" title="Area RE"></div>\
        </div>
    </div>
</div>
<style>
    .line-r1 {
        position: absolute;
        top: 23%;
        left: 1.5%;
        width: 3.75%;
        height: 37%;
        cursor: pointer;
    }

    div.line-r1:hover {
        background: #cc292982;
    }
</style>
<script>
    function reOwnerDefine() {
        openWindow("/re/checklist/ownerdefine")
    }
</script>