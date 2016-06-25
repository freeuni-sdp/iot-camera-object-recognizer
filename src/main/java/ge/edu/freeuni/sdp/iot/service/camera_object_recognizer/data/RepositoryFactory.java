package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class RepositoryFactory {

    private static final String ACCOUNT_KEY =
            "+UKHsHFQUWDjoHT1S7q4Ivc1phivLmXwWESvpcRCCJwhs1BnShkaFOOQs+BmI4XWtNnyg78S6ovbD2J5QCKxsQ==";

    private static final String ACCOUNT_NAME = "freeunisdptodo";

    private static final String TABLE_NAME = "iotcamerarecognizer";

    public static Repository create() throws StorageException {
        return new CloudRepository(getTable());
    }

    private static CloudTable getTable() throws StorageException {

        final String storageConnectionString =
                String.format("DefaultEndpointsProtocol=http;AccountName=%s;AccountKey=%s", ACCOUNT_NAME, ACCOUNT_KEY);

        CloudStorageAccount storageAccount;
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
        } catch (InvalidKeyException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        CloudTableClient tableClient = storageAccount.createCloudTableClient();
        CloudTable cloudTable;
        try {
            cloudTable = tableClient.getTableReference(TABLE_NAME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        cloudTable.createIfNotExists();
        return cloudTable;
    }
}
