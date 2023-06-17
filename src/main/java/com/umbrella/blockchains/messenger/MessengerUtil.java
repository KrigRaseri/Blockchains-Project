package com.umbrella.blockchains.messenger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessengerUtil {
    /**
     * Generates a random chat message.
     *
     * @return The random chat message.
     */
    public static String getRandomMessage() {
        List<String> randomWords = new ArrayList<>(Arrays.asList("Tom: Hey, I'm first!",
                "Sarah: It's not fair!", "Sarah: You always will be first because it is your blockchain!",
                "Sarah: Anyway, thank you for this amazing chat.", "Tom: You're welcome :)",
                "Nick: Hey Tom, nice chat"));

        return randomWords.get(ThreadLocalRandom.current().nextInt(randomWords.size()));
    }
}
