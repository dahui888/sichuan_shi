/******************************************************************
 * Copyright (C) Newegg Corporation. All rights reserved.
 *
 * Author: Colin Chen(Colin.Z.Chen@newegg.com) 
 * Create Date: 09/23/2010 14:16:51 PM
 * Description:
 *
 * Revision History:
 *      Date         Author               Description
 *
 *****************************************************************/
package com.base.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * This class is used to switch between pages including "From Main to Sub" ,
 * "From Sub to Main" and "From One to Another" modes.
 */
public final class IntentUtil {

    /**
     * get simple new intent
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @return
     */
    private static Intent getNewIntent(Context packageContext, Class<?> cls) {
        return new Intent(packageContext, cls);
    }

    /**
     * get simple new intent with bundle data
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @param bundle         the parameter data
     * @return
     */
    private static Intent getBundleIntent(Context packageContext, Class<?> cls, Bundle bundle) {
        return getNewIntent(packageContext, cls).putExtras(bundle);
    }

    /**
     * redirect to next page
     *
     * @param packageContext this current page
     * @param cls            the target page
     */
    public static void redirectToNextActivity(Context packageContext, Class<?> cls) {
        packageContext.startActivity(getNewIntent(packageContext, cls));
    }

    /**
     * Single String value as a parameter of the activity-oriented switching
     * <p>
     * get parameter method intent.getExtras();
     *
     * @param packageContext     current Context (xxx.this or this)
     * @param cls                target (xxx.class)
     * @param onlyKey            The Bundle of extras to add to this intent
     * @param correspondingValue String value as parameter
     */
    public static void redirectToNextActivity(Context packageContext, Class<?> cls, String onlyKey, String correspondingValue) {
        packageContext.startActivity(getNewIntent(packageContext, cls).putExtra(onlyKey, correspondingValue));
    }

    /**
     * redirect to next page with parameter data
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @param bundle         the parameter data
     */
    public static void redirectToNextActivity(Context packageContext, Class<?> cls, Bundle bundle) {
        packageContext.startActivity(getBundleIntent(packageContext, cls, bundle));
    }

    /**
     * redirect to next page with new task mode
     *
     * @param packageContext this current page
     * @param cls            the target page
     */
    public static void redirectToNextNewActivity(Context packageContext, Class<?> cls) {

        Intent intent = getNewIntent(packageContext, cls).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        packageContext.startActivity(intent);
    }

    /**
     * redirect to next page with new task mode
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @param bundle         the parameter
     */
    public static void redirectToNextNewActivity(Context packageContext, Class<?> cls, Bundle bundle) {

        Intent intent = getBundleIntent(packageContext, cls, bundle).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        packageContext.startActivity(intent);
    }

    /**
     * redirect to main page from sub page
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @param resultCode     the result code from target page to response to some request.
     */
    public static void redirectToMainActivity(Context packageContext, Class<?> cls, int resultCode) {
        final Activity tempActivity = (Activity) packageContext;
        tempActivity.setResult(resultCode, getNewIntent(packageContext, cls));
        tempActivity.finish();
    }

    /**
     * redirect to main page from sub page with data
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @param bundle         the parameter
     * @param resultCode     the result code from sub page
     */
    public static void redirectToMainActivity(Context packageContext, Class<?> cls, Bundle bundle, int resultCode) {
        final Activity tempActivity = (Activity) packageContext;
        tempActivity.setResult(resultCode, getBundleIntent(packageContext, cls, bundle));
        tempActivity.finish();
    }

    /**
     * redirect to sub page from main page
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @param requestCode    the request code from main page
     */
    public static void redirectToSubActivity(Context packageContext, Class<?> cls, int requestCode) {

        final Activity tempActivity = (Activity) packageContext;
        tempActivity.startActivityForResult(getNewIntent(packageContext, cls), requestCode);
    }

    /**
     * redirect to sub page from main page
     *
     * @param packageContext this current page
     * @param cls            the target page
     * @param bundle         the parameter
     * @param requestCode    the request code from main page
     */
    public static void redirectToSubActivity(Context packageContext, Class<?> cls, Bundle bundle, int requestCode) {
        final Activity tempActivity = (Activity) packageContext;
        tempActivity.startActivityForResult(getBundleIntent(packageContext, cls, bundle), requestCode);
    }
}
