<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<iframe id="manageNBB" src="http://10.225.35.80:2810/managenbb" style="border: 0px solid;" scrolling="yes"
    frameborder="0" width="100%" height="100%"></iframe>

<script>
    $(document).ready(function () {
        var height = screen.height;
        $('#manageNBB').css('height', height + 'px');
    })
</script>