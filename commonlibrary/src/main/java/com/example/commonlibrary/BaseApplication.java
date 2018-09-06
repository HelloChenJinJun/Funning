package com.example.commonlibrary;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlibrary.adaptScreen.ScreenAdaptManager;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.component.DaggerAppComponent;
import com.example.commonlibrary.dagger.module.GlobalConfigModule;
import com.example.commonlibrary.utils.Constant;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import androidx.multidex.MultiDex;

/**
 * Created by COOTEK on 2017/7/28.
 */

public class BaseApplication extends Application {


    private static AppComponent appComponent;
    private static BaseApplication instance;
    private ApplicationDelegate applicationDelegate;

    public static BaseApplication getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationDelegate = new ApplicationDelegate(base);
        applicationDelegate.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();
        ARouter.init(this);
        initUMInfo();
        initDagger();
        initScreenAdapt();
        instance = this;
        applicationDelegate.onCreate(this);
    }

    private void initUMInfo() {
        UMConfigure.init(this, "5ab0824ff43e483bab00026d", AnalyticsConfig.getChannel(this), UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin(Constant.WXAPP_ID, Constant.WXAPP_KET);
        PlatformConfig.setQQZone("1106767572", "tfZphxuMFpDymwhX");
    }

    private void initScreenAdapt() {
        new ScreenAdaptManager.Builder().designedHeight(640)
                .designedWidth(360).build().initData(this);
    }


    private void initDagger() {
        appComponent=DaggerAppComponent.builder().globalConfigModule(new GlobalConfigModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        applicationDelegate.onTerminate(this);
    }
}
