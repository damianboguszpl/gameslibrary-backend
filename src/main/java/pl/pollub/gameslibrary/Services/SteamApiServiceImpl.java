package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SteamApiServiceImpl implements SteamApiService {
    @Autowired
    static AppService appService;

    static String appListURL = "http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json";
    static String appDetailsURL = "https://store.steampowered.com/api/appdetails?appids=";
}
