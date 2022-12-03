package pl.pollub.gameslibrary.Services;


import org.hibernate.exception.DataException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public interface SteamApiService {

    String appListURL = "http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json";
    String appDetailsURL = "https://store.steampowered.com/api/appdetails?appids=";

    static void pullFromApi(AppService appService, String mode) throws IOException, InterruptedException, JSONException {
        System.out.println("SteamApiService: Pull procedure started. Mode: '" + mode + "'.");
        if(Objects.equals(mode, "PULL_NEW_ONLY") || Objects.equals(mode, "FULL_UPDATE")) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest appListRequests = HttpRequest.newBuilder().uri(URI.create(appListURL)).build();
                HttpResponse<String> appListResponse = null;

                boolean shouldWaitForAccess = true;
                boolean infoShown = false;
                while(shouldWaitForAccess) {
                    appListResponse = client.send(appListRequests, HttpResponse.BodyHandlers.ofString());
                    if(appListResponse.statusCode() != 200) {
                        if(!infoShown) {
                            System.out.println("SteamApiService: Public Steam API Request responseCode: " + appListResponse.statusCode() + " -> too many requests. Only 200 requests per 5 mins allowed. Waiting for access...");
                            infoShown = true;
                        }
                        TimeUnit.MILLISECONDS.sleep(3000);
                    }
                    else { shouldWaitForAccess = false; }
                }

                JSONObject appListObject = new JSONObject(appListResponse.body());
                appListObject = appListObject.getJSONObject("applist");
                JSONArray appListArray = appListObject.getJSONArray("apps");
                for (int i = 0; i < appListArray.length(); i++) {
                    Object appListArrayElement = appListArray.get(i);
                    JSONObject myJSONObject = (JSONObject) appListArrayElement;
                    Long currentAppId = myJSONObject.getLong("appid");
                    String currentAppName = myJSONObject.getString("name");

                    if (currentAppName.length()!=0) {
                        App existingApp = appService.getById(currentAppId);
                        if(existingApp != null && Objects.equals(mode, "PULL_NEW_ONLY")) {
                            System.out.println("SteamApiService: Skipping existing app with id:" + currentAppId);
                        } else {
                            HttpRequest appDetailsRequest = HttpRequest.newBuilder().uri(URI.create(appDetailsURL + currentAppId)).build();
                            HttpResponse<String> appDetailsResponse = appListResponse;	// not right

                            shouldWaitForAccess = true;
                            infoShown = false;
                            while(shouldWaitForAccess) {
                                appDetailsResponse = client.send(appDetailsRequest, HttpResponse.BodyHandlers.ofString());
                                if(appDetailsResponse.statusCode() != 200) {
                                    if(!infoShown) {
                                        System.out.println("SteamApiService: Public Steam API Request responseCode: " + appDetailsResponse.statusCode() + " -> too many requests. Only 200 requests per 5 mins allowed. Waiting for access...");
                                        infoShown = true;
                                    }
                                    TimeUnit.MILLISECONDS.sleep(3000);
                                }
                                else { shouldWaitForAccess = false; }
                            }

                            try {
                                JSONObject appDetailsObject = new JSONObject(appDetailsResponse.body());
                                JSONObject appDetails = appDetailsObject.getJSONObject(myJSONObject.getString("appid"));
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
                                    app.setId(currentAppId);
                                    app.setTitle(title);
                                    app.setType(type);
                                    app.setDescription(description);
                                    app.setShortDescription(shortDescription);
                                    app.setScreenshotLink(screenshotLink);
                                    app.setDevelopers(developers.isEmpty() ? developers : " ");
                                    app.setPublishers(publishers.isEmpty() ? publishers : " ");

                                    if (existingApp == null) {
                                        if(Objects.equals(type, "game") || Objects.equals(type, "dlc") || Objects.equals(type, "demo")) { // move it up
                                            System.out.println("SteamApiService: Adding new app: appId: " + currentAppId + " | name: " + currentAppName);
                                            appService.add(app);
                                        }
                                        else{
                                            System.out.println("SteamApiService: App is neither ot type 'game', 'dlc', nor 'demo' -> skipping.");
                                        }
                                    }
                                    else if(Objects.equals(mode, "FULL_UPDATE")){
                                        System.out.println("SteamApiService: Updating an existing app: appId: " + currentAppId + " | name: " + currentAppName);
                                        appService.edit(app, currentAppId);
                                    }
                                }
                            }
                            catch (JSONException exception) {
                                System.out.println("SteamApiService Exception: " + exception.getMessage());
                            }
                        }
                    }
                }
            } catch (DataException exception) {
                System.out.println("SteamApiService Exception: " + exception.getMessage());
            }
        } else
            System.out.println("SteamApiService: Incorrect mode '" + mode + "' for pullFromApi() method.");
        System.out.println("SteamApiService: Pull procedure ended.");
    }
}
