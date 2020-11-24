<!-- Main sidebar -->
<div class="sidebar sidebar-main fii-sidebar-menu">
    <div class="sidebar-content">

            <!-- Main navigation -->
            <div class="sidebar-category sidebar-category-visible">
                    <div class="category-content no-padding">
                            <ul class="navigation navigation-main navigation-accordion">

                                <!-- Main -->
                                <li class="imenu" path="home">
                                    <a href="home"><i class="icon-home4"></i><span>Home</span></a>
                                </li>
                                <li>
                                    <a href="#"><i class="icon-stack2"></i> <span>QUE VO</span></a>
                                        <ul>
                                            <li>
                                                <a href="#"> <span>B06</span> <i class="icon-stack2"></i></a>
                                                <ul>
                                                    <li>
                                                        <a href="#">SMT & PTH Management</a>
                                                        <ul>
                                                            <li class="imenu" path="b06-total-line-customer"><a
                                                                data-href="/customer/customer-total-line?factory=B06"
                                                                onclick="openWindow(this.dataset.href);">Total Line
                                                                Dashboard</a></li>
                                                        </ul>
                                                    </li>
                                                    <li>
                                                        <a href="#">Tester Management</a>
                                                        <ul>
                                                            <li class="imenu" path="b06-dashboard-customer"><a
                                                                data-href="/customer/customer-dashboard?factory=B06"
                                                                onclick="openWindow(this.dataset.href);">Tester
                                                                Dashboard</a></li>
                                                            <!-- <li class="imenu" path="b06-status-customer"><a
                                                                data-href="/customer/customer-status?factory=B06"
                                                                onclick="openWindow(this.dataset.href);">Tester
                                                                Status</a></li> -->
                                                            <li class="imenu" path="b06-detail-customer"><a
                                                                data-href="/customer/customer-detail?factory=B06"
                                                                onclick="openWindow(this.dataset.href);">Tester
                                                                Reason Analysis</a></li>
                                                            <li class="imenu" path="b06-cpk-customer"><a
                                                                data-href="/customer/customer-cpk?factory=B06"
                                                                onclick="openWindow(this.dataset.href);">Tester
                                                                CPK Analysis</a></li>
                                                            <li class="imenu" path="b06-pe-re-tab-ete-customer"><a
                                                                data-href="/customer/customer-pe-re-tab?factory=B06"
                                                                onclick="openWindow(this.dataset.href);">ETE & Top 3 Repair Issue</a></li>
                                                         
                                                        </ul>
                                                    </li>
                                                      
                                                </ul>
                                            </li>
                                           
                                        </ul>
                                </li> 
                               <!-- /main -->
                            </ul>        
                    </div>
            </div>
            <!-- /main navigation -->

    </div>
</div>
<!-- /main sidebar -->
<script src="/assets/js/custom/common.js"></script>
<script>
    activeMenu('${path}', '${factory}');
</script>