package com.nilapps.batteryalarm.templates;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {

    Activity activity;
    BillingClient billingClient;
    boolean isServiceConnected = false;
    int billingClientResponseCode;
    public BillingUpdatesListener billingUpdatesListener;

    public BillingManager(Activity activity, final BillingUpdatesListener billingUpdatesListener) {

        this.activity = activity;
        this.billingUpdatesListener = billingUpdatesListener;
        billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();
        startServiceConnection(new Runnable() {
            @Override
            public void run() {
                billingUpdatesListener.onBillingClientSetupFinished();
            }
        });
    }

    public void end_connection(){
        billingClient.endConnection();
    }

    public void startServiceConnection(final Runnable runnable) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    isServiceConnected = true;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
                billingClientResponseCode = billingResult.getResponseCode();
            }

            @Override
            public void onBillingServiceDisconnected() {
                isServiceConnected = false;
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingUpdatesListener.onPurchasesUpdated(purchases);
            for (final Purchase p : purchases) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handlepurchaseACK(p);
                    }
                }, 1000);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            billingUpdatesListener.onPurchasesFaild();
        } else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE){
            billingUpdatesListener.onPurchasesFaild();
        } else {
            billingUpdatesListener.onPurchasesFaild();
        }
    }

    public void handlepurchaseACK(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder().
                        setPurchaseToken(purchase.getPurchaseToken()).build();
                billingClient.acknowledgePurchase(params, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                    }
                });
            }
        }
    }

    public interface BillingUpdatesListener {
        void onBillingClientSetupFinished();
        void onConsumeFinished(String token, @BillingClient.BillingResponseCode int result);
        void onPurchasesUpdated(List<Purchase> purchases);
        void onPurchasesFaild();
    }

    private void executeServiceRequest(Runnable runnable) {
        if (isServiceConnected) {
            runnable.run();
        } else {
            // If the billing service disconnects, try to reconnect once.
            startServiceConnection(runnable);
        }
    }

    public void queryPurchases() {
        Runnable queryToExecute = new Runnable() {
            @Override
            public void run() {
                Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                if (areSubscriptionsSupported()) {
                    Purchase.PurchasesResult subscriptionResult
                            = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                    if (subscriptionResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        purchasesResult.getPurchasesList().addAll(
                                subscriptionResult.getPurchasesList());
                        //Log.e("Value " , subscriptionResult.getPurchasesList() + " ");
                    } else {
                        // Handle any error response codes.
                    }
                } else {
                    purchasesResult.getResponseCode();// Skip subscription purchases query as they are not supported.
// Handle any other error response codes.
                }
                onQueryPurchasesFinished(purchasesResult);
            }
        };
        executeServiceRequest(queryToExecute);
    }

    private void onQueryPurchasesFinished(Purchase.PurchasesResult result) {
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (billingClient == null || result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            /*//Log.e(TAG, "Billing client was null or result code (" + result.getResponseCode()
                    + ") was bad – quitting");*/
            return;
        }
        // Update the UI and purchases inventory with new list of purchases
        // mPurchases.clear();
        onPurchasesUpdated(result.getBillingResult(), result.getPurchasesList());
    }

    public boolean areSubscriptionsSupported() {
        int responseCode = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS).getResponseCode();
        return responseCode == BillingClient.BillingResponseCode.OK;
    }

    public void initiatePurchaseFlow(final List<String> skuList, final @BillingClient.SkuType String billingType) {
        Runnable purchaseFlowRequest = new Runnable() {
            @Override
            public void run() {
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(billingType);

                billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        for (SkuDetails s : skuDetailsList) {
                            Log.e("demo foe", s + " ");
                            BillingFlowParams mParams = BillingFlowParams.newBuilder().setSkuDetails(s).build();
                            billingClient.launchBillingFlow(activity, mParams);
                        }
                    }
                });
            }
        };
        executeServiceRequest(purchaseFlowRequest);
    }

    public void consumeAsync(Purchase purchase) {
        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult,  String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                }
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }
}