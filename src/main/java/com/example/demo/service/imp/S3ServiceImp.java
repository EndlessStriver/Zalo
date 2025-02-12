package com.example.demo.service.imp;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.S3Service;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.EncryptionTypeMismatchException;
import software.amazon.awssdk.services.s3.model.InvalidRequestException;
import software.amazon.awssdk.services.s3.model.InvalidWriteOffsetException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.TooManyPartsException;

@Service
public class S3ServiceImp implements S3Service {

	private S3Client s3Client;
	private final String BUCKET_NAME = "zalo-s3-bucket";

	public S3ServiceImp(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public String uploadFile(MultipartFile file)
			throws InvalidRequestException, InvalidWriteOffsetException, TooManyPartsException,
			EncryptionTypeMismatchException, S3Exception, AwsServiceException, SdkClientException, IOException {
		String fileName = UUID.randomUUID().toString();
		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(BUCKET_NAME).key(fileName)
				.contentType(file.getContentType()).build();
		s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

		return "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + fileName;
	}

	public void deleteFile(String fileUrl) {
		String fileName = getFileNameFromUrl(fileUrl);
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(fileName)
				.build();
		s3Client.deleteObject(deleteObjectRequest);
	}
	
	private String getFileNameFromUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
}
