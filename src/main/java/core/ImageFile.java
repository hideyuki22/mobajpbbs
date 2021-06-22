package core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;



public class ImageFile {

	private static  String ENDPOINT_URL;
	private static  String AWS_REGION;
	private static  String AWS_ACCESS_KEY_ID;
	private static  String AWS_SECRET_ACCESS_KEY;
	private static String AWS_BACKET_NAME;

	public static String getENDPOINT_URL() {
		return ENDPOINT_URL;
	}

	public static void setENDPOINT_URL(String eNDPOINT_URL) {
		ENDPOINT_URL = eNDPOINT_URL;
	}

	public static String getAWS_REGION() {
		return AWS_REGION;
	}

	public static void setAWS_REGION(String aWS_REGION) {
		AWS_REGION = aWS_REGION;
		ENDPOINT_URL = "https://s3-" + AWS_REGION + ".amazonaws.com";
	}

	public static String getAWS_ACCESS_KEY_ID() {
		return AWS_ACCESS_KEY_ID;
	}

	public static void setAWS_ACCESS_KEY_ID(String aWS_ACCESS_KEY_ID) {
		AWS_ACCESS_KEY_ID = aWS_ACCESS_KEY_ID;
	}

	public static String getAWS_SECRET_ACCESS_KEY() {
		return AWS_SECRET_ACCESS_KEY;
	}

	public static void setAWS_SECRET_ACCESS_KEY(String aWS_SECRET_ACCESS_KEY) {
		AWS_SECRET_ACCESS_KEY = aWS_SECRET_ACCESS_KEY;
	}

	public static String getAWS_BACKET_NAME() {
		return AWS_BACKET_NAME;
	}

	public static void setAWS_BACKET_NAME(String aWS_BACKET_NAME) {
		AWS_BACKET_NAME = aWS_BACKET_NAME;

	}

	public static String GenerateFileName(int id, String filename) {
		String extension = filename.substring(filename.lastIndexOf("."));
		System.out.println("拡張子:" + extension);
		Date date = new Date();
		return String.valueOf(id) + String.valueOf(date.getTime()) + extension;
	}

	public static boolean CheckExtension(String filename) {
		String extension = filename.substring(filename.lastIndexOf(".")).toUpperCase();
		//許可する拡張子リスト
		List<String> ExtensionList = Arrays.asList(".JPG", ".JPEG", ".PNG", ".GIF", ".BMP", ".TIFF");
		return ExtensionList.contains(extension);
	}

	public static boolean CheckImage(String path) {
		File file = new File(path);
		if (file != null && file.isFile()) {
			String filename = file.getName();

			try {
				BufferedImage bi = ImageIO.read(file);
				if (CheckExtension(filename) && bi != null) {
					return true;
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}

		return false;
	}

	public static boolean DeleteImage(String path) {
		File file = new File(path);
		//ファイルが存在すれば削除
		if (file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}

	}
	public static boolean DeleteImageFile(String fileName) {
		// 認証情報
		try {
	        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);

			 // クライアント設定
	        ClientConfiguration clientConfig = new ClientConfiguration();
	        clientConfig.setProtocol(Protocol.HTTPS);  // プロトコル
	        clientConfig.setConnectionTimeout(10000);   // 接続タイムアウト(ms)

	        // エンドポイント設定
	        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(ENDPOINT_URL, AWS_REGION);

	        // クライアント生成
	        AmazonS3 client = AmazonS3ClientBuilder.standard()
	                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
	                        .withEndpointConfiguration(endpointConfiguration).build();
	        client.deleteObject(AWS_BACKET_NAME,fileName);
	        return true;
		}catch(AmazonServiceException e) {
		    e.printStackTrace();
		    System.out.println("DeleteImageFile");
		    return false;
		}
	}


	public static boolean UploadFile(File file) {
		try {
			// 認証情報
	        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);

			 // クライアント設定
	        ClientConfiguration clientConfig = new ClientConfiguration();
	        clientConfig.setProtocol(Protocol.HTTPS);  // プロトコル
	        clientConfig.setConnectionTimeout(10000);   // 接続タイムアウト(ms)

	     // エンドポイント設定
	        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(ENDPOINT_URL, AWS_REGION);

	        // クライアント生成
	        AmazonS3 client = AmazonS3ClientBuilder.standard()
	                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
	                        .withEndpointConfiguration(endpointConfiguration).build();

			// ファイルをアップロード
			client.putObject(
			    // アップロード先バケット名
					AWS_BACKET_NAME,
			    // アップロード先のパス（キー名）
			    file.getName(),
			    // ファイルの実体
			    file
			);
			return true;
		}catch(AmazonServiceException e) {
		    e.printStackTrace();
		    System.out.println("UploadFile");
		    return false;
		}

	}

}