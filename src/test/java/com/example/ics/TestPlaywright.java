package com.example.ics;

import com.microsoft.playwright.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlaywright {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("http://localhost:4200/classify");

            Thread.sleep(250);

            page.click(".clr-combobox-wrapper .clr-combobox-trigger");
            page.click("clr-option[clrvalue=\"clarifai\"]");

            Thread.sleep(250);

            page.fill("input[formcontrolname=\"imageUrl\"]", "https://th.bing.com/th/id/R.fd6e965e5f9050f3b9060f953647f779?rik=rp1%2bUMaSp3sI5Q&pid=ImgRaw&r=0");
            page.click("button.btn.btn-primary");

            Thread.sleep(250);

            page.click("button#remakeTags");
            String initialURL = page.url();
            Thread.sleep(3000);
            page.click("a[routerlink=\"/gallery\"]");
            Thread.sleep(1000);
            page.click("button.ics-sort");
            page.click(".card.clickable:first-child");
            Thread.sleep(1000);
            String finalURL = page.url();
            assertEquals(initialURL, finalURL);

            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
