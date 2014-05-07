package com.applicasa.ActiveGames;
import java.util.ArrayList;
import java.util.List;
import java.util.GregorianCalendar;

import applicasa.LiCore.communication.LiUtility;


import applicasa.LiCore.communication.LiCallback.LiCallbackAction;
import com.applicasa.ApplicasaManager.LiCallbackQuery.LiActiveGamesGetByIDCallback;
import com.applicasa.ApplicasaManager.LiCallbackQuery.LiActiveGamesGetArrayCallback;
import com.applicasa.ApplicasaManager.LiManager.LiObject;

import android.database.Cursor;
import applicasa.LiCore.sqlDB.database.LiDbObject;
import applicasa.LiCore.communication.LiRequestConst.QueryKind;
import applicasa.LiCore.communication.LiUtility;
import applicasa.LiCore.LiErrorHandler;
import applicasa.LiCore.LiErrorHandler.ApplicasaResponse;
import applicasa.LiCore.communication.LiRequestConst.RequestAction;
import applicasa.LiCore.communication.LiObjRequest;
import applicasa.LiCore.communication.LiRequestConst.RequestCallback;
import applicasa.LiCore.communication.LiRequestConst.LiObjResponse;
import applicasa.LiCore.communication.LiFilters;
import applicasa.LiCore.communication.LiQuery;
import applicasa.LiCore.communication.LiFilters.Operation;
import applicasa.LiCore.sqlDB.database.LiCoreDBmanager;
import applicasa.LiJson.LiJSONException;
import applicasa.LiJson.LiJSONObject;



