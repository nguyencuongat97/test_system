<!-- Load page -->
<div class="animationload" style="display: none;">
    <div class="loader" style="display: none;">
    </div>
</div>
<!-- End load page -->
<!-- Content Wrapper -->
<div id="wrapper">
    <div class="container">
        <!-- brick of wall -->
        <div class="brick"></div>
        <!-- end brick of wall -->
        <!-- Number -->
        <div class="number">
            <div class="four"></div>
            <div class="zero">
                <div class="nail"></div>
            </div>
            <div class="four"></div>
        </div>
        <!-- end Number -->
        <!-- Info -->
        <div class="info">
            <h2>Something is wrong</h2>
            <p>The page you are looking for was moved, removed, renamed or might never existed.</p>
            <a href="/home" class="btn">Go Home</a>
        </div>
        <!-- end Info -->
    </div>
    <!-- end container -->
</div>
<!-- end Content Wrapper -->
<!-- Footer -->
<footer id="footer">
    <div class="container">
        <!-- Worker -->
        <div class="worker"></div>
        <!-- Tools -->
        <div class="tools"></div>
    </div>
    <!-- end container -->
</footer>
<!-- end Footer -->
<style>
*{
    font-family: 'Open Sans', Helvetica, Arial, sans-serif;
    font-weight: 300;
}
html{
    height: 100%;
}
body{
    position: relative;
    height: 100%;
}

h1, h2, h3, h4, h5, h6{
    color: #ffffff;
}
p{
    font-weight: 300;
}
img{
    background-size: auto;
}
.error{
    border-color: red !important;
}
::-webkit-input-placeholder { /* WebKit browsers */
    color:    #ffffff;
}
:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
    color:    #ffffff;
    opacity:  1;
}
::-moz-placeholder { /* Mozilla Firefox 19+ */
    color:    #ffffff;
    opacity:  1;
}
:-ms-input-placeholder { /* Internet Explorer 10+ */
    color:    #ffffff;
}
.container{
    position: relative;
}
/* ------------------
    Button
-------------------- */
.btn{
    font-size: 18px;
    font-weight: 300;
    color: #ffffff;
    border: 0px solid;
    border-bottom: 2px solid;
    border-color: #12172e;
    padding: 10px 41px;
    border-radius: 5px;
    background: none;
    display: inline-block;
    margin: 10px 0;
    background-color: #00a9e1;
    -webkit-transition: all 0.5s ease-in-out;
    -moz-transition: all 0.5s ease-in-out;
    -ms-transition: all 0.5s ease-in-out;
    -o-transition: all 0.5s ease-in-out;
    transition: all 0.5s ease-in-out;
    text-decoration: none;
    cursor: pointer;
}
.btn:hover{
    background: #0090c0;
    color: #ffffff;
    text-decoration: none;
}
/* ------------------
    Wrapper
-------------------- */
#wrapper{
    min-height: 100%;
    height: 100%;
    width: 100%;
}

.brick{
    position: absolute;
    top: 135px;
    left: -20px;
    width: 435px;
    height: 210px;
    background-image: url("/assets/images/custom/image404/wall-1.png");
    background-size: 100%;
    background-repeat: no-repeat;
    background-position: center;
}
/* ------------------
    Number - 404
--------------------*/
.number{
    position: relative;
    z-index: 100;
    text-align: right;
    margin-top: 21px;
}

.number .four{
    width: 230px;
    height: 292px;
    display: inline-block;
    background-image: url("/assets/images/custom/image404/four.png");
    background-repeat: no-repeat;
    background-size: 100%;
    z-index: 19;
    position: relative;
}

.number .zero{
    width: 230px;
    height: 292px;
    display: inline-block;
    background-image: url("/assets/images/custom/image404/zero.png");
    background-repeat: no-repeat;
    background-size: 100%;
    background-position: 0px 20px;
    position: relative;
    z-index: 18;
    -moz-animation: 4s ease 0s normal none infinite zero;
    -moz-transform-origin: center top;
    -webkit-animation:zero 4s infinite ease-in-out;
    -webkit-transform-origin:top;
    -o-animation: 4s ease 0s normal none infinite zero;
    -o-transform-origin: center top;
    -o-animation:zero 4s infinite ease-in-out;
    -o-transform-origin:top;
    -ms-animation: 4s ease 0s normal none infinite zero;
    -ms-transform-origin: center top;
    -ms-animation:zero 4s infinite ease-in-out;
    -ms-transform-origin:top;
    animation: 4s ease 0s normal none infinite zero;
    transform-origin: center top;
    animation:zero 4s infinite ease-in-out;
    transform-origin:top;
}

