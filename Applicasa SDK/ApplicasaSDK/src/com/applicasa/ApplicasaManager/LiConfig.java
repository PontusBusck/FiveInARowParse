package com.applicasa.ApplicasaManager;

public class LiConfig {

	/**
	* Enter Application ID
	*/
	private final static String APPLICATION_ID = "57519C";
	
	/**
	 * Enter Application Key
	 */
	private final static String APPLICATION_KEY = "be44f70e8b63ba29";
	
	/**
	 * Enable Applicasa Debug 
	 */
	private final static boolean ENABLE_APPLICASA_DEBUG = true;
	
	/**
	* Indicates whether working in live or sandbox environment
	*/
	private final static boolean SANDBOX = true;

	/**
	*	
	*	In app billing services
	*
	*/
	
		
	/**
	* Is IAP Enabled mode
	*/
	private final static boolean ENABLE_IAP = true;
	
	/**
	* Enter Google public key
	*/
	private final static String GOOGLE_PLAY_PUBLIC_KEY =  "<GOOGLE_PLAY_PUBLIC_KEY>";
	
	
	/**
	 * Is IAP in sandbox mode
	 */
	private final static boolean ENABLE_IAP_SANDBOX = false;
	
	/**
	*	
	*	Push Configuration
	*
	*/
	
	/**
	 * Enable push services
	 */
	private final static boolean ENABLE_PUSH = true;
			
	/**
	* Enter GCM SenderID (projectId or email)
	*/
	private final static String GOOGLE_GCM = "334815809176";
 
 
	/**
	*	
	*	Facebook
	*
	*/
	
	/**
	* Indicate if Facebook allowed
	*/
	private final static boolean ENABLE_FACEBOOK = false;
	
	/**
	 * Enter FaceBook Application key
	 */
	private final static String FB_APPLICATION_KEY = "<FB_APPLICATION_KEY>";

	/**
	 * Enable Fb debug
	 */
	private static boolean ENABLE_FACEBOOK_DEBUG = false;	
	
	
	/**
	*	
	*	Location Services
	*/
	
	/**
	* Enable Location Services
	*/
	private final static boolean ENABLE_LOCATION_SERVICE = true;
	
	/**
	*
	* MMedia
	*/ 
	
	/**
	 * Should enable MMedia Promotions
	 */
	private final static boolean ENABLE_MMEDIA = false;
	
	/**
	*
	* SupersonicAds
	*/ 
	
	/**
	 * Should enable SuperSonic Promotions
	 */
	private final static boolean ENABLE_SUPERSONICADS = false;
	
	/**
	* Enter SuperSonic App ID
	*/
	private final static String SUPERSONICADS_APPID = "";
	
	/**
	*
	* SponorPay
	*/ 
	
	/**
	 * Should enable SponserPay Promotions
	 */
	private final static boolean ENABLE_SPONSORPAY = false;
	
	/**
	* Enter SponserPay App ID
	*/
	
	private final static String SPONSORPAY_ID = "";
	/**
	 * Enter SponserPay SecurityToken 
	 */
	private final static String SPONSORPAY_SECURITYTOKEN = "";
	
	
	/**
	*
	* AppNext
	*/
	/**
	 * Should enable AppNext Promotions
	 */
	private final static boolean ENABLE_APPNEXT = false;
	
	/**
	*
	* Aarki
	*/
	
	/**
	 * Should enable Aarki Promotions
	 */
	private final static boolean ENABLE_AARKI = false;
	
	
	/**
	* Enter Aarki Security Key
	*/
	
	private final static String AARKI_CLIENT_SECURITY_KEY = "";
	

	/**
	*
	* Appia
	*/
	
	/**
	* Should enable Appia Promotions
	*/
	 private final static boolean ENABLE_APPIA = false;
	
	/**
	 * Enter Appia ID
	 */
	 private final static int APPIA_SITE_ID = 0;

	
	/**
	*
	* Chartboost
	*/
	
	/**
	* Should enable Chartboost Promotions
	*/
	 private final static boolean ENABLE_CHARTBOOST = false;
	
	/**
	 * Enter CHARTBOOST ID
	 */
	 private final static String CHARTBOOST_ID = "";
	 /**
	  * Enter CHARTBOOST SIGNATURE 
	  */
	 private final static String CHARTBOOST_SIGNATURE= "";
	
	
	