public class ActiveGames extends ActiveGamesData {
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////							SAVE                     /////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Saves the Object to Applicasa's Servers
	 * The method saves all of the item's value to the server
	 * If the Object has an ID the operation will update existing object in applicasa's server; otherwise an add operation will be called
	 * 
	 * In Order to Update a specific field Use the method saveFields
	 * @param actionCallback
	 * @return
	 * @throws applicasa.LiCore.LiErrorHandler
	 */
	public void save(LiCallbackAction liCallbackAction)  
	{
		LiObjRequest request = new LiObjRequest();
		
		// If Id is of hex representation and not 0, then the itemId is Mongo id
		if (ActiveGamesID!= "0" && (LiUtility.isHex(ActiveGamesID)|| ActiveGamesID.startsWith("temp_") ))
		{
			request.setAction(RequestAction.UPDATE_ACTION);
			request.setRecordID(ActiveGamesID);
			request.setIncrementedFields(incrementedFields);			
		}
		else
		{
			request.setAction(RequestAction.ADD_ACTION);
			request.setAddedObject(this);
		}
		
		request.setClassName(kClassName);
		request.setCallback(callbackHandler);
		request.setEnableOffline(EnableOffline);
		
		setActionCallback(liCallbackAction,request.requestID);
		// add the Values of the Object Item to the Request
		try{
			request.setParametersArrayValue(dictionaryRepresentation(false));
		}catch (LiErrorHandler e) {
			if (liCallbackAction != null)
				liCallbackAction.onFailure(e);
			else 
				return;
		}
		
		request.startASync();

	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////						  DELETE                     /////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public void delete(LiCallbackAction liCallbackAction) 
	{
		// Verifies Item isn't null
		if (ActiveGamesID == null || ActiveGamesID == "") 
			if (liCallbackAction != null) liCallbackAction.onFailure(new LiErrorHandler(ApplicasaResponse.NULL_ITEM, "Missing Item ID"));
			else return;
		
		LiObjRequest request = new LiObjRequest();
		request.setAction(RequestAction.DELETE_ACTION);
		request.setClassName(kClassName);
		request.setCallback(callbackHandler);
		request.setRecordID(ActiveGamesID);
		
		setActionCallback(liCallbackAction,request.requestID);
		request.setEnableOffline(EnableOffline);
		
		request.startASync();

	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////							  						 /////////////////////////////////////////
//////////////////////////////////						   GET                       /////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	/**
	    * A- Synchronized function which returns an object from server by ID
	    * @param ID
	    * @return
	    * @throws applicasa.LiCore.LiErrorHandler
	    */
	    public static void getByID(String Id, QueryKind queryKind, LiActiveGamesGetByIDCallback liActiveGamesGetByIDCallback) 
	    {
	        if (Id != null)
	        {
	        	// Creates new Query
	        	LiQuery query= new LiQuery();
	        	
	        	// Create a where statement expression of ObjectId = 'id';  
		        LiFilters filter = new LiFilters(LiFieldActiveGames.ActiveGamesID, Operation.EQUAL, Id);		        
		        query.setFilter(filter);
	        	
		    	LiObjRequest request = new LiObjRequest();
		        request.setClassName(kClassName);
		
		        request.setAction(RequestAction.GET_ACTION);
		        request.setGet(queryKind);
		       
		        request.setQueryToRequest(query);
		        request.setCallback(callbackHandler);
		        setGetCallback(liActiveGamesGetByIDCallback,request.requestID);
		       
		        request.startASync();
	        }
	    }

	    /**
	    * A- Synchronized Method to returns an object from server by filters
	    * @param ID
	    * @return
	    * @throws applicasa.LiCore.LiErrorHandler
	    */
	    public static void getArrayWithQuery(LiQuery query ,QueryKind queryKind, LiActiveGamesGetArrayCallback liActiveGamesGetArrayCallback) 
	    {
	        LiObjRequest request = new LiObjRequest();
	        request.setClassName(kClassName);
	        request.setAction(RequestAction.GET_ARRAY);
	        request.setGet(queryKind);
	        request.setQueryToRequest(query);
	        request.setCallback(callbackHandler);
	        setGetCallback(liActiveGamesGetArrayCallback,request.requestID);
	        request.startASync();
	    }
	   
		public static void getLocalyWithRawSQLQuery(String whereClause, String[] args, LiActiveGamesGetArrayCallback liActiveGamesGetArrayCallback)
	    {
			LiObjRequest request = new LiObjRequest();
	        request.setCallback(callbackHandler);
	        setGetCallback(liActiveGamesGetArrayCallback,request.requestID);
	        request.GetWithRawQuery(kClassName, whereClause, args);
	    }
	
		 /** Synchronized Method to returns an object from server by filters
		 * @param ID
		 * @return the list of items or null in case on an error
		 * @throws applicasa.LiCore.LiErrorHandler
		 */
		 public static List<ActiveGames> getArrayWithQuery(LiQuery query ,QueryKind queryKind) throws LiErrorHandler 
		 {
			 LiObjRequest request = new LiObjRequest();
			 request.setClassName(kClassName);
			 request.setAction(RequestAction.GET_ARRAY);
			 request.setGet(queryKind);
			 request.setQueryToRequest(query);
			 LiObjResponse response = request.startSync();
			 
			 if (response.LiRespType.equals(ApplicasaResponse.RESPONSE_SUCCESSFUL))
			 {
			  Cursor cursor = request.getCursor();
			  return buildActiveGamesFromCursor(request.requestID, cursor);
			 }
			 
			 return null;
		 }

	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////					    Upload File                  /////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	* Method to Upload file 
	* @param activeGamesField - The field to be updated with the file name in Applicasa server 
	* @param filePath - the path to the uploaded file
	* @param activeGamesActionCallBack - call back to indicate when the upload was completed
	* @return
	* @throws applicasa.LiCore.LiErrorHandler
	*/
	public void updloadFile(LiFieldActiveGames liFieldActiveGames, String filePath, LiCallbackAction liCallbackAction)
	{
		LiObjRequest request = new LiObjRequest();
		
		request.setAction(RequestAction.UPLOAD_FILE);
		request.setClassName(ActiveGames.kClassName);
		request.setRecordID(ActiveGamesID);
		
		request.setFileFieldName(liFieldActiveGames);
		request.setFilePath(filePath);
		request.setAddedObject(this);
		request.setCallback(callbackHandler);
		setActionCallback(liCallbackAction,request.requestID);
		
		
		request.startASync();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////						Callback                     /////////////////////////////////////////
//////////////////////////////////													 /////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
static RequestCallback callbackHandler = new RequestCallback() {
		
	public void onCompleteGet(String requestID, Cursor cursor) {
		// TODO Auto-generated method stub
		List<ActiveGames> returnList = new ArrayList<ActiveGames>();

		returnList = buildActiveGamesFromCursor(requestID ,cursor);

		Object callback = activeGamesCallbacks.get(requestID);
		if (callback != null && callback instanceof LiActiveGamesGetArrayCallback)
		{
			activeGamesCallbacks.remove(requestID);
			((LiActiveGamesGetArrayCallback)callback).onGetActiveGamesComplete(returnList);
		}
		if (callback != null && callback instanceof LiActiveGamesGetByIDCallback )
		{
			activeGamesCallbacks.remove(requestID);
			((LiActiveGamesGetByIDCallback)callback).onGetActiveGamesComplete(returnList.get(0));
		}
		
	}
	
	public void LiException(String requestID,LiErrorHandler ex) {
		// TODO Auto-generated method stub
		Object callback = activeGamesCallbacks.get(requestID);
		if (callback != null && callback instanceof LiActiveGamesGetArrayCallback )
		{
			activeGamesCallbacks.remove(requestID);
			((LiActiveGamesGetArrayCallback)callback).onGetActiveGamesFailure(ex);
		}		
		else if (callback != null && callback instanceof LiActiveGamesGetByIDCallback )
		{
			activeGamesCallbacks.remove(requestID);
			((LiActiveGamesGetByIDCallback)callback).onGetActiveGamesFailure(ex);
		}
		else if (callback != null && callback instanceof LiCallbackAction )
		{
			activeGamesCallbacks.remove(requestID);
			((LiCallbackAction)callback).onFailure(ex);
		}

	}
		public void onCompleteAction(String requestID, LiObjResponse response) {
			// TODO Auto-generated method stub
			Object callback = activeGamesCallbacks.get(requestID);
			if (callback != null )
			{
				activeGamesCallbacks.remove(requestID);
				if (response.action == RequestAction.ADD_ACTION)
					((ActiveGames)response.addedObject).ActiveGamesID = response.newObjID;
					
				if (response.action == RequestAction.UPLOAD_FILE)
				{
					((ActiveGames)response.addedObject).setActiveGamesFieldbySortType((LiFieldActiveGames)response.field, response.newObjID);
					if (response.actionResponseList.get(0).objId != null && response.actionResponseList.get(0).requestID == requestID )
						((ActiveGames)response.addedObject).ActiveGamesID = response.actionResponseList.get(0).objId;
				}
								
				((LiCallbackAction)callback).onComplete(response.LiRespType, response.LiRespMsg, response.action,response.newObjID, LiObject.getLiObject(response.className));
			}
		}
	};
	
	/**
	 * 
	 * @param requestID
	 * @param cursor
	 * @deprecated use buildActiveGamesFromCursor
	 * @return
	 */
	@Deprecated
	public static List<ActiveGames> BuildActiveGamesFromCursor(String requestID, Cursor cursor)
	{
		return buildActiveGamesFromCursor(requestID, cursor);
	}

	/**
	 * 
	 * @param requestID
	 * @param cursor
	 * @return
	 */
	public static List<ActiveGames> buildActiveGamesFromCursor(String requestID ,Cursor cursor)
	{
		List<ActiveGames> returnList = new ArrayList<ActiveGames>();
		if (cursor == null || cursor.getCount() == 0 ) {return returnList; }// nothing received
		else
		{
			cursor.moveToFirst();
			ArrayList<String> idsList = LiObjRequest.IdsMap.get(requestID);
            ArrayList<String> idsToDelete = new ArrayList<String>();
            
            String id;
            while (!cursor.isAfterLast())
            {
                id = cursor.getString(0);
                if (idsList == null || idsList.contains(id))
                {
                    returnList.add(new ActiveGames(cursor));                    
                }
                else
                {
                    idsToDelete.add(id);
                }
				cursor.moveToNext();
            }
            if (!idsToDelete.isEmpty())
			{
				LiObjRequest.DeleteUnlistedIds(kClassName,requestID, idsToDelete);
			}
			idsList = null;
			idsToDelete = null;			
		}
		
		cursor.close();
	
	
		return returnList;
		
	}
	
	
	private static void setGetCallback(LiActiveGamesGetArrayCallback getCallback, String reqID) {
		// TODO Auto-generated method stub
		activeGamesCallbacks.put(reqID, getCallback);
	}
	
	private static void setGetCallback(LiActiveGamesGetByIDCallback getCallback, String reqID) {
		// TODO Auto-generated method stub
		activeGamesCallbacks.put(reqID, getCallback);
	}

	private static void setActionCallback(LiCallbackAction actionCallback, String reqID) {
		// TODO Auto-generated method stub
		activeGamesCallbacks.put(reqID, actionCallback);
	}
	
	
	 /** Synchronized Method that updates local storage according to request
	 * @return the item Count, if count of 1500 is the max number of values returned by the server.
	 * @throws applicasa.LiCore.LiErrorHandler
	 */
	 public static int updateLocalStorage(LiQuery query ,QueryKind queryKind) throws LiErrorHandler 
	 {
		 int recordsCount = 0;
		 LiObjRequest request = new LiObjRequest();
		 request.setClassName(kClassName);
		 request.setAction(RequestAction.GET_ARRAY);
		 request.setGet(queryKind);
		 request.setQueryToRequest(query);
		 LiObjResponse response = request.startSync();
		 
		 if (response.LiRespType.equals(ApplicasaResponse.RESPONSE_SUCCESSFUL))
		 {
			 Cursor cursor = request.getCursor();
			 if(cursor == null)
				return 0;
		
			 if (queryKind.compareTo(QueryKind.PAGER) != 0)
			 {
				 deleteItems(request.requestID,cursor);
			 }
			 
			 recordsCount = cursor.getCount();
			 cursor.close();
			 cursor = null;
		 }
		 
		 return recordsCount;
	 }
	 
	 public static void deleteItems(final String requestID ,final Cursor cursor)
	 {
		 new Thread(new Runnable() {
			
			public void run() {
					// TODO Auto-generated method stub
					if (cursor == null || cursor.getCount() == 0 ) {}// nothing received
					else
					{
						cursor.moveToFirst();
						ArrayList<String> idsList = LiObjRequest.IdsMap.get(requestID);
						ArrayList<String> idsToDelete = new ArrayList<String>();
						
						String id;
						while (!cursor.isAfterLast())
						{
							id = cursor.getString(0);
							if (idsList != null && !idsList.contains(id))
							{
								idsToDelete.add(id);
							}
							cursor.moveToNext();
						}
						if (!idsToDelete.isEmpty())
						{
							LiObjRequest.DeleteUnlistedIds(kClassName,requestID, idsToDelete);
						}
						idsList = null;
						idsToDelete = null;			
					}
				}
			}).run();
		}
 /** End of Basic SDK **/ 

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////                                                   ////////////////////////////////////////
///////////////////////////////////                    Init Method                    ////////////////////////////////////////
///////////////////////////////////                    Don't ALTER                    ////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ActiveGames()
	{
		this.ActiveGamesSecondPlayer = "";
		this.ActiveGamesFirstPlayer = "";
		this.ActiveGamesLastMoveMadeBy = 0;
		this.ActiveGamesID = "0";
		(this.ActiveGamesLastUpdate = new GregorianCalendar()).setTimeInMillis(0);
		this.ActiveGamesGameArrayString = 0;
		this.ActiveGamesWantRandomPlayer = false;
		this.ActiveGamesPlayersTurn = 0;
	}

	public ActiveGames(Cursor cursor) 
	{
		initWithCursor(cursor);
	}
	
	public ActiveGames(Cursor cursor,String header,int level) 
	{
		initWithCursor(cursor,header,level);
	}
	
	public ActiveGames(String ActiveGamesID)
	{
		this.ActiveGamesID = ActiveGamesID;
	}

	public ActiveGames(ActiveGames item)
	{
		initWithObject(item);
	}

	/**
	* Init Object with Cursor
	* @param corsor
	* @return
	*/
	public ActiveGames initWithCursor(Cursor cursor)
	{
		return initWithCursor(cursor,"",0);
	}
	
	/**
	* Init Object with Cursor
	* @param corsor
	* @return
	*/
	public ActiveGames initWithCursor(Cursor cursor,String header,int level)
	{
		int columnIndex;
	
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesSecondPlayer.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.ActiveGamesSecondPlayer = cursor.getString(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesFirstPlayer.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.ActiveGamesFirstPlayer = cursor.getString(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesLastMoveMadeBy.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.ActiveGamesLastMoveMadeBy = cursor.getInt(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesID.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.ActiveGamesID = cursor.getString(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesLastUpdate.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
		{
			long dateStr = cursor.getLong(columnIndex);
			GregorianCalendar gc= new GregorianCalendar();
			gc.setTimeInMillis(dateStr);
			this.ActiveGamesLastUpdate = gc;
		}
		
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesGameArrayString.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.ActiveGamesGameArrayString = cursor.getInt(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesWantRandomPlayer.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
		{
			this.ActiveGamesWantRandomPlayer = cursor.getInt(columnIndex)==1?true:false;
		}
		
		columnIndex = cursor.getColumnIndex(header + LiFieldActiveGames.ActiveGamesPlayersTurn.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.ActiveGamesPlayersTurn = cursor.getInt(columnIndex);
		
	
		return this;
	}
	
	/**
	* Initialize values with Object
	* @param item
	* @return
	*/
	public String initWithObject(ActiveGames item)
	{
		this.ActiveGamesSecondPlayer			= item.ActiveGamesSecondPlayer;
		this.ActiveGamesFirstPlayer			= item.ActiveGamesFirstPlayer;
		this.ActiveGamesLastMoveMadeBy			= item.ActiveGamesLastMoveMadeBy;
		this.ActiveGamesID			= item.ActiveGamesID;
		this.ActiveGamesLastUpdate			= item.ActiveGamesLastUpdate;
		this.ActiveGamesGameArrayString			= item.ActiveGamesGameArrayString;
		this.ActiveGamesWantRandomPlayer			= item.ActiveGamesWantRandomPlayer;
		this.ActiveGamesPlayersTurn			= item.ActiveGamesPlayersTurn;
	
		return ActiveGamesID;
	}
	
	/**
	* Function to add the given object fields to the request parameters list
	* @param item
	* @param request
	* @return
	*/
/**
* Initialize Dictionary with ActiveGames item instance
* @param dictionary
* @return
*/
public LiJSONObject dictionaryRepresentation(boolean withFK) throws LiErrorHandler {

	try{
		LiJSONObject dictionary = new LiJSONObject();
		dictionary.put(LiFieldActiveGames.ActiveGamesSecondPlayer, ActiveGamesSecondPlayer);
	
		dictionary.put(LiFieldActiveGames.ActiveGamesFirstPlayer, ActiveGamesFirstPlayer);
	
		dictionary.put(LiFieldActiveGames.ActiveGamesLastMoveMadeBy, ActiveGamesLastMoveMadeBy);
	
		dictionary.put(LiFieldActiveGames.ActiveGamesID, ActiveGamesID);
	
		dictionary.put(LiFieldActiveGames.ActiveGamesLastUpdate, LiUtility.convertDateToDictionaryRepresenataion(ActiveGamesLastUpdate));
	
		dictionary.put(LiFieldActiveGames.ActiveGamesGameArrayString, ActiveGamesGameArrayString);
	
		dictionary.put(LiFieldActiveGames.ActiveGamesWantRandomPlayer, ActiveGamesWantRandomPlayer);
	
		dictionary.put(LiFieldActiveGames.ActiveGamesPlayersTurn, ActiveGamesPlayersTurn);
	
		return dictionary;
		}
		catch (LiJSONException ex)
		{
			throw new LiErrorHandler(ApplicasaResponse.INPUT_VALUES_ERROR, ex.getMessage());
		}
	}
	
	public static LiDbObject createDB() throws LiJSONException{
		LiDbObject dbObject = new LiDbObject();
		dbObject.put("LiClassName", kClassName);
		dbObject.put(LiFieldActiveGames.ActiveGamesID, LiCoreDBmanager.PRIMARY_KEY,-1);
		dbObject.put(LiFieldActiveGames.ActiveGamesSecondPlayer, LiCoreDBmanager.TEXT,"");
		dbObject.put(LiFieldActiveGames.ActiveGamesFirstPlayer, LiCoreDBmanager.TEXT,"");
		dbObject.put(LiFieldActiveGames.ActiveGamesLastMoveMadeBy, LiCoreDBmanager.INTEGER,0);
		dbObject.put(LiFieldActiveGames.ActiveGamesLastUpdate, LiCoreDBmanager.DATE,0);
		dbObject.put(LiFieldActiveGames.ActiveGamesGameArrayString, LiCoreDBmanager.INTEGER,0);
		dbObject.put(LiFieldActiveGames.ActiveGamesWantRandomPlayer, LiCoreDBmanager.BOOL,false);
		dbObject.put(LiFieldActiveGames.ActiveGamesPlayersTurn, LiCoreDBmanager.INTEGER,0);
	return dbObject;
}
	public void increment(LiFieldActiveGames liFieldActiveGames) throws LiErrorHandler
	{
		increment(liFieldActiveGames, 1);
	}
		 
	public void increment(LiFieldActiveGames liFieldActiveGames, Object value) throws LiErrorHandler
	{
		String key = liFieldActiveGames.toString();
		float oldValueFloat = 0;
		int oldValueInt = 0;
		Object incrementedField = getActiveGamesFieldbySortType(liFieldActiveGames);
		try {
			if (incrementedField instanceof Integer)
			{
				int incInt;
				if (value instanceof Integer)
					incInt = (Integer)value;
				else
					 throw new LiErrorHandler(ApplicasaResponse.INPUT_VALUES_ERROR, "Incremented Value isn't of the same type as the requested field");
				int total = (Integer)incrementedField+incInt;
				setActiveGamesFieldbySortType(liFieldActiveGames, total);
				if (incrementedFields.has(liFieldActiveGames.toString()))
					oldValueInt = (Integer)incrementedFields.remove(key);
	
				incrementedFields.put(key, (oldValueInt+incInt));
			}
			else if (incrementedField instanceof Float)
			{
				float incFloat;
				 if (value instanceof Float)
					incFloat = (Float)value;
				 else
					incFloat = Float.valueOf((Integer)value);
				float total = (Float)incrementedField+incFloat;
				setActiveGamesFieldbySortType(liFieldActiveGames, total);
					if (incrementedFields.has(liFieldActiveGames.toString()))
						oldValueFloat = (Float)incrementedFields.remove(key);
				incrementedFields.put(key, (oldValueFloat+incFloat));
			}
			else
				throw new LiErrorHandler(ApplicasaResponse.INPUT_VALUES_ERROR,"Can't increase, Specified field is not Int or Float");
		} catch (LiJSONException e) {
			// TODO Auto-generated catch block
			throw new LiErrorHandler(ApplicasaResponse.INPUT_VALUES_ERROR,"Can't increase, Recheck inserted Values");
		}
	}
		 
	private void resetIncrementedFields() {
		// TODO Auto-generated method stub
		incrementedFields = new LiJSONObject();
	}
	

}
