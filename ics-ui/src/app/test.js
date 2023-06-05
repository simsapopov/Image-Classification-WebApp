const { chromium } = require('playwright');

function delay(time) {
  return new Promise(function (resolve) {
    setTimeout(resolve, time);
  });
}

(async () => {
  const browser = await chromium.launch({ headless: false });
  const context = await browser.newContext();
  const page = await context.newPage();
  await page.goto('http://localhost:4200/classify');

  await delay(250);

 
  await page.click('.clr-combobox-wrapper .clr-combobox-trigger');
  await page.click('clr-option[clrvalue="clarifai"]');

 
  await delay(250);

  await page.fill('input[formcontrolname="imageUrl"]', 'https://th.bing.com/th/id/R.fd6e965e5f9050f3b9060f953647f779?rik=rp1%2bUMaSp3sI5Q&pid=ImgRaw&r=0');

  await delay(250);

  await page.click('button.btn-primary');
  const firstTag = await page.textContent('table.table tbody tr:first-child td:nth-child(2)');
  console.log('First Tag:', firstTag);
  
  await delay(2500);

  await page.click('a[routerlink="/gallery"]');
  await page.fill('input#Tag', 'car');
  await delay(2500);

  await page.click('button.ics-sort');

  await delay(2500);
  await page.click('.card.clickable');
  const currentFirstTag = await page.textContent('table.table tbody tr:first-child td:nth-child(2)');
  const isFirstTagSame = firstTag === currentFirstTag;
  await page.click('button.btn.btn-primary');


  

  const result = await page.textContent('div#result');
  console.log('Result:', result);

  await browser.close();
})();