	/**
	*
	*	System configuration
	*
	*/
	
	
	/**
	 * define the pause time require for a session to be consider a new session
	 */
	private final static int SESSION_PAUSE_TIME = 15;
	
	
	/**
	 *  Minimum Framework Version - DO NOT ALTER
	 */
	private final static double FRAMEWORK_VERSION = 3.2;
	
	/**
	 *   SDK Version - DO NOT ALTER
	 */
	private final static double SDK_VERSION = 3.2;
		
	
	private final static String SCHEMA_VERISON = "3.0";
	
	/**
     * Schema Generated Timestamp in sec
     */
    private final static int SCHEMA_DATE = 1399027664;
	
	/**
	 * Enable offline Action
	 */
	private final static boolean SUPPORT_OFFLINE = true;
	
	
	/**
	* Enable parallel Action request (Add, Update, Delete)
	*/
	private final static boolean ENABLE_PARALLEL_ACTION = true;
	
	
	
	/** End of Basic Configuration file **/	
	
	
	/**
	*
	*		Getters Methods
	*
	*/
	
	
	public static String getApplicationId() {
		return APPLICATION_ID;
	}

	public static String getApplicationKey() {
		return APPLICATION_KEY;
	}

	public static String getFbApplicationKey() {
		return FB_APPLICATION_KEY;
	}

	public static boolean isPushEnabled() {
		return ENABLE_PUSH;
	}

	public static boolean isLocationServiceEnabled() {
		return ENABLE_LOCATION_SERVICE;
	}

	public static boolean isParallelActionEnabled() {
		return ENABLE_PARALLEL_ACTION;
	}

	public static double getFrameworkVersion() {
		return FRAMEWORK_VERSION;
	}

	public static double getSDKVersion() {
		return SDK_VERSION;
	}

	public static String getSchemaVersion() {
		return SCHEMA_VERISON;
	}

	public static boolean isApplicasaDebugEnabled() {
		return ENABLE_APPLICASA_DEBUG;
	}

	public static int getSchemaDate() {
		return SCHEMA_DATE;
	}

	public static boolean isSandbox() {
		return SANDBOX;
	}

	public static boolean isIAPEnabled() {
		return ENABLE_IAP;
	}

	public static boolean isSupportOffline() {
		return SUPPORT_OFFLINE;
	}

	public static boolean isFacebookEnabled()
	{
		return ENABLE_FACEBOOK;
	}
	
	public static boolean isFacebookDebugEnabled() {
		return ENABLE_FACEBOOK_DEBUG;
	}

	public static String getGoogleGCM()
	{
		return GOOGLE_GCM;
	}

	public static String getGooglePlayPublicKey() {
		return GOOGLE_PLAY_PUBLIC_KEY;
	}
	
	public static boolean isSandboxModeEnabled() {
		return ENABLE_IAP_SANDBOX;
	}

	 public static String getChartboostId() {
		return CHARTBOOST_ID;
	}
 
	public static String getChartboostSignature() {
		return CHARTBOOST_SIGNATURE;
	}
 
	public static boolean isChartboostEnabled() {
		return ENABLE_CHARTBOOST;
	}

	public static int getAppiaSiteID() {
		return APPIA_SITE_ID;
	}
 
	public static boolean isAppiaEnabled() {
		return ENABLE_APPIA;
	}
	
	public static boolean isMMediaEnabled() {
		return ENABLE_MMEDIA;
	}
	
	public static boolean isSupersonicAdsEnabled() {
		return ENABLE_SUPERSONICADS;
	}
	
	public static String getSupersonicAdsAppId() {
		return SUPERSONICADS_APPID;
	}
	
	public static boolean isAppnextEnabled() {
		return ENABLE_APPNEXT;
	}
	
	public static String getSponserPayAppId() {
		return SPONSORPAY_ID;
	}
	
	public static String getSponserPaySecurityToken() {
		return SPONSORPAY_SECURITYTOKEN;
	}
	
	public static boolean isSponsorPayEnabled() {
		return ENABLE_SPONSORPAY;
	}
	
	public static boolean isAarkiEnabled() {
		return ENABLE_AARKI;
	}
	
	public static String getAarkiSecurityKey() {
		return AARKI_CLIENT_SECURITY_KEY;
	}
	
	
	public static int getSessionPauseTime()
	{
		return SESSION_PAUSE_TIME;
	}
	
	
	
}
