package service;

import static com.oracle.jrockit.jfr.ContentType.Timestamp;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class Util {

    // Check if have access to the page
    public Boolean checkIfHavePerms(String roleId, String pages) {
        // TODO
        return true;
    }

    // Random generate welcome message
    public static String randomGenerateWelcomeMessage() {
        // Array of possible welcome messages for vets
        String[] messages = {
            "Your dedication to animals inspires us all!",
            "Every pet you help brings joy to their owners.",
            "Your compassion and expertise make a difference every day.",
            "Thank you for being a hero to our furry friends!",
            "Your commitment to healing is truly admirable."
        };

        // Generate a random index
        Random random = new Random();
        int index = random.nextInt(messages.length);

        // Return the randomly selected message
        return messages[index];
    }

    // Generate greeting based on time
    public static String generateGreeting() {
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isBefore(LocalTime.NOON)) {
            return "Good morning!";
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            return "Good afternoon!";
        } else {
            return "Good evening!";
        }
    }

    // Return current timestamp
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    // Convert timestamp to date string
    public static String timestampToDateString(Timestamp t) {
        return (t == null) ? null : new SimpleDateFormat("yyyy-MM-dd").format(t.getTime());
    }

    // Conver timestamp to date string with time
    public static String timestampToDateTimeString(Timestamp t) {
        return (t == null) ? null : new SimpleDateFormat("h:mma dd MMM yyyy").format(t.getTime());
    }

    // Convert date string with time to timetamp
    public static Timestamp dateTimeStringToTimestamp(String t) {
        try {
            return new Timestamp((new SimpleDateFormat("h:mma dd MMM yyyy").parse(t)).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Covert date string to timestamp
    public static Timestamp dateStringToTimestamp(String t) {
        try {
            return (t == null) ? null : new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(t).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Calculate coming date based on input day
    public static Timestamp addDaysToDate(Timestamp oldDate, int numberOfDay) {
        // Create a Calendar instance and add specificday
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(oldDate.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDay);

        return new Timestamp(calendar.getTimeInMillis());
    }
}
