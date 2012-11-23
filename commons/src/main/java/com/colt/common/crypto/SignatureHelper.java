package com.colt.common.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SignatureHelper {
	private static final String MAC_ALGORITHM = "HmacSHA1";

	public static String signString(String queryString, String secretKey) throws NoSuchAlgorithmException,
			InvalidKeyException {
		Mac mac = Mac.getInstance(MAC_ALGORITHM);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), MAC_ALGORITHM);
		mac.init(secret);
		byte[] digest = mac.doFinal(queryString.toLowerCase().getBytes());

		String signature = new String(Base64.encodeBase64(digest));
		return signature;
	}
}
