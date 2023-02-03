package step;

import com.thoughtworks.gauge.Step;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import driver.Driver;
import methods.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class StepImplementation extends Driver {

    Methods methods;
    Logger logger = LogManager.getLogger(Methods.class);

    List<String> directors;
    List<String> writers;
    List<String> stars;

    public StepImplementation (){
        this.methods = new Methods();
    }

    @Step("<seconds> saniye bekle")
    public void waitElement(long seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
        System.out.println(seconds + " saniye beklendi");
        logger.info(seconds + " saniye beklendi");
    }

    @Step("<key> - <keyType> elementine tıklanır")
    public void clickElement(String key, String keyType) {
        WebElement element = methods.getElementByKey(key, keyType);

        if (element == null) {
            System.out.println("!!! HATA ( Element == Null ) HATA !!!");
            logger.error("!!! HATA ( Element == Null ) HATA !!!");
            webDriver.quit();
        }

        if (methods.isDisplayedAndEnabled(element)) {
            methods.clickElement(element);
            System.out.println("( " + key + " ) elementine tiklandi");
            logger.info("( " + key + " ) elementine tiklandi");
        } else {
            System.out.println("( " + key + " ) elementine tiklanamadi");
            logger.info("( " + key + " ) elementine tiklanamadi");
        }

    }

    @Step("<key> - <keyType> elementine <text> değerini yaz")
    public void sendKeysElement(String key, String keyType, String text) {

        WebElement element = methods.getElementByKey(key, keyType);
        text = text.endsWith("KeyValue") ? Driver.TestMap.get(text).toString() : text;

        if (element.isDisplayed() && element.isEnabled()) {
            methods.clickElement(element);
            methods.sendKeys(element, text);
            System.out.println("( " + key + " ) elementine ( " + text + " ) degeri yazildi");
            logger.info("( " + key + " ) elementine ( " + text + " ) degeri yazildi");
        } else {
            System.out.println("( " + key + " ) elementi sayfada goruntulenemedi.");
            logger.info("( " + key + " ) elementi sayfada goruntulenemedi.");
            webDriver.quit();
        }
    }

    @Step("<key> - <keyType> elementinin sayfada görünür olmadığı kontrol edilir")
    public void checkElementVisible(String key, String keyType){
        List<WebElement> elements = methods.getElements(key, keyType);
        int elementSize = elements.size();

        if(elementSize > 0) {
            System.out.println("( " + key + ") elementi gorunur ");
            logger.info("( " + key + ") elementi gorunur ");
            webDriver.quit();
        } else {
            System.out.println("( " + key + ") elementinin sayfada gorunur olmadigi onaylandi ");
            logger.info("( " + key + ") elementinin sayfada gorunur olmadigi onaylandi ");
        }

    }

    @Step("<key> - <keyType> elementinin görünür olması kontrol edilir")
    public void checkVisibleElement(String key, String keyType) {

        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        WebElement element = methods.getElementByKey(key, keyType);
        By locator = null;

        switch (keyType) {

            case "id":
                locator = By.id(key);
                break;

            case "cssSelector":
                locator = By.cssSelector(key);
                break;

            case "xpath":
                locator = By.xpath(key);
                break;

            case "className":
                locator = By.className(key);
                break;

            case "tagName":
                locator = By.tagName(key);
                break;

            case "name":
                locator = By.name(key);
                break;

            default:
                System.out.println("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                logger.info("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                break;
        }

        if (locator == null) {
            System.out.println("!!! HATA ( Locator == Null ) HATA !!!");
            logger.error("!!! HATA ( Locator == Null ) HATA !!!");
            webDriver.quit();
        }

        element = wait.until(visibilityOfElementLocated(locator));

        if (element != null && element.isDisplayed()){
            System.out.println("( " + key + " ) elementi sayfada goruntulendi");
            logger.info("( " + key + " ) elementi sayfada goruntulendi");
        } else {
            System.out.println("( " + key + " ) elementi sayfada goruntulenemedi");
            logger.info("( " + key + " ) elementi sayfada goruntulenemedi");
        }

    }

    @Step("<key> - <keyType> elementine scroll yapılır")
    public void scrollElement(String key, String keyType) throws InterruptedException{

        WebElement element = methods.getElementByKey(key, keyType);

        if (methods.isDisplayedAndEnabled(element)) {
            System.out.println("( " + key + " elementine scroll yapildi ");
            logger.info("( " + key + " elementine scroll yapildi ");
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500);
        } else {
            System.out.println("( " + key + " ) elementine scroll yapilamadi");
            logger.info("( " + key + " ) elementine scroll yapilamadi");
        }

    }

    @Step("<url> gelen url ile aynı mı")
    public void urlControl(String url){

        methods.getUrl(url);

    }
    @Step("<fileName> adında yeni bir dosya oluştur")
    public void createFile(String fileName) throws IOException {

        String filePath = "C://Users//testinium//Desktop//" + fileName;
        File f = new File(filePath);
        if(f.exists()) {
            System.out.println("File already exists.");
        }
        else {
            System.out.println("File created.");
            f.createNewFile();
        }

    }

    @Step("<fileName> dosyasına 'ürün fiyat', 'model adı', 'CPU' bilgilerini yaz")
    public void writeFile(String fileName) throws IOException {

        createFile(fileName);
        String filePath = "C://Users//testinium//Desktop//" + fileName;
        FileWriter myWriter = new FileWriter(filePath);
        String productPriceWhole = webDriver.findElement(By.cssSelector("span[class='a-price-whole']:nth-child(1)")).getText();
        String productPriceFraction = webDriver.findElement(By.cssSelector("span[class='a-price-fraction']:nth-child(2)")).getText();
        String productPriceSymbol = webDriver.findElement(By.cssSelector("span[class='a-price-symbol']:nth-child(3)")).getText();
        String productPrice = productPriceWhole + "." + productPriceFraction + " " + productPriceSymbol;
        System.out.println("Product Price : " + productPrice);
        myWriter.write("Product Price : " + productPrice + "\n");
        String productModelName = webDriver.findElement(By.cssSelector("tr[class='a-spacing-small po-model_name'] td:nth-child(2)")).getText();
        System.out.println("Product Model Name : " + productModelName);
        myWriter.write("Product Model Name : " + productModelName + "\n");
        String productCPU = webDriver.findElement(By.cssSelector("tr[class='a-spacing-small po-cpu_model.family'] td:nth-child(2)")).getText();
        System.out.println("Product CPU : " + productCPU);
        myWriter.write("Product CPU : " + productCPU + "\n");
        myWriter.close();
    }
}
