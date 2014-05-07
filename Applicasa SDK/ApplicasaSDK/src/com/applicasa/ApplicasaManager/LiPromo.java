package com.applicasa.ApplicasaManager;

import java.util.List;

import android.app.Activity;
import applicasa.LiCore.LiErrorHandler;
import applicasa.LiCore.communication.LiRequestConst.RequestCallback.LiCallbackGetPromotion;
import applicasa.LiCore.promotion.sessions.LiEventManager;
import applicasa.LiCore.promotion.sessions.LiPromotionCallback;
import applicasa.LiCore.promotion.sessions.LiPromotionCallback.LiPromotionResultCallback;

import com.applicasa.Promotion.LiPromoManager;
import com.applicasa.Promotion.Promotion;

import com.applicasa.ThirdParty.ThirdPartyManager;
import com.applicasa.TrialPay.TrialPayManager.LiTPActionResultCallback;

public class LiPromo {

	/**
	 * Sets the promotion callback
	 * @param liPromotionCallback
	 */
	public static void setPromoCallback(LiPromotionCallback liPromotionCallback)
	{
		LiEventManager.setPromoCallback(liPromotionCallback);
	}
	
	/**
	 * Sets the promotion callback
	 * @param liPromotionCallback
	 * @param shouldCheck - indicate whether after setting the callback to check for pending promotions
	 */
	public static void setPromoCallbackAndCheckForAvailablePromotions(LiPromotionCallback liPromotionCallback, boolean shouldCheck)
	{
		LiEventManager.setPromoCallbackAndCheckForAvailablePromotions(liPromotionCallback,shouldCheck);
	}
	
	/**
	 * 
	 * @return the available promotions
	 */
	public static List<Promotion> getAvailablePromotions()
	{
		return LiPromoManager.getAvailablePromotions();
	}
	
	/**
	 * 
	 * @return the available promotions
	 * @throws applicasa.LiCore.LiErrorHandler
	 */
	public static void refreshPromotions(LiCallbackGetPromotion liCallBackGetPromotion) throws LiErrorHandler
	{
		LiPromoManager.getPromotions(liCallBackGetPromotion);
	}
	
	/**
	 * Dismiss all promotion waiting to be viewed
	 */
	public static void dismissAllPromotion()
	{
		LiPromoManager.dismissAllPromotion();
	}
	
	/**
	 * raise custom event by event key name
	 * @param key the name of the event
	 */
	public static void raiseCustomEvent(String key)
	{
		LiPromoManager.raiseCustomEvent(key);
	}
	
	/**
	 * raise custom event by event key name and shows the event
	 * @param key the name of the event
	 */
	public static void raiseCustomEvent(String key, Activity activity, LiPromotionResultCallback liPromotionResultCallback )
	{
		LiPromoManager.raiseCustomEvent(key,activity, liPromotionResultCallback);
	}
	
	/**
	* Get all ThirdParty actions
	* @param liTPActionResultCallback
	*/
	public static void getTPAction(LiTPActionResultCallback liTPActionResultCallback)
	{
		ThirdPartyManager.getTPAction(liTPActionResultCallback);
	}
}
