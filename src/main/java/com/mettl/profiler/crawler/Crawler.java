package com.mettl.profiler.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


public class Crawler {


    public String getLinks(String firstName, String lastName, String location, String organisation,
            String email, String source) {

        RestAssured.useRelaxedHTTPSValidation();



        ////////////////////////////////////////////////// Crawl
        ////////////////////////////////////////////////// StackOverflow///////////////////////

        if (source.contains("stack")) {


            String url = "https://stackoverflow.com/users/filter?search=" + firstName + "+"
                    + lastName + "&filter=All&tab=Reputation&_=" + System.currentTimeMillis();
            if (lastName.isEmpty())
                url = "https://stackoverflow.com/users/filter?search=" + firstName
                        + "&filter=All&tab=Reputation&_=" + System.currentTimeMillis();

            RestAssured.urlEncodingEnabled = false;

            // Hitting Search URL for Stack Overflow
            String users = RestAssured.given().header("X-Requested-With", "XMLHttpRequest").header(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                    .get(url).getBody().asString();
            Elements userNames = Jsoup.parse(users).select("div[class=user-details]>a");
            // Elements userLocation = Jsoup.parse(users).select(".user-location");
            String userURL = null;

            for (int i = 0; i < userNames.size(); i++) {
                if (userNames.get(i).text().toLowerCase().contains(firstName.toLowerCase()))

                    // Add this to Filter using Location
                    // if(userLocation.get(i).text().contains(location)) {}
                    userURL = "https://stackoverflow.com/" + userNames.get(i).attr("href");
                break;

            }
            return userURL;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////



        //////////////////////////////////////////// Crawl
        //////////////////////////////////////////// GitHub////////////////////////////////////

        if (source.contains("git")) {
            String searchUrl = "https://api.github.com/search/users?q=" + firstName;
            String searchResultJson = RestAssured.given().get(searchUrl).getBody().asString();
            int searchResultCounts = JsonPath.read(searchResultJson, "$.total_count");
            // LinkedList<String > profileUrls= new LinkedList<>();
            // profileUrls.add("https://api.github.com/users/"+JsonPath.read(searchResultJson,"$.items."+i+".login")+"/events/public");
            JSONArray userNames = JsonPath.read(searchResultJson, "$..login");

            for (int i = 0; i < userNames.size() - 1; i++) {
                String profileUrls =
                        "https://api.github.com/users/" + userNames.get(i) + "/events/public";

                String publicProfileJson =
                        RestAssured.given().get(profileUrls).getBody().asString();
                if (!(publicProfileJson.equals("[]"))) {
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

        return null;
        ////////////////////////////////////////////////////////////////////////////////////////////



        //////////////////////////////////////// Crawl
        //////////////////////////////////////// Linkedin///////////////////////////////////////
        //
        // if (source.contains("linkedin")) {
        // String key = "AIzaSyBXonlcNZ2ka9xAzvWnj6-tjNwE4hwIQLQ";
        // String qry = firstName + "+" + lastName + "+" + organisation;
        // String cx = "009230221581079861830%3Au_oznyobngi";
        //
        // String url =
        // "https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=" + cx + "&q="
        // + qry;
        //
        // RestAssured.urlEncodingEnabled = false;
        //
        // String outputJson = RestAssured.given().get(url).getBody().asString();
        //
        // JSONArray jsonArrayLinks = JsonPath.read(outputJson, "$..link");
        // JSONArray jsonArrayOrganisation = JsonPath.read(outputJson, "$..hcard..title");
        // JSONArray jsonArrayNames = JsonPath.read(outputJson, "$..hcard..fn");
        //
        // for (int i = 0; i < jsonArrayOrganisation.size(); i++) {
        // if (jsonArrayOrganisation.get(i).toString().toLowerCase()
        // .contains(organisation.toLowerCase()) && (jsonArrayNames.get(i).toString()
        // .toLowerCase().contains(firstName.toLowerCase())))
        // return jsonArrayLinks.get(i).toString();
        // }
        // }
        // return null;
        ////////////////////////////////////////////////////////////////////////////////////////////////
    }



    public void getLinkedInProfile(String firstName, String lastName, String location,
            String organisation, String email) throws IOException {


        String url = getLinks(firstName, lastName, location, organisation, email, "linkedin");



        String output = RestAssured.given().header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .cookie("VID", "V_2018_05_04_06_800")
                .cookie("bcookie", "v=2&424dcfd8-7769-4832-8979-dc73235c335a")
                .cookie("JSESSIONID", "ajax:5403788336768947062")
                .cookie("li_at",
                        "AQEDAScTkgoEOBWpAAABYzXjYugAAAFjWe_m6FEAewsBISXo780mjaQVLvF4LYZMW0DskLLkoGteq93LrtQp1wtBiEMy96Opp8xtsjdBLl7UM0qI_4oaF8pHmcd37AlrALJ6o3T_qC3br9FQwFLxByjU")
                .get(url).asString();



        System.out.println(Jsoup.parse(output, "", Parser.htmlParser()));
        System.out.println(Jsoup.parse(output, "", Parser.htmlParser()));



        // LinkedHashMap<String, Object> linkedInDetails = new LinkedHashMap<>();
        // linkedInDetails.put("PastPositionDetails",
        // Jsoup.parse(output).select("tr[data-section='pastPositionsDetails']").text());
        //
        // linkedInDetails.put("Connections",
        // Jsoup.parse(output).select(".member-connections").text());
        //
        // LinkedHashMap<String, Object> experience = new LinkedHashMap<>();
        // int experienceCount = Jsoup.parse(output).select("#experience>ul>li").size();
        // for (int i = 0; i < experienceCount; i++)
        // experience.put("Experience " + i,
        // Jsoup.parse(output).select("#experience>ul>li").text());
        // linkedInDetails.put("Experiences", experience);
        //
        // LinkedHashMap<String, Object> education = new LinkedHashMap<>();
        // int educationCount = Jsoup.parse(output).select("#education>ul>li").size();
        // for (int i = 0; i < educationCount; i++)
        // education.put("Education " + i,
        // Jsoup.parse(output).select("#education>ul>li").text());
        // linkedInDetails.put("Education", education);
        //
        // LinkedHashMap<String, Object> skillSets = new LinkedHashMap<>();
        // int skillCount = Jsoup.parse(output).select(".pills>li").size();
        // for (int i = 0; i < skillCount - 1; i++)
        // skillSets.put("Skill " + i, Jsoup.parse(output).select(".pills>li").text());
        // linkedInDetails.put("Skills", skillSets);
        //
        // LinkedHashMap<String, Object> awards = new LinkedHashMap<>();
        // int awardCount = Jsoup.parse(output).select("#awards>ul>li").size();
        // for (int i = 0; i < awardCount; i++)
        // awards.put("Award " + i, Jsoup.parse(output).select("#awards>ul>li").text());
        // linkedInDetails.put("Awards", awards);
        //
        // LinkedHashMap<String, Object> languages = new LinkedHashMap<>();
        // int languageCount = Jsoup.parse(output).select("#languages>ul>li").size();
        // for (int i = 0; i < languageCount; i++)
        // languages.put("Language " + i,
        // Jsoup.parse(output).select("#languages>ul>li").text());
        // linkedInDetails.put("Languages", languages);
        //
        // LinkedHashMap<String, Object> projects = new LinkedHashMap<>();
        // int projectCount = Jsoup.parse(output).select("#projects>ul>li").size();
        // for (int i = 0; i < projectCount; i++)
        // skillSets
        // .put("Project " + i, Jsoup.parse(output).select("#projects>ul>li").text());
        // linkedInDetails.put("Projects", projects);
    }


    ////////////////////////////// Get Data fro Stack Overflow
    ////////////////////////////// User//////////////////////////////////

    public String getStackOverflowProfile(String firstName, String lastName, String location,
            String organisation, String email) {

        RestAssured.useRelaxedHTTPSValidation();

        LinkedHashMap<String, Object> stackUserInfo = new LinkedHashMap<String, Object>();
        String url = getLinks(firstName, lastName, location, organisation, email, "stack");

        if (url == null) {
            stackUserInfo.put("UserDetails", "Unable To Find Data on StackOverflow");
            return stackUserInfo.toString();
        }


        LinkedHashMap<String, Object> badgeDetails = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> userTopSkills = new LinkedHashMap<String, Object>();
        Map<String, Object> skillInfo = new TreeMap<String, Object>();
        List<Map<String, Object>> skillMap = new ArrayList<Map<String, Object>>();

        String outputJson = RestAssured.given().header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .get(url).body().asString();
        try {
            badgeDetails.put("goldBadgeCount", Jsoup.parse(outputJson)
                    .select(".badge1-alternate>.-total").text().replace(",", ""));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            badgeDetails.put("goldBadgeCount", "null");
        }

        try {
            badgeDetails.put("silverBadgeCount", Jsoup.parse(outputJson)
                    .select(".badge2-alternate>.-total").text().replace(",", ""));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            badgeDetails.put("silverBadgeCount", "null");
        }

        try {
            badgeDetails.put("bronzeBadgeCount", Jsoup.parse(outputJson)
                    .select(".badge3-alternate>.-total").text().replace(",", ""));


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
                skillInfo.put("Score", Jsoup.parse(outputJson).select(".g-col.-number").get(i)
                        .text().replace(",", ""));
                skillInfo.put("Posts", Jsoup.parse(outputJson).select(".g-col.-number").get(i + 1)
                        .text().replace(",", ""));
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
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ///////////////// Get Data From
    ///////////////// GitHub///////////////////////////////////////////////////////////
    public String getGitHubData(String firstName, String lastName, String location,
            String organisation, String email) {

        RestAssured.useRelaxedHTTPSValidation();

        Map<String, String> data = new LinkedHashMap<String, String>();
        String url = getLinks(firstName, lastName, location, organisation, email, "git");

        if (url == null) {
            data.put("UserDetails", "Unable To Find Data on GitHub");
            return data.toString();
        }

        ResponseBody response = RestAssured.given().log().all().get(url).getBody();


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
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
