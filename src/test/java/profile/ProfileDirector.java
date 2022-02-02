package profile;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProfileDirector {

    private List<String> randomDataForProfile;

    private void buildFullProfile(Builder profileBuilder) {
        randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(3).collect(Collectors.toList());
        profileBuilder.setEmail(randomDataForProfile.get(0).toLowerCase()+"@testemail.ru");
        profileBuilder.setPassword(randomDataForProfile.get(1));
        profileBuilder.setName(randomDataForProfile.get(2));
    }

    private void buildProfileWithoutEmail(Builder profileBuilder) {
       randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(2).collect(Collectors.toList());
        profileBuilder.setEmail(null);
        profileBuilder.setPassword(randomDataForProfile.get(0));
        profileBuilder.setName(randomDataForProfile.get(1));
    }

    private void buildProfileWithoutPassword(Builder profileBuilder) {
        randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(2).collect(Collectors.toList());

        profileBuilder.setEmail(randomDataForProfile.get(0).toLowerCase()+"@testemail.ru");
        profileBuilder.setPassword(null);
        profileBuilder.setName(randomDataForProfile.get(1));
    }

    private void buildProfileWithoutName(Builder profileBuilder) {
        randomDataForProfile = Stream
                .generate(() -> RandomStringUtils.randomAlphabetic(10))
                .limit(2).collect(Collectors.toList());
        profileBuilder.setEmail(randomDataForProfile.get(0).toLowerCase()+"@testemail.ru");
        profileBuilder.setPassword(randomDataForProfile.get(1));
        profileBuilder.setName(null);
    }

    @Step("New {profileType} profile created")
    public void buildProfile(Builder profileBuilder, ProfileType profileType) throws IllegalArgumentException {
        switch (profileType) {
            case FULL:
                buildFullProfile(profileBuilder);
                break;
            case WITHOUT_EMAIL:
                buildProfileWithoutEmail(profileBuilder);
                break;
            case WITHOUT_PASSWORD:
                buildProfileWithoutPassword(profileBuilder);
                break;
            case WITHOUT_NAME:
                buildProfileWithoutName(profileBuilder);
                break;
            default:
                throw new IllegalArgumentException("Wrong ProfileType was passed for build Profile");
        }
    }
}
