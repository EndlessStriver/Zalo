package com.example.demo.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.EncryptionTypeMismatchException;
import software.amazon.awssdk.services.s3.model.InvalidRequestException;
import software.amazon.awssdk.services.s3.model.InvalidWriteOffsetException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.TooManyPartsException;

public interface S3Service {

	public String uploadFile(MultipartFile file)
			throws InvalidRequestException, InvalidWriteOffsetException, TooManyPartsException,
			EncryptionTypeMismatchException, S3Exception, AwsServiceException, SdkClientException, IOException;

	public void deleteFile(String fileName);

}
