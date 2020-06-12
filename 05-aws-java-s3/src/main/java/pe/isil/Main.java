package pe.isil;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        System.out.println("AWS - S3");

        AWSCredentials credentials = new BasicAWSCredentials("AKIAQKS6V3RVKL4LAWEB",
                "3zgvuwLu5qaFkmdHcDWxN3Ejs5dB2kvdP7Y+bjpE");

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_WEST_1)
                .build();

        final String BUCKET_NAME = "a-marco-soto";

        //(1) Create Bucket
        if( !s3Client.doesBucketExistV2(BUCKET_NAME) ){
            s3Client.createBucket(BUCKET_NAME);
            System.out.println("se ha creado el bucket"+ BUCKET_NAME);
        }else{
            System.out.println("el bucket ya existe ");
        }

        //(2) List Buckets
//        List<Bucket> buckets = s3Client.listBuckets();
//        for (Bucket bucket : buckets) {
//            System.out.println("bucket => " + bucket);
//        }

        //(3) Upload file
//        s3Client.putObject(BUCKET_NAME, "java/apuntes.txt", new File("src/main/resources/data/notas.txt"));
//        System.out.println("se ha subido el archivo");

//        //(3.1) Multi Upload file
        Map<String, String> mapa = new HashMap();
        mapa.put("java/apuntes.txt","src/main/resources/data/notas.txt");
        mapa.put("java/apuntes1.txt","src/main/resources/data/notas1.txt");

        for ( Map.Entry<String, String> entry : mapa.entrySet()) {
            s3Client.putObject(BUCKET_NAME, entry.getKey(), new File(entry.getValue()));
        }
        System.out.println("se ha subido el archivo");


        //(4) Download file
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, "java/apuntes.txt");
        S3ObjectInputStream objectContent = s3Object.getObjectContent();

        try {
            FileUtils.copyInputStreamToFile(objectContent, new File("src/main/resources/input/apuntes.txt"));
            System.out.println("el archivo se ha descargado satisfactoriamente!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //(5) Delete object
//        s3Client.deleteObject(BUCKET_NAME, "java/apuntes.txt");
//        System.out.println("el archivo se ha borrado");

        for ( Map.Entry<String, String> entry : mapa.entrySet()) {
            s3Client.deleteObject(BUCKET_NAME, entry.getKey());
        }
        System.out.println("el archivo se ha borrado");

        //(5) Delete bucket
        s3Client.deleteBucket(BUCKET_NAME);
        System.out.println("el bucket se ha borrado");

    }

}
