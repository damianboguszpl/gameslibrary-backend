package pl.pollub.gameslibrary.SchedulingTasks;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.pollub.gameslibrary.Services.AppService;
import pl.pollub.gameslibrary.Services.SteamApiService;

import java.io.IOException;

@Component
public class ScheduledTasks {
    @Autowired
    AppService appService;
    // cron second(0-59) minute(0-59) hour(0-59) day_of_the_month(1-31) month(1-12) day_of_the_week(0-6)

//    // execute at 08.00 AM every day
//    @Scheduled(cron = "0 0 8 * * *")
//    public void test() {
//        System.out.println("test");
//    }

    // execute at 16.00 PM every day
    @Scheduled(cron = "0 13 16 * * *")
    public void pullNewAppFromSteamApi() throws JSONException, IOException, InterruptedException {
        SteamApiService.pullFromApi(appService, "PULL_NEW_ONLY");
    }

    // execute at 02.00 AM every day
    @Scheduled(cron = "0 0 2 * * *")
    public void pullNewAndUpdateAppFromSteamApi() throws JSONException, IOException, InterruptedException {
        SteamApiService.pullFromApi(appService, "FULL_UPDATE");
    }


}
