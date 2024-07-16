package com.pampers.GenericPampersService.services;

import com.pampers.GenericPampersService.model.OutboundCallParameters;
import com.pampers.GenericPampersService.model.ServicConfigEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

public class Utility {

	
	
	public static LocalDateTime getCurrentDatetime(ServicConfigEntity serviceparam){
//		PropertyConfigurator.configure(serviceparam.getLog4jFilePath());
		LocalDateTime currentDateTime=null;
		try{
			currentDateTime=java.time.LocalDateTime.now();			
		}catch(Exception exception) {
//			logger.error(exception + Arrays.asList(exception.getStackTrace()).stream().map(Objects::toString).collect(Collectors.joining("\n")));
		}
		return currentDateTime;
	}
	
	
	public static String encrypt(ServicConfigEntity serviceparam,String strToEncrypt){
//		PropertyConfigurator.configure(serviceparam.getLog4jFilePath());		
	    try{
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(serviceparam.getSecretKey().toCharArray(), serviceparam.getSalt().getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);	       
	        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
	    }catch(Exception exception) {
//			logger.error(exception + Arrays.asList(exception.getStackTrace()).stream().map(Objects::toString).collect(Collectors.joining("\n")));
		}
	    return null;
	}
	
	
	
	public static String decrypt(ServicConfigEntity serviceparam,String strToDecrypt) {
		//PropertyConfigurator.configure(serviceparam.getLog4jFilePath());
	    try{
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(serviceparam.getSecretKey().toCharArray(), serviceparam.getSalt().getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
	        
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
	        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	    }catch(Exception exception) {
			//logger.error(exception + Arrays.asList(exception.getStackTrace()).stream().map(Objects::toString).collect(Collectors.joining("\n")));
		}
	    return null;
	}
}
