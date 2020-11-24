//package com.foxconn.fii;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.Select;
//
//@Slf4j
//public class SeleniumTest {
//
//    @Test
//    public void selenium() {
////        System.setProperty("webdriver.gecko.driver","C:\\geckodriver.exe");
////        WebDriver driver = new FirefoxDriver();
//        //comment the above 2 lines and uncomment below 2 lines to use Chrome
//        ChromeOptions options = new ChromeOptions();
//        // setting headless mode to true.. so there isn't any ui
////        options.setHeadless(true);
//
//        System.setProperty("webdriver.chrome.driver","D:\\tiennd\\chromedriver.exe");
//        WebDriver driver = new ChromeDriver(options);
//
//        String baseUrl = "http://demo.guru99.com/test/newtours/";
//        String expectedTitle = "Welcome: Mercury Tours";
//        String actualTitle = "";
//
//        // launch Fire fox and direct it to the Base URL
//        driver.get(baseUrl);
//
//        driver.findElement(By.cssSelector("input[name='userName']")).sendKeys("hello");
//        driver.findElement(By.cssSelector("input[name='password']")).sendKeys("world");
////        Select selectbox = new Select(driver.findElement(By.name("country")));
////        selectbox.selectByVisibleText("ANTARCTICA");
//
//        // get the actual value of the title
//        actualTitle = driver.getTitle();
//
//        /*
//         * compare the actual title of the page with the expected one and print
//         * the result as "Passed" or "Failed"
//         */
//        if (actualTitle.contentEquals(expectedTitle)){
//            System.out.println("Test Passed!");
//        } else {
//            System.out.println("Test Failed");
//        }
//
//        //close Fire fox
//        //driver.close();
//    }
//}
