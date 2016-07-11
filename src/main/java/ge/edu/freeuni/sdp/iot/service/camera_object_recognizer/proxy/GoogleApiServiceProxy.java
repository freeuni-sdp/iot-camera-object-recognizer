package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


public class GoogleApiServiceProxy {

    private static final String APPLICATION_NAME = "FreeUniSDP-IotCameraObjectRecognizer/1.0";

    private static final double TRUST_THRESHOLD = .80;

    private static final String GOOGLE_PROJECT_ID = "GOOGLE_PROJECT_ID";

    private static final String GOOGLE_PRIVATE_KEY_ID = "GOOGLE_PRIVATE_KEY_ID";

    private static final String GOOGLE_PRIVATE_KEY = "GOOGLE_PRIVATE_KEY";

    private static final String GOOGLE_CLIENT_EMAIL = "GOOGLE_CLIENT_EMAIL";

    private static final String GOOGLE_CLIENT_ID = "GOOGLE_CLIENT_ID";

    private static final String GOOGLE_AUTH_URI = "GOOGLE_AUTH_URI";

    private static final String GOOGLE_TOKEN_URI = "GOOGLE_TOKEN_URI";

    private static final String GOOGLE_AUTH_PROVIDER_X509_CERT_URL = "GOOGLE_AUTH_PROVIDER_X509_CERT_URL";

    private static final String GOOGLE_CLIENT_X509_CERT_URL = "GOOGLE_CLIENT_X509_CERT_URL";


    private final ServiceState state;


    public GoogleApiServiceProxy(ServiceState state) {
        this.state = state;
    }

    public List<String> getObjectList(byte[] imageData, int maxResults) throws IOException, GeneralSecurityException {
        List<String> objects = new ArrayList<>();
        for (EntityAnnotation label : labelImage(imageData, maxResults)) {
            if (label.getScore() >= TRUST_THRESHOLD) {
                objects.add(label.getDescription());
            }
        }

        return objects;
    }

    private List<EntityAnnotation> labelImage(byte[] imageData, int maxResults) throws IOException, GeneralSecurityException {
        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(imageData))
                        .setFeatures(ImmutableList.of(
                                new Feature()
                                        .setType("LABEL_DETECTION")
                                        .setMaxResults(maxResults)));
        Vision.Images.Annotate annotate = getVisionService()
                .images()
                .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));

        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 1;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        if (response.getLabelAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                            ? response.getError().getMessage()
                            : "Unknown error getting image annotations");
        }

        return response.getLabelAnnotations();
    }

    private static Vision getVisionService() throws IOException, GeneralSecurityException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = getCredential(jsonFactory, httpTransport);

        return new Vision.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static GoogleCredential getCredential(JsonFactory jsonFactory, NetHttpTransport httpTransport) throws IOException {
//        return GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        return GoogleCredential.fromStream(getCredentialStream(), httpTransport, jsonFactory)
                .createScoped(VisionScopes.all());
    }

    private static InputStream getCredentialStream() throws IOException {
        JSONObject json = getCredentialJson();
        return new ByteArrayInputStream(json.toString().getBytes(Charset.defaultCharset()));
    }

    private static JSONObject getCredentialJson() throws IOException {
        JSONObject json = new JSONObject();
        json.put("type", "service_account");
        json.put("project_id", getEnv(GOOGLE_PROJECT_ID));
        json.put("private_key_id", getEnv(GOOGLE_PRIVATE_KEY_ID));
        json.put("private_key", getEnv(GOOGLE_PRIVATE_KEY).replace("\\n", "\n"));
        json.put("client_email", getEnv(GOOGLE_CLIENT_EMAIL));
        json.put("client_id", getEnv(GOOGLE_CLIENT_ID));
        json.put("auth_uri", getEnv(GOOGLE_AUTH_URI));
        json.put("token_uri", getEnv(GOOGLE_TOKEN_URI));
        json.put("auth_provider_x509_cert_url", getEnv(GOOGLE_AUTH_PROVIDER_X509_CERT_URL));
        json.put("client_x509_cert_url", getEnv(GOOGLE_CLIENT_X509_CERT_URL));
        return json;
    }

    private static String getEnv(String envVarName) throws IOException {
        String envVar = System.getenv(envVarName);
        if (envVar == null)
            throw new IOException(envVarName + "is not specified.");

        return envVar;
    }
}
