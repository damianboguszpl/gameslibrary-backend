package pl.pollub.gameslibrary.Services;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

@Service
public class SteamApiService {
    @Autowired
    static AppService appService;

    static String appListURL = "http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json";
    static String appDetailsURL = "https://store.steampowered.com/api/appdetails?appids=";

    public static void rebase(AppService appServiceO) throws IOException, InterruptedException, JSONException {
        appService = appServiceO;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest appListRequests = HttpRequest.newBuilder().uri(URI.create(appListURL)).build();

        HttpResponse<String> appListResponse = client.send(appListRequests, HttpResponse.BodyHandlers.ofString());
        JSONObject appListObject = new JSONObject(appListResponse.body());
        appListObject = appListObject.getJSONObject("applist");
        JSONArray appListArray = appListObject.getJSONArray("apps");

        for (int i = 0; i < appListArray.length(); i++) {
            Object appListArrayElement = appListArray.get(i);
            JSONObject myJSONObject = (JSONObject) appListArrayElement;
            Long id = myJSONObject.getLong("appid");
            String name = myJSONObject.getString("name");

            System.out.println(i +"> appId: " + id + " & name: " + name);

            if (name.length()!=0) {
                HttpRequest appDetailsRequest = HttpRequest.newBuilder().uri(URI.create(appDetailsURL + id)).build();
                HttpResponse<String> appDetailsResponse = appListResponse;	// not right

                boolean goOn = false;
                while(!goOn) {
                    appDetailsResponse = client.send(appDetailsRequest, HttpResponse.BodyHandlers.ofString());
                    if(appDetailsResponse.statusCode() != 200) {
                        System.out.println(appDetailsResponse.statusCode() + "... too many requests... waiting 3s");
                        TimeUnit.MILLISECONDS.sleep(3000);
                    }
                    else { goOn = true; }
                }

                JSONObject appDetailsObject = new JSONObject(appDetailsResponse.body());
                JSONObject appDetails = appDetailsObject.getJSONObject(myJSONObject.getString("appid"));
                try {
                    boolean success = appDetails.getBoolean(("success"));
                    if (success)
                    {
                        appDetails = appDetails.getJSONObject("data");

                        String title = appDetails.getString("name");
                        String type = appDetails.getString("type");
                        String description = appDetails.getString("detailed_description");
                        String shortDescription = appDetails.getString("short_description");
                        String screenshotLink = appDetails.getString("header_image");
                        String developers = appDetails.getString("developers");
                        String publishers = appDetails.getString("publishers");

                        App app = new App();
                        app.setId(id);
                        app.setTitle(title);
                        app.setType(type);
                        app.setDescription(description);
                        app.setShortDescription(shortDescription);
                        app.setScreenshotLink(screenshotLink);
                        app.setDevelopers(developers);
                        app.setPublishers(publishers);
//                        System.out.println(app);

                        App existingApp = appService.findById(id);
                        if (existingApp == null) {
                            System.out.println("app to be added");
                            appService.add(app);
                        }
                        else {
                            System.out.println("app to be updated");
                            appService.edit(app, id);
                        }
                    }
                }
                catch (JSONException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    public static void pullNewApps(AppService appServiceO) throws IOException, InterruptedException, JSONException {
        appService = appServiceO;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest appListRequests = HttpRequest.newBuilder().uri(URI.create(appListURL)).build();

        HttpResponse<String> appListResponse = client.send(appListRequests, HttpResponse.BodyHandlers.ofString());
        JSONObject appListObject = new JSONObject(appListResponse.body());
        appListObject = appListObject.getJSONObject("applist");
        JSONArray appListArray = appListObject.getJSONArray("apps");

        for (int i = 0; i < appListArray.length(); i++) {
            Object appListArrayElement = appListArray.get(i);
            JSONObject myJSONObject = (JSONObject) appListArrayElement;
            Long id = myJSONObject.getLong("appid");
            String name = myJSONObject.getString("name");

            System.out.println(i +"> appId: " + id + " & name: " + name);

            if (name.length()!=0) {
                App existingApp = appService.findById(id);
                if (existingApp == null) {
                    HttpRequest appDetailsRequest = HttpRequest.newBuilder().uri(URI.create(appDetailsURL + id)).build();
                    HttpResponse<String> appDetailsResponse = appListResponse;	// not right

                    boolean goOn = false;
                    while(!goOn) {
                        appDetailsResponse = client.send(appDetailsRequest, HttpResponse.BodyHandlers.ofString());
                        if(appDetailsResponse.statusCode() != 200) {
                            System.out.println(appDetailsResponse.statusCode() + "... too many requests... waiting 3s");
                            TimeUnit.MILLISECONDS.sleep(3000);
                        }
                        else { goOn = true; }
                    }

                    JSONObject appDetailsObject = new JSONObject(appDetailsResponse.body());
                    JSONObject appDetails = appDetailsObject.getJSONObject(myJSONObject.getString("appid"));
                    try {
                        boolean success = appDetails.getBoolean(("success"));
                        if (success)
                        {
                            appDetails = appDetails.getJSONObject("data");

                            String title = appDetails.getString("name");
                            String type = appDetails.getString("type");
                            String description = appDetails.getString("detailed_description");
                            String shortDescription = appDetails.getString("short_description");
                            String screenshotLink = appDetails.getString("header_image");
                            String developers = appDetails.getString("developers");
                            String publishers = appDetails.getString("publishers");

                            App app = new App();
                            app.setId(id);
                            app.setTitle(title);
                            app.setType(type);
                            app.setDescription(description);
                            app.setShortDescription(shortDescription);
                            app.setScreenshotLink(screenshotLink);
                            app.setDevelopers(developers);
                            app.setPublishers(publishers);
//                            System.out.println(app);

                            System.out.println("app to be added");
                            appService.add(app);
                        }
                    }
                    catch (JSONException exception) {
                         System.out.println(exception.getMessage());
                    }
                }
                else {
                    System.out.println("app to be skipped");
                }
            }
        }
    }
}
