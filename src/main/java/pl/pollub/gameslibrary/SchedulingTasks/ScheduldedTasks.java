package pl.pollub.gameslibrary.SchedulingTasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduldedTasks {

    // cron second(0-59) minute(0-59) hour(0-59) day_of_the_month(1-31) month(1-12) day_of_the_week(0-6)

    // execute at 08.00 AM every day
    @Scheduled(cron = "0 0 8 * * *")
    public void test() {
        System.out.println("test");
    }
}
