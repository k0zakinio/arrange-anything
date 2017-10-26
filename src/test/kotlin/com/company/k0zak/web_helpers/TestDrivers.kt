package com.company.k0zak.web_helpers

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

object TestDrivers {
    fun getChromeDriver(): ChromeDriver {
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        System.setProperty("webdriver.chrome.driver", "lib/chromedriver")
        return ChromeDriver(chromeOptions)
    }
}