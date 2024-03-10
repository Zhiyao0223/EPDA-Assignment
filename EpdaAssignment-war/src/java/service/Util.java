package service;

import java.time.LocalTime;
import java.util.Random;

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

}
