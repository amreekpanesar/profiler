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


    public String getLinks(String firstName, String lastName, String location, String fetcher) {

        if (fetcher.contains("stack")) {
            String url =
                    "https://stackoverflow.com/users/filter?search=" + firstName + "+" + lastName;
            //String url = "https://stackoverflow.com/users";
            String users = RestAssured.given().get(url).getBody().asString();
            Elements userNames = Jsoup.parse(users).select(".user-details>a[href]");
            Elements userLocation = Jsoup.parse(users).select(".user-location");
            String userURL = null;

            for (int i = 0; i < userNames.size(); i++) {
                if (userNames.get(i).text().contains(firstName))
                    //if(userLocation.get(i).text().contains(location)) {}
                    userURL = "https://stackoverflow.com/" + userNames.get(i).attr("href");
                break;

            }
            return userURL;
        }


        if (fetcher.contains("linkedin")) {
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

    public Map<String, Object> getStackOverflowProfile() {

        String url = getLinks("Tarun", "Lalwani", "", "stack");
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

        badgeDetails.put("goldBadgeCount",
                Jsoup.parse(outputJson).select(".-total").get(0).text().replace(",", ""));
        badgeDetails.put("silverBadgeCount",
                Jsoup.parse(outputJson).select(".-total").get(1).text().replace(",", ""));
        badgeDetails.put("bronzeBadgeCount",
                Jsoup.parse(outputJson).select(".-total").get(2).text().replace(",", ""));
        stackUserInfo.put("BadgeDetails", badgeDetails);

        stackUserInfo.put("reputation",
                Jsoup.parse(outputJson).select(".reputation").text().replaceAll("[^\\d]", ""));
        stackUserInfo.put("numberOfAnswers",
                Jsoup.parse(outputJson).select(".answers>span").text().replace(",", ""));
        stackUserInfo.put("numberOfQuestions",
                Jsoup.parse(outputJson).select(".questions>span").text().replace(",", ""));
        stackUserInfo.put("peopleReached",
                Jsoup.parse(outputJson).select(".people-helped>span").text().replace(",", ""));

        skillInfo.put("Score",
                Jsoup.parse(outputJson).select(".g-col.-number").get(0).text().replace(",", ""));
        skillInfo.put("Posts",
                Jsoup.parse(outputJson).select(".g-col.-number").get(1).text().replace(",", ""));
        skillMap.add(skillInfo);

        for (int i = 3; i < 12; i += 2) {
            skillInfo = new HashMap<>();
            skillInfo.put("Score", Jsoup.parse(outputJson).select(".g-col.-number").get(i).text()
                    .replace(",", ""));
            skillInfo.put("Posts",
                    Jsoup.parse(outputJson).select(".g-col.-number").get(i + 1).text()
                            .replace(",", ""));
            skillMap.add(skillInfo);
        }

        for (int i = 0; i < 5; i++) {
            userTopSkills.put(Jsoup.parse(outputJson).select(".post-tag").get(i).text(),
                    skillMap.get(i));
        }

        stackUserInfo.put("SkillSet", userTopSkills);
        return stackUserInfo;
    }

    public Map<String, Object> getGitHubData() {

        String url = getLinks("meghna", "bhasin", "", "git");
        ResponseBody response = RestAssured.given().get(url).getBody();

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
        System.out.println(gitHubJSon);
        return gitHubJSon;
    }

}

