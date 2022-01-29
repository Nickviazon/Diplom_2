package clients.Profile;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProfileDirector {

    private List<String> randomDataForProfile;

    public void buildFullProfile(Builder profileBuilder) {
        randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(3).collect(Collectors.toList());
        profileBuilder.setEmail(randomDataForProfile.get(0)+"@testemail.ru");
        profileBuilder.setPassword(randomDataForProfile.get(1));
        profileBuilder.setName(randomDataForProfile.get(2));
    }

    public void buildProfileWithoutEmail(Builder profileBuilder) {
       randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(2).collect(Collectors.toList());
        profileBuilder.setEmail(null);
        profileBuilder.setPassword(randomDataForProfile.get(0));
        profileBuilder.setName(randomDataForProfile.get(1));
    }

    public void buildProfileWithoutPassword(Builder profileBuilder) {
        randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(2).collect(Collectors.toList());

        profileBuilder.setEmail(randomDataForProfile.get(0));
        profileBuilder.setPassword(null);
        profileBuilder.setName(randomDataForProfile.get(1));
    }

    public void buildProfileWithoutName(Builder profileBuilder) {
        randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(2).collect(Collectors.toList());
        profileBuilder.setEmail(randomDataForProfile.get(0));
        profileBuilder.setPassword(randomDataForProfile.get(1));
        profileBuilder.setName(null);
    }
}
