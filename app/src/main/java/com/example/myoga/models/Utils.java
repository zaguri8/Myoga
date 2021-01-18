package com.example.myoga.models;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import static androidx.core.content.ContextCompat.getSystemService;

public class Utils {

    public static void loadEmbedVideo(String videoURL, WebView webView) {
        String frameVideo = "<html><body style='margin:0;padding:0;'><iframe width=\"300\" height=\"200\" src=\"" +
                videoURL +
                "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        webView.setBackground(null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(frameVideo, "text/html", "utf-8");
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }



    public static Uri openWebPage(Context context, String url) {
        try {
            if (!URLUtil.isValidUrl(url)) {
                Toast.makeText(context, " Currently unavailable, try again in few seconds", Toast.LENGTH_LONG).show();
                return null;
            } else {
                return Uri.parse(url);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}