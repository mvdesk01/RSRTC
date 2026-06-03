package com.mtech.rsrtcsc.ui.activity.splash;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivitySplashBinding;
import com.mtech.rsrtcsc.ui.activity.auth.login.LoginActivity;
import com.mtech.rsrtcsc.utils.FileLogger;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    private AppUpdateManager appUpdateManager;
    private static final int UPDATE_REQUEST_CODE = 101;
    private boolean updateLaunched = false;

    @Override
    protected ActivitySplashBinding getActivityBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {

//        FileLogger.log(SplashActivity.this, "SplashScreen", "SplashScreen Loaded");

        // Firebase
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck.getInstance()
                .installAppCheckProviderFactory(
                        PlayIntegrityAppCheckProviderFactory.getInstance());

        // Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top,
                    systemBars.right, systemBars.bottom);
            return insets;
        });

        // INIT UPDATE MANAGER (IMPORTANT)
        appUpdateManager = AppUpdateManagerFactory.create(this);

        checkForAppUpdate();
    }

    @Override
    protected void initCtrl() {

    }

    // ---------------- UPDATE LOGIC ----------------

    private void checkForAppUpdate() {
        FileLogger.log(SplashActivity.this, "SplashScreen", "check app update");

        appUpdateManager.getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {

                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                            && !updateLaunched) {

                        updateLaunched = true;
                        startFlexibleUpdate(appUpdateInfo);

                    } else {
                        proceedToNextScreen();
                    }
                })
                .addOnFailureListener(e -> {
                    FileLogger.log(SplashActivity.this, "SplashScreen", "App Update check failed: "+e);
                    Log.e("Update", "App Update check failed", e);
                    proceedToNextScreen();
                });
    }

    private void startFlexibleUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    UPDATE_REQUEST_CODE
            );

            appUpdateManager.registerListener(installStateUpdatedListener);

        } catch (Exception e) {
            FileLogger.log(SplashActivity.this, "SplashScreen", "App Update flow failed: "+e);
            Log.e("Update", "Update flow failed", e);
            proceedToNextScreen();
        }
    }

    private final InstallStateUpdatedListener installStateUpdatedListener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showCompleteUpdateDialog();
        }
    };

    private void showCompleteUpdateDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Update Ready")
                .setMessage("An update has been downloaded. Restart to apply it.")
                .setPositiveButton("Restart",
                        (dialog, which) -> appUpdateManager.completeUpdate())
                .setCancelable(false)
                .show();
    }

    // ---------------- NAVIGATION ----------------

    private void proceedToNextScreen() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, 2000);
    }

    // ---------------- ACTIVITY RESULT ----------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST_CODE && resultCode != RESULT_OK) {
            Log.e("Update", "User cancelled or update failed");
            proceedToNextScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }
}







/*
package com.mtech.rsrtcsc.ui.activity.splash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.WindowManager;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivitySplashBinding;
import com.mtech.rsrtcsc.ui.activity.auth.login.LoginActivity;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected ActivitySplashBinding getActivityBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        loadIntent();

        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Optional: Make status and nav bar transparent
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
//        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        loadIntent();
    }

    @Override
    protected void initCtrl() { }


    private void loadIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        },3000);
    }
}*/
