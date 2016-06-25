package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleApiServiceProxy {

    private static final String APPLICATION_NAME = "FreeUniSDP-IotCameraObjectRecognizer/1.0";

    private static final double TRUST_THRESHOLD = 80.;

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
        GoogleCredential credential = GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
