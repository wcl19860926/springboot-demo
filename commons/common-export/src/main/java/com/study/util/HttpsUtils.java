package com.study.util;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HttpsUtils {
    public HttpsUtils() {
    }

    public static SSLSocketFactory buildJavaSSLSocketFactory(String certPath) throws SSLException {
        String trustCertPath = certPath != null ? certPath : getTrustCertPath();
        String truststoreFile = getTruststoreFile();
        if (trustCertPath == null && truststoreFile == null) {
            return null;
        } else {
            try {
                String secureSocketProtocol = getSecureSocketProtocol();
                SSLContext sc = SSLContext.getInstance(secureSocketProtocol);
                TrustManager[] trustManagers = null;
                if (trustCertPath != null) {
                    trustManagers = buildCertTrustManager(trustCertPath);
                } else if (truststoreFile != null) {
                    trustManagers = buildTrustStoreTrustManager();
                }

                sc.init((KeyManager[])null, trustManagers, new SecureRandom());
                return sc.getSocketFactory();
            } catch (Exception var6) {
                throw new SSLException("Https buildSSLSocketFactory error ", var6);
            }
        }
    }

    private static TrustManager[] buildTrustStoreTrustManager() throws SSLException {
        try {
            String truststoreType = getTruststoreType();
            String truststoreFile = getTruststoreFile();
            String truststorePassword = getTruststorePassword();
            String truststoreAlgorithm = getTruststoreAlgorithm();
            KeyStore keyStore = KeyStore.getInstance(truststoreType);
            keyStore.load(new FileInputStream(truststoreFile), truststorePassword.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(truststoreAlgorithm);
            tmf.init(keyStore);
            return tmf.getTrustManagers();
        } catch (Exception var6) {
            throw new SSLException(var6);
        }
    }

    private static TrustManager[] buildCertTrustManager(String trustCertPath) throws SSLException {
        FileInputStream in = null;

        try {
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory selfTmf = TrustManagerFactory.getInstance(algorithm);
            KeyStore trustKeyStore = KeyStore.getInstance("JKS");
            trustKeyStore.load((InputStream)null, (char[])null);
            in = new FileInputStream(trustCertPath);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certs = cf.generateCertificates(in);
            int count = 0;
            Iterator i$ = certs.iterator();

            while(i$.hasNext()) {
                Certificate cert = (Certificate)i$.next();
                trustKeyStore.setCertificateEntry("cert-" + count++, cert);
            }

            selfTmf.init(trustKeyStore);
            TrustManager[] trustManagers = selfTmf.getTrustManagers();
            TrustManager[] var10 = trustManagers;
            return var10;
        } catch (Exception var14) {
            throw new SSLException(var14);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static String getTrustCertPath() {
        return System.getProperty("aliyun.sdk.ssl.trustCertPath");
    }

    private static String getTruststoreFile() {
        return System.getProperty("aliyun.sdk.ssl.truststoreFile");
    }

    private static String getSecureSocketProtocol() {
        return System.getProperty("aliyun.sdk.ssl.secureSocketProtocol", "TLSv1.2");
    }

    private static String getTruststoreType() {
        return System.getProperty("aliyun.sdk.ssl.truststoreType", "JKS");
    }

    private static String getTruststorePassword() {
        return System.getProperty("aliyun.sdk.ssl.truststorePassword", (String)null);
    }

    private static String getTruststoreAlgorithm() {
        return System.getProperty("aliyun.sdk.ssl.truststoreAlgorithm", "SunX509");
    }
}
