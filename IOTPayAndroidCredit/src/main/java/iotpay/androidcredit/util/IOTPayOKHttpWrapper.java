package iotpay.androidcredit.util;


import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;

public class IOTPayOKHttpWrapper {

    private static final int DEFAULT_TIMEOUT = 125;  //credit pay need more timeout

    private static IOTPayOKHttpWrapper sInstance;

    private OkHttpClient okHttpClient;

    private IOTPayOKHttpWrapper() throws Exception {
        initOkHttpClient();
    }

    public static IOTPayOKHttpWrapper getInstance() throws Exception {
        if (sInstance == null) {
            sInstance = new IOTPayOKHttpWrapper();
        }
        return sInstance;
    }

    public void initOkHttpClient() throws Exception{
        Cache cache = null;//new Cache(new File(MyApplication.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
        Interceptor cacheControlInterceptor = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
                return chain.proceed(request);
            }

        };
        //HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        SSLSocketFactory socketFactory = null;
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] trustManagers = {x509TrustManager};
            sslContext.init(null, trustManagers, new SecureRandom());
            socketFactory = sslContext.getSocketFactory();
        if (socketFactory != null) {
            okHttpClient = new OkHttpClient.Builder()
                     .cache(cache)
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .sslSocketFactory(socketFactory)
                    .hostnameVerifier(hostnameVerifier)

                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
                     .cache(cache)
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .hostnameVerifier(hostnameVerifier)
                    .build();
        }
    }

    private X509TrustManager x509TrustManager = new X509TrustManager() {


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    };

    private HostnameVerifier hostnameVerifier = new HostnameVerifier() {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    };

    private ConnectionSpec connectionSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_1)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build();
    public String post(String path, String params) throws Exception {
        return postget(path, params,true);
    }
    public String get(String path) throws Exception {
        return postget(path, null,false);
    }
    public String postget(String path, String bodyString, boolean isPost) throws Exception {
        MediaType JSON = MediaType.parse("application/text; charset=utf-8");
        RequestBody bodyJson = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(path)
                .post(bodyJson)
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

            boolean successful = response.isSuccessful();
            if (successful) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String responseStr = responseBody.string();
                    return responseStr;
                } else {
                    throw new Exception("response body is null");
                }
            } else {
                throw new Exception("response failed");
            }
    }


}
