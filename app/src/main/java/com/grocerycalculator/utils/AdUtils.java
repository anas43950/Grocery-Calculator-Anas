package com.grocerycalculator.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.grocerycalculator.R;
import com.grocerycalculator.databinding.NativeAdLayoutBinding;

public class AdUtils {
    static InterstitialAd mInterstitialAd;
    static ProgressDialog progressDialog;
    static Activity activity;
    static OnLoadInterstitialFailedListener listener;

    public static void showBannerAd(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public interface OnLoadInterstitialFailedListener {
        void onLoadInterstitialFailed();
    }

    public static void showInterstitialAd(Activity activity, FullScreenContentCallback callback, OnLoadInterstitialFailedListener listener) {

        AdUtils.listener = listener;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(Html.fromHtml("<font color='black'>Loading...</font>"));
        progressDialog.setCancelable(false);
        progressDialog.show();
        AdUtils.activity = activity;

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, activity.getResources().getString(R.string.interstitial_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(activity);
                        progressDialog.dismiss();
                        mInterstitialAd.setFullScreenContentCallback(callback);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                        progressDialog.dismiss();
                        listener.onLoadInterstitialFailed();
                    }
                });
    }


    public static void loadNativeAd(Context context, NativeAdLayoutBinding binding) {
        AdLoader.Builder builder = new AdLoader.Builder((Activity) context, context.getResources().getString(R.string.native_id))
                .forNativeAd(nativeAd -> populateNativeADView(nativeAd, binding));

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(context, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void populateNativeADView(NativeAd nativeAd, NativeAdLayoutBinding binding) {
        NativeAdView adView = binding.adView;
        adView.setIconView(binding.iconIv);
        adView.setHeadlineView(binding.headlineTv);
        adView.setCallToActionView(binding.callToAction);
        adView.setStarRatingView(binding.ratingBar);
        adView.setStoreView(binding.storeNameTv);
        if (nativeAd.getIcon() != null) {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
        }
        if (nativeAd.getHeadline() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }
        if (nativeAd.getCallToAction() != null) {
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getStarRating() != null) {
            ((RatingBar) adView.getStarRatingView()).setRating(Float.parseFloat(String.valueOf(nativeAd.getStarRating())));
        }
        if (nativeAd.getStore() != null) {
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
        adView.setNativeAd(nativeAd);
    }
}
