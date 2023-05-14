//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//import org.jsoup.nodes.Element
//import org.openqa.selenium.chrome.ChromeDriver
//import org.openqa.selenium.chrome.ChromeOptions
//import org.openqa.selenium.support.ui.ExpectedConditions
//import org.openqa.selenium.support.ui.WebDriverWait
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//
//@Serializable
//data class Listing(
//    val title: String,
//    val link: String,
//    val photoUrl: String,
//    val price: String,
//    val size: String,
//    val floor: String,
//    val year: String,
//    val sellerTitle: String
//)
//
//
//fun main(args: Array<String>) {
//    val chromeDriverPath = args[0] // or System.getenv("CHROMEDRIVER_PATH")
//    for (i in 1..2) {
//        scraper(i, 0, 999999999, "stanovanje", chromeDriverPath)
//    }
//}
//
//fun scraper(numberOfPage: Int, minCost: Int, maxCost: Int, typOfBuilding: String, chromeDriverPath: String) {
//    System.setProperty("webdriver.chrome.driver", chromeDriverPath)
//    System.setProperty("webdriver.chrome.driver", "C:\\Users\\LENOVO\\OneDrive\\gradivo za feri\\4.Semester\\Praktikum\\chromedriver.exe")
//    var url = ""
//    if (numberOfPage == 1) {
//        url = "https://www.nepremicnine.net/oglasi-prodaja/ljubljana-mesto/$typOfBuilding/cena-od-$minCost-do-$maxCost-eur/?s=3&nadst%5B0%5D=vsa&nadst%5B1%5D=vsa"
//    } else {
//        url = "https://www.nepremicnine.net/oglasi-prodaja/ljubljana-mesto/$typOfBuilding/cena-od-$minCost-do-$maxCost-eur/$numberOfPage/?s=3&nadst%5B0%5D=vsa&nadst%5B1%5D=vsa"
//    }
//    val chromeOptions = ChromeOptions()
//    chromeOptions.setHeadless(false)
//    val driver = ChromeDriver(chromeOptions)
//    driver.get(url)
//
//    val wait = WebDriverWait(driver, 60)
//    wait.until(ExpectedConditions.urlToBe(url))
//
//    val pageSource: String = driver.pageSource
//    driver.quit()
//
//    val document: Document = Jsoup.parse(pageSource)
//
//
//    val listings = document.select(".oglas_container")
//    val listingData = ArrayList<Listing>()
//
//    for (listing in listings) {
//        val title = getTextSafely(listing, ".title")
//        val link = getTextSafely(listing, "a", "href")
//        val photoUrl = getTextSafely(listing, "a.slika img", "data-src")
//        val price = getTextSafely(listing, ".cena")
//        val size = getTextSafely(listing, ".velikost")
//        val floor = getTextSafely(listing, ".nadstropje")
//        val year = getTextSafely(listing, ".leto")
//        val sellerTitle = getTextSafely(listing, ".prodajalec_o", "title")
//
//
//        println("Title: $title")
//        println("Link: https://www.nepremicnine.net/$link")
//        println("Photo URL: $photoUrl")
//        println("Price: $price")
//        println("Size: $size")
//        println("Floor: $floor")
//        println("Year: $year")
//        println("Seller Title: $sellerTitle")
//        println("------------")
//        val listingObj = Listing(title, "https://www.nepremicnine.net/$link", photoUrl, price, size, floor, year, sellerTitle)
//        listingData.add(listingObj)
//    }
//    val jsonData = Json.encodeToString(listingData) // array vseh elementov v Json formatu
//    //tu je potrebno implementirat da bo vrnilo array vseh elementov
//}
//
//fun getTextSafely(element: Element, selector: String, attribute: String? = null): String {
//    val selectedElement = element.selectFirst(selector)
//    return if (selectedElement != null) {
//        if (attribute != null) {
//            selectedElement.attr(attribute).ifBlank { "N/A" }
//        } else {
//            selectedElement.text().ifBlank { "N/A" }
//        }
//    } else {
//        "N/A"
//    }
//}
