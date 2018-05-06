package com.mettl.profiler.crawler;

import io.restassured.RestAssured;

import java.util.*;

import com.jayway.jsonpath.JsonPath;
import io.restassured.response.ResponseBody;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Crawler {


    public String getLinks(String firstName, String lastName, String location, String email,
            String source) {

        RestAssured.useRelaxedHTTPSValidation();

        if (source.contains("stack")) {
            String url =
                    "https://stackoverflow.com/users/filter?search=" + firstName + "+" + lastName
                            + "&filter=All&tab=Reputation&_=" + System.currentTimeMillis();
            //String url = "https://stackoverflow.com/users";

            RestAssured.urlEncodingEnabled = false;
            String users = RestAssured.given().header("X-Requested-With", "XMLHttpRequest")
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                    .get(url).getBody().asString();
            Elements userNames = Jsoup.parse(users).select("div[class=user-details]>a");
            Elements userLocation = Jsoup.parse(users).select(".user-location");
            String userURL = null;

            for (int i = 0; i < userNames.size(); i++) {
                if (userNames.get(i).text().toLowerCase().contains(firstName.toLowerCase()))
                    //if(userLocation.get(i).text().contains(location)) {}
                    userURL = "https://stackoverflow.com/" + userNames.get(i).attr("href");
                break;

            }
            return userURL;
        }

        if (source.contains("git")) {
            String searchUrl = "https://api.github.com/search/users?q=" + firstName;
            String searchResultJson = RestAssured.given().get(searchUrl).getBody().asString();
            int searchResultCounts = JsonPath.read(searchResultJson, "$.total_count");
            //LinkedList<String > profileUrls= new LinkedList<>();
            //profileUrls.add("https://api.github.com/users/"+JsonPath.read(searchResultJson,"$.items."+i+".login")+"/events/public");
            JSONArray userNames = JsonPath.read(searchResultJson, "$..login");

            for (int i = 0; i < searchResultCounts; i++) {
                String profileUrls =
                        "https://api.github.com/users/" + userNames.get(i) + "/events/public";

                String publicProfileJson =
                        RestAssured.given().get(profileUrls).getBody().asString();
                if (!(publicProfileJson.equalsIgnoreCase("[]"))) {
                    JSONArray visibleEmails = JsonPath.read(publicProfileJson, "$..email");
                    for (int j = 0; j < visibleEmails.size(); j++) {
                        if (email.equalsIgnoreCase(visibleEmails.get(j).toString())) {
                            System.out.println("https://github.com/" + userNames.get(i));
                            return "https://github.com/" + userNames.get(i);
                        }
                    }
                }
            }
        }


        if (source.contains("linkedin")) {
            String key = "AIzaSyBXonlcNZ2ka9xAzvWnj6-tjNwE4hwIQLQ";
            String qry = "amreek+mettl";
            String cx = "009230221581079861830%3Au_oznyobngi";

            String url =
                    "https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=" + cx + "&q="
                            + qry;

            RestAssured.urlEncodingEnabled = false;

            String outputJson = RestAssured.given().get(url).getBody().asString();

            JSONArray jsonArrayOrganisation = JsonPath.read(outputJson, "$..link");
            //        JSONArray jsonArrayOrganisation = JsonPath.read(outputJson, "$..person[?( @.org=='Mettl')]");
            //
            //                for (int i = 0; i < jsonArrayOrganisation.size(); i++) {
            //            if(jsonArrayOrganisation.get(i).toString().contains())
            //        }

            return jsonArrayOrganisation.get(0).toString();
        }
        return null;
    }

    //    public void getLinkedInProfile() throws IOException {
    //
    //        String url = getLinks();
    //        String outputJson = RestAssured.given().header("User-Agent",
    //                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0")
    //                .header("Accept",
    //                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    //                .get(url).body().asString();
    //        System.out.println(outputJson);
    //    }

    public String getStackOverflowProfile(String firstName, String lastName, String location,
            String email) {

        RestAssured.useRelaxedHTTPSValidation();
        String url = getLinks(firstName, lastName, location, email, "stack");
        String outputJson = RestAssured.given().header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .get(url).body().asString();
        LinkedHashMap<String, Object> stackUserInfo = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> badgeDetails = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> userTopSkills = new LinkedHashMap<String, Object>();
        Map<String, Object> skillInfo = new TreeMap<String, Object>();
        List<Map<String, Object>> skillMap = new ArrayList<Map<String, Object>>();

        try {
            badgeDetails.put("goldBadgeCount",
                    Jsoup.parse(outputJson).select(".badge1-alternate>.-total").text()
                            .replace(",", ""));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            badgeDetails.put("goldBadgeCount", "null");
        }

        try {
            badgeDetails.put("silverBadgeCount",
                    Jsoup.parse(outputJson).select(".badge2-alternate>.-total").text()
                            .replace(",", ""));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            badgeDetails.put("silverBadgeCount", "null");
        }

        try {
            badgeDetails.put("bronzeBadgeCount",
                    Jsoup.parse(outputJson).select(".badge3-alternate>.-total").text()
                            .replace(",", ""));


            stackUserInfo.put("BadgeDetails", badgeDetails);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            badgeDetails.put("bronzeBadgeCount", "null");
        }

        stackUserInfo.put("reputation",
                Jsoup.parse(outputJson).select(".reputation").text().replaceAll("[^\\d]", ""));
        stackUserInfo.put("numberOfAnswers",
                Jsoup.parse(outputJson).select(".answers>span").text().replace(",", ""));
        stackUserInfo.put("numberOfQuestions",
                Jsoup.parse(outputJson).select(".questions>span").text().replace(",", ""));
        stackUserInfo.put("peopleReached",
                Jsoup.parse(outputJson).select(".people-helped>span").text().replace(",", ""));

        try {
            skillInfo.put("Score", Jsoup.parse(outputJson).select(".g-col.-number").get(0).text()
                    .replace(",", ""));

            skillInfo.put("Posts", Jsoup.parse(outputJson).select(".g-col.-number").get(1).text()
                    .replace(",", ""));
            skillMap.add(skillInfo);

            for (int i = 3; i < Jsoup.parse(outputJson).select(".g-col.-number").size(); i += 2) {
                skillInfo = new HashMap<>();
                skillInfo.put("Score",
                        Jsoup.parse(outputJson).select(".g-col.-number").get(i).text()
                                .replace(",", ""));
                skillInfo.put("Posts",
                        Jsoup.parse(outputJson).select(".g-col.-number").get(i + 1).text()
                                .replace(",", ""));
                skillMap.add(skillInfo);
            }
        } catch (IndexOutOfBoundsException e) {
            skillInfo = null;
        }
        for (int i = 0; i < Jsoup.parse(outputJson).select(".post-tag").size(); i++) {
            userTopSkills.put(Jsoup.parse(outputJson).select(".post-tag").get(i).text(),
                    skillMap.get(i));
        }

        stackUserInfo.put("SkillSet", userTopSkills);
        String jsonString = new JSONObject(stackUserInfo).toString();
        return jsonString;
    }

    public String getGitHubData(String firstName, String lastName, String location, String email) {

        RestAssured.useRelaxedHTTPSValidation();

        String url = getLinks(firstName, lastName, location, email, "git");
        ResponseBody response = RestAssured
                .given().log().all()
                .get(url)
                .getBody();

        Map<String, String> data = new LinkedHashMap<String, String>();

        data.put("User Location", Jsoup.parse(response.asString()).select(".p-label").text());
        data.put("Repositories",
                Jsoup.parse(response.asString()).select("a[title=Repositories] span").text());
        data.put("Stars", Jsoup.parse(response.asString()).select("a[title=Stars] span").text());
        data.put("Followers",
                Jsoup.parse(response.asString()).select("a[title=Followers] span").text());
        data.put("Following",
                Jsoup.parse(response.asString()).select("a[title=Following] span").text());

        JSONObject gitHubJSon = new JSONObject(data);
        System.out.println(gitHubJSon.toJSONString());
        return gitHubJSon.toJSONString();
    }

}

