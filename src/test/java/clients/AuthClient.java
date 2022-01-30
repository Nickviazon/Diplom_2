package clients;

import profile.Profile;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthClient extends RestAssuredClient{

    private static final String AUTH_PATH = "api/auth/";
    private String accessToken;

    public String getAccessToken(Profile Profile) {
        return accessToken;
    }

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

    
}
