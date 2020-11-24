<!-- Main navbar -->
<style>
    @media only screen and (min-width: 1280px) and (max-width: 1365px) {
        .navbar-left ul li span {
            font-size: 1.2rem;
            font-family: "Segoe UI", Arial, sans-serif;
        }
    }

    @media only screen and (min-width: 1366px) and (max-width: 1599px) {
        .navbar-left ul li span {
            font-size: 1.2rem;
            font-family: "Segoe UI", Arial, sans-serif;
        }
    }

    @media only screen and (min-width: 1600px) and (max-width: 1919px) {
        .navbar-left ul li span {
            font-size: 1.2rem;
            font-family: "Segoe UI", Arial, sans-serif;
        }
    }

    @media only screen and (min-width: 1920px) {
        .navbar-left ul li span {
            font-size: 1.6rem;
            font-family: "Segoe UI", Arial, sans-serif;
        }
    }

    .navbar-brand img {
        margin-top: -6px;
        height: 35px
    }
</style>
<div class="navbar navbar-default navbar-inverse navbar-fixed-top header-highlight bg-slate-800"
    style="background: transparent;">
    <div class="navbar-header bg-state-800">
        <a class="navbar-brand" style="background-size: auto;" href="/home">
            <img src="/assets/images/logo_fii_new.gif">
        </a>
        <ul class="nav navbar-nav visible-xs-block">
            <!-- <li><a data-toggle="collapse" data-target="#navbar-mobile"><i class="icon-tree5"></i></a></li> -->
            <li><a class="sidebar-mobile-main-toggle"><i class="fa fa-bars"></i></a></li>
        </ul>
    </div>

    <div class="navbar-collapse collapse" id="navbar-mobile">

        <ul class="nav navbar-nav">
            <li><a class="sidebar-control sidebar-main-toggle hidden-xs"><i class="icon-paragraph-justify3"></i></a>
            </li>
        </ul>
        <!-- <div class="navbar-left">
            <ul class="nav navbar-nav">
                <li>
                    <a href="/home">
                        <span id="profileName">Home</span>
                    </a>
                </li>
                <li>
                    <a href="/hr">
                        <span id="profileName">Human Resources</span>
                    </a>
                </li>
                <li>
                    <a href="/about">
                        <span id="profileName">About</span>
                    </a>
                </li>
            </ul>
        </div> -->

        <div class="navbar-right">
            <!-- <p class="navbar-text">Morning, Icarus!</p>
            <p class="navbar-text"><span class="label bg-success-400">Online</span></p> -->

            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i style="font-size: 18px" class="fa fa-user"></i> <span style="font-size: 14px"
                            id="profileName">Profile</span>
                        <span class="visible-xs-inline-block position-right">Activity</span>
                    </a>

                    <div class="dropdown-menu dropdown-content">
                        <!-- <div class="dropdown-content-heading">
                            Activity
                            <ul class="icons-list">
                                <li><a href="#"><i class="icon-menu7"></i></a></li>
                            </ul>
                        </div> -->

                        <ul class="media-list dropdown-content-body">
                            <li class="media">
                                <div class="media-left no-padding">
                                    <p href="#" class="btn-rounded btn-icon btn-xs legitRipple"><i
                                            class="fa fa-sign-out"></i></p>
                                </div>

                                <div class="media-body">
                                    <a href="/logout" style="font-size: 16px">Logout</a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>

    </div>
</div>
<!-- /main navbar -->

<script>
    var loginID = '${employeeId}';
    var loginName = '${employeeName}';
    if (loginID == '' || loginID == null || loginID == undefined) {
        $('.navbar-right .nav.navbar-nav').addClass('hidden');
    }
    else {
        $('.navbar-right .nav.navbar-nav').removeClass('hidden');
        $('#profileName').html(loginName);
    }
</script>