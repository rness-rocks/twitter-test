import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;

public class TwitterMobileTest {

	private SelenideElement emptyTitleHeader = $(".HomeEmptyTimeline-header");
	private SelenideElement newTweetBar = $(".new-tweets-bar");

	@BeforeTest
	public void setup() {
		Configuration.browser = AndroidDriverProvider.class.getName();
		Configuration.baseUrl = "https://twitter.com";
		open("/login");
		login("myro666@icloud.com","Password1234");
	}

	@AfterTest
	public void tearDown(){
		closeWebDriver();
	}

	@Test(priority=1)
	public void createTweet() {
		emptyTitleHeader.shouldHave(visible, text("What? No Tweets yet?"));
		$(byId("global-new-tweet-button")).click();
		$(byText("Compose new Tweet")).waitUntil(visible, 5000);

		$(".is-fakeFocus > div:nth-child(2) > div:nth-child(2) > div:nth-child(1)").
				val("New tweet added for test web ui automation demo");

		$("#Tweetstorm-tweet-box-0 > div:nth-child(2) > div:nth-child(2) > div:nth-child(2) > span:nth-child(1) > button:nth-child(2)").click();

		newTweetBar.waitUntil(visible, 20000).click();

		$(".TweetTextSize").shouldHave(visible, text("New tweet added for test web ui automation demo"));
	}

	@Test(priority=2)
	public void deleteTweet() {
		$(".ProfileTweet-action--more > div:nth-child(1) > button:nth-child(1)").click();
		$(".js-actionDelete > button:nth-child(1)").click();
		$(byId("delete-tweet-dialog-header")).waitUntil(visible, 5000);
		$(".delete-action").click();

		emptyTitleHeader.shouldHave(visible, text("What? No Tweets yet?"));

		logout();

		$(".StaticLoggedOutHomePage-communicationContent").waitUntil(visible, 7000);
		$$(byXpath("/html/body/div[1]/div/div[1]/div[2]/div/div")).shouldHave(CollectionCondition.size(3));
	}

	public void login(String name, String pass) {
		$(".js-username-field").val(name);
		$(".js-password-field").val(pass);
		$("button.submit").click();
	}

	public void logout(){
		$("#user-dropdown-toggle").click();
		$("#signout-button > button:nth-child(1)").click();
	}
}

class AndroidDriverProvider implements WebDriverProvider {

	public static final String USERNAME = "YOUR_USERNAME";
	public static final String ACCESS_KEY = "YOUR_ACCESS_KEY";
	public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";


	public WebDriver createDriver(DesiredCapabilities capabilities) {
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName","Android Emulator");
		capabilities.setCapability("deviceType","phone");
		capabilities.setCapability("deviceOrientation", "portrait");
		capabilities.setCapability("platformVersion", "7.0");
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Browser");
		capabilities.setCapability("appiumVersion", "1.5.3");

		try {
			return new AndroidDriver(new URL(URL), capabilities);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
