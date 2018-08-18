import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class TwitterApiTest {

	String tweetId;

	String consumerKey = "O47q9oV6D2ctVcRp68e0ef8qY";
	String consumerSecret = "18D2QueyjIDDTxFLxxb95xWPwtpA11439yYdR3CVitcGftNk0L";
	String accessTocken = "1013396522053132288-UCeDDvuVaYQ5ly4HBdTqBvcMBPEwsI";
	String accessSecret = "zKUVvDFW8HkvxHX4wXdzCT44hgbqVKFwQD0MIVeAxefpn";

	@Test (priority=1)
	public void getTweetsTest(){
		RestAssured.baseURI = "https://api.twitter.com";
		given().
				auth().
				oauth(consumerKey, consumerSecret, accessTocken, accessSecret).
				when().get("/1.1/statuses/home_timeline.json").
				then().
				assertThat().
				statusCode(200).
				body("$", hasSize(0));
	}

	@Test (priority=2)
	public void postTweetTest(){
		RestAssured.baseURI = "https://api.twitter.com";
		tweetId = given().
				auth().
				oauth(consumerKey, consumerSecret, accessTocken, accessSecret).
				queryParam("status", "Auto generated tweet").
				when().
				post("/1.1/statuses/update.json").
				then().
				assertThat().
				statusCode(200).
				extract().path("id_str");
	}

	@Test (priority=3)
	public void checkTweetTest(){
		RestAssured.baseURI = "https://api.twitter.com";
		given().
				auth().
				oauth(consumerKey, consumerSecret, accessTocken, accessSecret).
				param("count", 1).
				when().
				get("/1.1/statuses/home_timeline.json").
				then().
				assertThat().
				statusCode(200).
				body("text[0]", equalTo("Auto generated tweet")).
				and().
				body("id_str[0]", equalTo(tweetId));
	}


	@Test (priority=4)
	public void deleteTweetTest(){
		RestAssured.baseURI = "https://api.twitter.com";
		given().
				auth().
				oauth(consumerKey, consumerSecret, accessTocken, accessSecret).
				when().
				post("/1.1/statuses/destroy/" + tweetId + ".json").
				then().
				assertThat().
				statusCode(200);
	}


	@Test (priority=5)
	public void checkDeletedTweetsTest(){
		RestAssured.baseURI = "https://api.twitter.com";
		given().
				auth().
				oauth(consumerKey, consumerSecret, accessTocken, accessSecret).
				when().get("/1.1/statuses/home_timeline.json").
				then().
				assertThat().
				statusCode(200).
				body("$", hasSize(0));
	}
}
