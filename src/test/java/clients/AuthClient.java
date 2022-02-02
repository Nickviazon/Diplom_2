package clients;

import io.qameta.allure.Step;
import profile.Profile;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import profile.ProfileCredentials;

public class AuthClient extends RestAssuredClient{

    private static final String AUTH_PATH = "api/auth/";

    @Step("Register new profile with email = {profile.email} and name = {profile.name}")
    public Response registerProfileResponse(Profile profile) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.POST;
        String requestPath = AUTH_PATH + "register";
        return getResponse(specification, requestType, requestPath, profile);
    }

    @Step("Profile with email = {profile.email} login")
    public Response loginProfileResponse(Profile profile) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.POST;
        String requestPath = AUTH_PATH + "login";
        return getResponse(specification, requestType, requestPath, profile);
    }

    @Step("Update profile")
    public Response updateProfileResponse(ProfileCredentials profileCredentials, String accessToken) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.PATCH;
        String requestPath = AUTH_PATH + "user";
        if (accessToken != null) {
            specification.given().header("Authorization", accessToken);
        }
        return getResponse(specification, requestType, requestPath, profileCredentials);
    }
}