.number .zero .nail{
    width: 30px;
    height: 50px;
    top: 6px;
    left: 115px;
    background-image: url("/assets/images/custom/image404/nail.png");
    background-repeat: no-repeat;
    background-size: 100%;
    position: absolute;
}

@-moz-keyframes zero {
    0%{-moz-transform:rotate(-2deg)}
    50%{-moz-transform:rotate(2deg)}
    100%{-moz-transform:rotate(-2deg)}
}

@-webkit-keyframes zero {
    0%{-webkit-transform:rotate(-2deg)}
    50%{-webkit-transform:rotate(2deg)}
    100%{-webkit-transform:rotate(-2deg)}
}

@-o-keyframes zero {
    0%{-o-transform:rotate(-2deg)}
    50%{-o-transform:rotate(2deg)}
    100%{-o-transform:rotate(-2deg)}
}

@-ms-keyframes zero {
    0%{-ms-transform:rotate(-2deg)}
    50%{-ms-transform:rotate(2deg)}
    100%{-ms-transform:rotate(-2deg)}
}

@keyframes zero {
    0%{transform:rotate(-2deg)}
    50%{transform:rotate(2deg)}
    100%{transform:rotate(-2deg)}
}

/* ------------------
    Info
--------------------*/
.info{
    margin-top: -40px;
    margin-left: 510px;
    position: relative;
    z-index: 999;

}

.info h2{
    font-size: 50px;
    font-weight: 300;
    color: #ffc938;
}

.info p{
    font-size: 20px;
    color: #ffffff;
}
/* ------------------
    Footer
--------------------*/
#footer{
    width: 100%;
    text-align: center;
    height: 375px;
    margin-top: -375px;
}
.worker{
    position: absolute;
    left: 0;
    top: 4px;
    width: 300px;
    height: 371px;
    background-size: 100%;
    background-image: url("/assets/images/custom/image404/worker.png");
    background-repeat: no-repeat;
    background-position: center;
    z-index: 10;
}
.tools{
    position: absolute;
    right: 0;
    top: 176px;
    width: 730px;
    height: 199px;
    background-size: 100%;
    background-image: url("/assets/images/custom/image404/tools.png");
    background-repeat: no-repeat;
    background-position: center;
}
/* ------------------
    Animationload
--------------------*/
.animationload {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ffffff;
    z-index: 999999;
}
.loader {
    position: absolute;
    top: 50%;
    left: 50%;
    margin: -100px 0 0 -100px;
    width: 200px;
    height: 200px;
    background-image: url("/assets/images/custom/image404/ajax-loader.gif");
    background-position: center;
    background-repeat: no-repeat;
}
/* ------------------
    Animated setting
--------------------*/
.animated{
    visibility: hidden;
}
.visible{
    visibility: visible;
}


@media (max-width: 1200px) {
.info {
    margin-left: 300px;
}
}
@media (max-width: 992px) {
    .info {
        margin-left: 0;
        text-align: center;
    }
    #wrapper{
        min-height: 0px;
        height: auto;
    }
    #footer {
        height: auto;
        margin-top: 0;
    }
}
@media (max-width: 768px) {
.brick {
    width: 256px;
}
.number {
    text-align: center;
}
.number .four {
    width: 123px;
    height: 157px;
}
.number .zero {
    width: 130px;
    height: 157px;
}
.number .zero .nail {
    width: 18px;
    top: 12px;
    left: 64px;
}

.worker {
    left: 20px;
    top: 7px;
    width: 200px;
    height: 249px;
}
.tools {
    right: 8px;
    top: 166px;
    width: 330px;
    height: 90px;
}
.info h2 {
    font-size: 30px;
}

.info p {
    font-size: 16px;
}
}
@media (max-width: 440px) {
.brick {
    width: 156px;
}
.number {
    text-align: center;
    margin-top: 32px;
}
.number .four {
    width: 80px;
    height: 103px;
}
.number .zero {
    width: 85px;
    height: 112px;
}
.number .zero .nail {
    width: 11px;
    top: 14px;
    left: 44px;
}
}
</style>