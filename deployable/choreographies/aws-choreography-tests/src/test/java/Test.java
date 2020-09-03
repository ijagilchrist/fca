import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class Test {

    public static void main(String args[]) {

        String objectUUID = "002-Audio-mp3";
        String sourceBucket = "org.fca.s3.test.reception";

        String prefix = String.format("%s/%s/",objectUUID,"derived");

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.EU_WEST_2)
                .build();
        ListObjectsRequest listRequest = new ListObjectsRequest()
                .withBucketName(sourceBucket)
                .withPrefix(prefix)
                .withDelimiter("/");
        ObjectListing items = s3.listObjects(listRequest);
        for (S3ObjectSummary object: items.getObjectSummaries()) {
            System.out.println(object.getKey());
        }

    }
}
