package example.HelperClassTUT;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TokenRunner {

    private static final String CONFIG_FILE = "src/test/resources/config.properties";
    private static final String TALENT_HUB_STAGING_URI =
            "https://talent-hub-lite-dev-server-rvq7gueufa-oa.a.run.app";
    private static final String TALENT_HUB_PRODUCTION_URI =
            "https://talent-hub-lite-prod-server-1091763260278.europe-west6.run.app";
    private static final String TALENT_HUB_SIGN_IN_PATH = "/api/v1/auth/signin";
    private static final String EGC_DEV_URI =
            "https://egc-dev-server-440410785361.europe-west6.run.app";
    private static final String EGC_LOGIN_PATH = "/api/v1/auth/login";

    public static void adminTokenProcess()  {
        String email = readValueFromConfig("admin_email");
        encryptAndSavePasswordIfNeeded("admin_encrypted_password_rc", "Roadstar1988");
        String password = readAndDecryptPassword("admin_encrypted_password_rc");
        String token = loginAndSaveAdminToken(email, password);
        String retrievedToken = readValueFromConfig("admintoken1");
        System.out.println("Retrieved Token: " + retrievedToken);
    }

    public static void adminTokenProcess2() {
        String email = readValueFromConfig("admin_email_2");
        encryptAndSavePasswordIfNeeded("admin_encrypted_password_rc_2", "123456789");
        String password = readAndDecryptPassword("admin_encrypted_password_rc_2");
        String token = loginAndSaveAdminToken2(email, password);
        String retrievedToken = readValueFromConfig("admintoken2");
        System.out.println("Retrieved Token: " + retrievedToken);
    }

    public static void InstOneTokenProcess()  {
        String email = readValueFromConfig("instructor_one_rc_mail");
        encryptAndSavePasswordIfNeeded("instOne_encrypted_password_rc", "123456789");
        String password = readAndDecryptPassword("instOne_encrypted_password_rc");
        String token = loginAndSaveInstOneToken(email, password);
        String retrievedToken = readValueFromConfig("token1");
        System.out.println("Retrieved Teoken: " + retrievedToken);
    }


    public static void corporateAdminTokenProcess()  {
        // Step 1: Read email from config
        String email = readValueFromConfig("corporate_admin_email");

        // Step 2: Encrypt and save password if not already done
        encryptAndSavePasswordIfNeeded("corporate_encrypted_password", "123456");

        // Step 3: Read and decrypt password
        String password = readAndDecryptPassword("corporate_encrypted_password");

        // Step 4: Perform login and save token
        String token = loginAndSaveCorporateAdmin(email, password);
        // Step 5: Read saved token from config
        String retrievedToken = readValueFromConfig("corporate_admin_token");
        // Step 6: Print token for verification
        System.out.println("Retrieved Token: " + retrievedToken);
    }

    public static void LearnerTokenProcess()  {
        // Step 1: Read email from config
        String email = readValueFromConfig("learner_one_rc_mail");

        // Step 2: Encrypt and save password if not already done
        encryptAndSavePasswordIfNeeded("learner_encrypted_password_rc", "123456789");

        // Step 3: Read and decrypt password
        String password = readAndDecryptPassword("learner_encrypted_password_rc");

        // Step 4: Perform login and save token
        String token = loginAndSaveLearner(email, password);
        // Step 5: Read saved token from config
        String retrievedToken = readValueFromConfig("learnertoken");
        // Step 6: Print token for verification
        System.out.println("Retrieved Token: " + retrievedToken);
    }




    public static void main(String[] args) {
        try {
//            adminTokenProcess();
//            adminTokenProcess2();
//            InstOneTokenProcess();
//            corporateAdminTokenProcess();
//            InstTwoTokenProcess();
//            LearnerTokenProcess();
//            demoTokenProcess();
            CandidategenerateStagingToken();
            generateStagingToken();
            generateStagingTokenTwo();
            generateProductionToken();
//            SADMNgenerateEGCServerDevToken();
//            ADMNgenerateEGCServerDevToken();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String readValueFromConfig(String key) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
            String value = properties.getProperty(key);
            if (value == null || value.isEmpty()) {
                throw new RuntimeException(key + " not found in config.properties");
            }
            return value;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + key + " from config.properties", e);
        }
    }

    private static void saveValueToConfig(String key, String value, String comment) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException ignored) {}

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.setProperty(key, value);
            properties.store(out, comment);
            System.out.println(key + " saved successfully to config.properties");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save " + key + " to config.properties", e);
        }
    }

    private static void encryptAndSavePasswordIfNeeded(String passwordKey, String plainPassword) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException ignored) {}

        if (properties.getProperty(passwordKey) == null || properties.getProperty(passwordKey).isEmpty()) {
            try {
                String encryptedPassword = EncryptionUtil.encrypt(plainPassword);
                saveValueToConfig(passwordKey, encryptedPassword, "Encrypted password saved");
            } catch (Exception e) {
                throw new RuntimeException("Failed to encrypt and save password", e);
            }
        }
    }

    private static String readAndDecryptPassword(String passwordKey) {
        String encryptedPassword = readValueFromConfig(passwordKey);
        try {
            return EncryptionUtil.decrypt(encryptedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt password", e);
        }
    }

    private static String loginAndSaveAdminToken(String email, String password)  {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        RequestSpecification request = RestAssured.given()
                .baseUri("https://badgewell-crm-release-app-z667sx5a2q-ey.a.run.app")
                .basePath("/api/users/login")
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString());

        Response response = request.post();

        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        String token = response.jsonPath().getString("accessToken");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig("admintoken1", token, "Saved admin token 1");
        return token;
    }

    private static String loginAndSaveAdminToken2(String email, String password)  {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        RequestSpecification request = RestAssured.given()
                .baseUri("https://badgewell-crm-release-app-z667sx5a2q-ey.a.run.app")
                .basePath("/api/users/login")
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString());

        Response response = request.post();

        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        String token = response.jsonPath().getString("accessToken");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig("admintoken2", token, "Saved admin token 2");
        return token;
    }

    private static String loginAndSaveInstOneToken(String email, String password)  {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        RequestSpecification request = RestAssured.given()
                .baseUri("https://badgewell-crm-release-app-z667sx5a2q-ey.a.run.app")
                .basePath("/api/users/login")
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString());

        Response response = request.post();

        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        String token = response.jsonPath().getString("accessToken");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig("token1", token, "Saved token 1");
        return token;
    }

    private static String loginAndSaveInstTwoToken(String email, String password)  {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        RequestSpecification request = RestAssured.given()
                .baseUri("https://badgewell-crm-release-app-z667sx5a2q-ey.a.run.app")
                .basePath("/api/users/login")
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString());

        Response response = request.post();

        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        String token = response.jsonPath().getString("accessToken");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig("token2", token, "Saved token 2");
        return token;
    }


    private static String loginAndSaveLearner(String email, String password)  {

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        RequestSpecification request = RestAssured.given()
                .baseUri("https://badgewell-crm-release-app-z667sx5a2q-ey.a.run.app")
                .basePath("/api/users/login")
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString());

        Response response = request.post();

        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        String token = response.jsonPath().getString("accessToken");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig("learnertoken", token, "Saved token 2");
        return token;

    }



    private static String loginAndSaveCorporateAdmin(String email, String password)  {

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        RequestSpecification request = RestAssured.given()
                .baseUri("https://badgewell-crm-release-app-z667sx5a2q-ey.a.run.app")
                .basePath("/api/users/login")
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString());

        Response response = request.post();

        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        String token = response.jsonPath().getString("accessToken");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig("corporate_admin_token", token, "Saved token 2");
        return token;

    }

    private static void encryptAndSavePasswordIfNeeded(String passwordKey, String plainPassword, boolean force) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException ignored) {}

        if (force || properties.getProperty(passwordKey) == null || properties.getProperty(passwordKey).isEmpty()) {
            try {
                String encryptedPassword = EncryptionUtil.encrypt(plainPassword);
                saveValueToConfig(passwordKey, encryptedPassword, "Encrypted password saved");
            } catch (Exception e) {
                throw new RuntimeException("Failed to encrypt and save password", e);
            }
        }
    }
    public static void InstTwoTokenProcess()  {
        String email = readValueFromConfig("instructor_two_rc_mail");

        // Force re-encrypt correct password to avoid reuse of Instructor One's password
        encryptAndSavePasswordIfNeeded("instTwo_encrypted_password_rc", "123456", true);

        String password = readAndDecryptPassword("instTwo_encrypted_password_rc");

        // Optional debug print — remove before committing
        System.out.println("InstTwo login => email: " + email + ", password: " + password);

        String token = loginAndSaveInstTwoToken(email, password);
        String retrievedToken = readValueFromConfig("token2");
        System.out.println("Retrieved Token: " + retrievedToken);
    }


    private static String DBGAMEloginAndSaveAdminToken(String username, String password) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        RequestSpecification request = RestAssured.given()
                .baseUri("https://www.videogamedb.uk")
                .basePath("/api/authenticate")
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString());

        Response response = request.post();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode()
                    + ", response body: " + response.getBody().asString());
        }

        String token = response.jsonPath().getString("token");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig("demoToken", token, "Saved admin token 1");
        return token;
    }


    public static void demoTokenProcess() {
        // Step 1: Define or read username from config
        String username = readValueFromConfig("videogame_admin_username");

        // Step 2: Encrypt and save password if not already done
        encryptAndSavePasswordIfNeeded("videogame_admin_encrypted_password", "admin");

        // Step 3: Read and decrypt password
        String password = readAndDecryptPassword("videogame_admin_encrypted_password");

        // Step 4: Perform login and save token
        String token = DBGAMEloginAndSaveAdminToken(username, password);

        // Step 5: Read saved token from config
        String retrievedToken = readValueFromConfig("demoToken");

        // Step 6: Print token for verification
        System.out.println("Retrieved Token: " + retrievedToken);
    }

    public static void CandidategenerateStagingToken() {
        generateTalentHubToken(
                "candemail",
                "CandIencryptedPassword",
                "123456789",
                TALENT_HUB_STAGING_URI,
                "candtoken",
                "Staging candidate token"
        );
    }

    public static void generateStagingToken() {
        generateTalentHubToken(
                "email",
                "encryptedPassword",
                "Roadstar1988",
                TALENT_HUB_STAGING_URI,
                "admintoken1",
                "Staging token"
        );
    }

    public static void generateStagingTokenTwo() {
        generateTalentHubToken(
                "admin_email_2",
                "SeconDencryptedPassword",
                "123456789",
                TALENT_HUB_STAGING_URI,
                "admintokenorg2",
                "Second staging token"
        );
    }

    public static void generateProductionToken() {
        generateTalentHubToken(
                "email",
                "prodEncryptedPassword",
                "Roadstar1988",
                TALENT_HUB_PRODUCTION_URI,
                "productionToken",
                "Production token"
        );
    }

    public static void SADMNgenerateEGCServerDevToken() {
        generateEgcToken(
                "SAEGCUserName",
                "SAEGCEncryptedPass",
                "123456789",
                "egcServerDevToken",
                "Super Admin EGC Server Dev token"
        );
    }

    public static void ADMNgenerateEGCServerDevToken() {
        generateEgcToken(
                "AEGCUserName",
                "AEGCEncryptedPass",
                "123456789",
                "AegcServerDevToken",
                "Admin EGC Server Dev token"
        );
    }

    private static void generateTalentHubToken(
            String emailKey,
            String encryptedPasswordKey,
            String plainPassword,
            String baseUri,
            String tokenKey,
            String tokenLabel
    ) {
        String email = readValueFromConfig(emailKey);
        encryptAndSavePasswordIfNeeded(encryptedPasswordKey, plainPassword);
        String password = readAndDecryptPassword(encryptedPasswordKey);
        loginAndSaveToken(email, password, baseUri, TALENT_HUB_SIGN_IN_PATH, tokenKey);
        System.out.println(tokenLabel + " generated and saved successfully");
    }

    private static String loginAndSaveToken(
            String email,
            String password,
            String baseUri,
            String path,
            String tokenKey
    ) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("userNameOrEmail", email);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .baseUri(baseUri)
                .basePath(path)
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString())
                .post();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        return extractAndSaveToken(response, "data.accessToken", tokenKey);
    }

    private static void generateEgcToken(
            String usernameKey,
            String encryptedPasswordKey,
            String plainPassword,
            String tokenKey,
            String tokenLabel
    ) {
        String username = readValueFromConfig(usernameKey);
        encryptAndSavePasswordIfNeeded(encryptedPasswordKey, plainPassword);
        String password = readAndDecryptPassword(encryptedPasswordKey);

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .baseUri(EGC_DEV_URI)
                .basePath(EGC_LOGIN_PATH)
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody.toString())
                .post();

        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
        }

        extractAndSaveToken(response, "data.accessToken", tokenKey);
        System.out.println(tokenLabel + " generated and saved successfully");
    }

    private static String extractAndSaveToken(Response response, String jsonPath, String tokenKey) {
        String token = response.jsonPath().getString(jsonPath);
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authentication token not found in the response");
        }

        saveValueToConfig(tokenKey, token, "Saved token: " + tokenKey);
        return token;
    }




}
