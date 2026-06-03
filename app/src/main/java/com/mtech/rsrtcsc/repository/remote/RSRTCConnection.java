package com.mtech.rsrtcsc.repository.remote;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RSRTCConnection {

    public String BASE_URL = "https://rsrtcrfidsystem.co.in/rsrtcapi/";
//    public String BASE_URL_ROUTE = "http://180.92.168.54:8082/RFIDAPI/";
    public String BASE_URL_ROUTE="http://14.194.127.44:8082/RFIDAPI/";
    public String SMS_BASE_URL = "http://Services.m-techinnovations.com/SMSSendService/SMSSendService.svc/";


    public RSRTCInterface createService() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);

        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .build();

        return retrofit.create(RSRTCInterface.class);
    }

    public RSRTCInterface createServiceRoute() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);

        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(RSRTCInterface.class);
    }

    public RSRTCInterface createSMSService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);

        httpClient.networkInterceptors().add(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.header("Content-Type", "text/plain");
                return chain.proceed(requestBuilder.build());
            }
        });

        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SMS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(RSRTCInterface.class);
    }
}




 class UnsafeOkHttpClient {

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            /// Trust all certificates
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            /// Install trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);       /// disable hostname verification

            builder.connectTimeout(1, TimeUnit.MINUTES);
            builder.readTimeout(1, TimeUnit.MINUTES);
            builder.writeTimeout(1, TimeUnit.MINUTES);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}