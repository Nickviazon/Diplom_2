package clients;

import profile.Profile;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import profile.ProfileCredentials;

public class AuthClient extends RestAssuredClient{

    private static final String AUTH_PATH = "api/auth/";

    public Response registerProfileResponse(Profile profile) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.POST;
        String requestPath = AUTH_PATH + "register";
        return getResponse(specification, requestType, requestPath, profile);
    }

    public Response loginProfileResponse(Profile profile) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.POST;
        String requestPath = AUTH_PATH + "login";
        return getResponse(specification, requestType, requestPath, profile);
    }

    public Response updateProfileResponse(ProfileCredentials profileCredentials) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.PUT;
        String requestPath = AUTH_PATH + "user";
        return getResponse(specification, requestType, requestPath, profileCredentials);
    }
}
