package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleApiServiceProxy {

    private static final String APPLICATION_NAME = "FreeUniSDP-IotCameraObjectRecognizer/1.0";

    private static final double TRUST_THRESHOLD = 80.;

    private static final String GOOGLE_CLIENT_ID = "GOOGLE_CLIENT_ID";

    private static final String GOOGLE_CLIENT_SECRET = "GOOGLE_CLIENT_SECRET";

    private static final String GOOGLE_REFRESH_TOKEN = "GOOGLE_REFRESH_TOKEN";

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
        GoogleCredential credential = getCredentials(jsonFactory, httpTransport);

        return new Vision.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static GoogleCredential getCredentials(JsonFactory jsonFactory, NetHttpTransport httpTransport) throws IOException {
        // return GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        String clientId = getEnv(GOOGLE_CLIENT_ID);
        String clientSecret = getEnv(GOOGLE_CLIENT_SECRET);
        String refreshToken = getEnv(GOOGLE_REFRESH_TOKEN);

        return new GoogleCredential.Builder()
                .setJsonFactory(jsonFactory)
                .setTransport(httpTransport)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);
    }

    private static String getEnv(String envVarName) throws IOException {
        String envVar = System.getenv(envVarName);
        if (envVar == null)
            throw new IOException(envVarName + "is not specified.");

        return envVar;
    }
}
