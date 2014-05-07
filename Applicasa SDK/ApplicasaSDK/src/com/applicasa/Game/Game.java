package com.applicasa.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.GregorianCalendar;

import applicasa.LiCore.communication.LiUtility;


import applicasa.LiCore.communication.LiCallback.LiCallbackAction;
import com.applicasa.ApplicasaManager.LiCallbackQuery.LiGameGetByIDCallback;
import com.applicasa.ApplicasaManager.LiCallbackQuery.LiGameGetArrayCallback;
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



public class Game extends GameData {
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
		if (GameID!= "0" && (LiUtility.isHex(GameID)|| GameID.startsWith("temp_") ))
		{
			request.setAction(RequestAction.UPDATE_ACTION);
			request.setRecordID(GameID);
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
		if (GameID == null || GameID == "") 
			if (liCallbackAction != null) liCallbackAction.onFailure(new LiErrorHandler(ApplicasaResponse.NULL_ITEM, "Missing Item ID"));
			else return;
		
		LiObjRequest request = new LiObjRequest();
		request.setAction(RequestAction.DELETE_ACTION);
		request.setClassName(kClassName);
		request.setCallback(callbackHandler);
		request.setRecordID(GameID);
		
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
	    public static void getByID(String Id, QueryKind queryKind, LiGameGetByIDCallback liGameGetByIDCallback) 
	    {
	        if (Id != null)
	        {
	        	// Creates new Query
	        	LiQuery query= new LiQuery();
	        	
	        	// Create a where statement expression of ObjectId = 'id';  
		        LiFilters filter = new LiFilters(LiFieldGame.GameID, Operation.EQUAL, Id);		        
		        query.setFilter(filter);
	        	
		    	LiObjRequest request = new LiObjRequest();
		        request.setClassName(kClassName);
		
		        request.setAction(RequestAction.GET_ACTION);
		        request.setGet(queryKind);
		       
		        request.setQueryToRequest(query);
		        request.setCallback(callbackHandler);
		        setGetCallback(liGameGetByIDCallback,request.requestID);
		       
		        request.startASync();
	        }
	    }

	    /**
	    * A- Synchronized Method to returns an object from server by filters
	    * @param ID
	    * @return
	    * @throws applicasa.LiCore.LiErrorHandler
	    */
	    public static void getArrayWithQuery(LiQuery query ,QueryKind queryKind, LiGameGetArrayCallback liGameGetArrayCallback) 
	    {
	        LiObjRequest request = new LiObjRequest();
	        request.setClassName(kClassName);
	        request.setAction(RequestAction.GET_ARRAY);
	        request.setGet(queryKind);
	        request.setQueryToRequest(query);
	        request.setCallback(callbackHandler);
	        setGetCallback(liGameGetArrayCallback,request.requestID);
	        request.startASync();
	    }
	   
		public static void getLocalyWithRawSQLQuery(String whereClause, String[] args, LiGameGetArrayCallback liGameGetArrayCallback)
	    {
			LiObjRequest request = new LiObjRequest();
	        request.setCallback(callbackHandler);
	        setGetCallback(liGameGetArrayCallback,request.requestID);
	        request.GetWithRawQuery(kClassName, whereClause, args);
	    }
	
		 /** Synchronized Method to returns an object from server by filters
		 * @param ID
		 * @return the list of items or null in case on an error
		 * @throws applicasa.LiCore.LiErrorHandler
		 */
		 public static List<Game> getArrayWithQuery(LiQuery query ,QueryKind queryKind) throws LiErrorHandler 
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
			  return buildGameFromCursor(request.requestID, cursor);
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
	* @param gameField - The field to be updated with the file name in Applicasa server 
	* @param filePath - the path to the uploaded file
	* @param gameActionCallBack - call back to indicate when the upload was completed
	* @return
	* @throws applicasa.LiCore.LiErrorHandler
	*/
	public void updloadFile(LiFieldGame liFieldGame, String filePath, LiCallbackAction liCallbackAction)
	{
		LiObjRequest request = new LiObjRequest();
		
		request.setAction(RequestAction.UPLOAD_FILE);
		request.setClassName(Game.kClassName);
		request.setRecordID(GameID);
		
		request.setFileFieldName(liFieldGame);
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
		List<Game> returnList = new ArrayList<Game>();

		returnList = buildGameFromCursor(requestID ,cursor);

		Object callback = gameCallbacks.get(requestID);
		if (callback != null && callback instanceof LiGameGetArrayCallback)
		{
			gameCallbacks.remove(requestID);
			((LiGameGetArrayCallback)callback).onGetGameComplete(returnList);
		}
		if (callback != null && callback instanceof LiGameGetByIDCallback )
		{
			gameCallbacks.remove(requestID);
			((LiGameGetByIDCallback)callback).onGetGameComplete(returnList.get(0));
		}
		
	}
	
	public void LiException(String requestID,LiErrorHandler ex) {
		// TODO Auto-generated method stub
		Object callback = gameCallbacks.get(requestID);
		if (callback != null && callback instanceof LiGameGetArrayCallback )
		{
			gameCallbacks.remove(requestID);
			((LiGameGetArrayCallback)callback).onGetGameFailure(ex);
		}		
		else if (callback != null && callback instanceof LiGameGetByIDCallback )
		{
			gameCallbacks.remove(requestID);
			((LiGameGetByIDCallback)callback).onGetGameFailure(ex);
		}
		else if (callback != null && callback instanceof LiCallbackAction )
		{
			gameCallbacks.remove(requestID);
			((LiCallbackAction)callback).onFailure(ex);
		}

	}
		public void onCompleteAction(String requestID, LiObjResponse response) {
			// TODO Auto-generated method stub
			Object callback = gameCallbacks.get(requestID);
			if (callback != null )
			{
				gameCallbacks.remove(requestID);
				if (response.action == RequestAction.ADD_ACTION)
					((Game)response.addedObject).GameID = response.newObjID;
					
				if (response.action == RequestAction.UPLOAD_FILE)
				{
					((Game)response.addedObject).setGameFieldbySortType((LiFieldGame)response.field, response.newObjID);
					if (response.actionResponseList.get(0).objId != null && response.actionResponseList.get(0).requestID == requestID )
						((Game)response.addedObject).GameID = response.actionResponseList.get(0).objId;
				}
								
				((LiCallbackAction)callback).onComplete(response.LiRespType, response.LiRespMsg, response.action,response.newObjID, LiObject.getLiObject(response.className));
			}
		}
	};
	
	/**
	 * 
	 * @param requestID
	 * @param cursor
	 * @deprecated use buildGameFromCursor
	 * @return
	 */
	@Deprecated
	public static List<Game> BuildGameFromCursor(String requestID, Cursor cursor)
	{
		return buildGameFromCursor(requestID, cursor);
	}

	/**
	 * 
	 * @param requestID
	 * @param cursor
	 * @return
	 */
	public static List<Game> buildGameFromCursor(String requestID ,Cursor cursor)
	{
		List<Game> returnList = new ArrayList<Game>();
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
                    returnList.add(new Game(cursor));                    
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
	
	
	private static void setGetCallback(LiGameGetArrayCallback getCallback, String reqID) {
		// TODO Auto-generated method stub
		gameCallbacks.put(reqID, getCallback);
	}
	
	private static void setGetCallback(LiGameGetByIDCallback getCallback, String reqID) {
		// TODO Auto-generated method stub
		gameCallbacks.put(reqID, getCallback);
	}

	private static void setActionCallback(LiCallbackAction actionCallback, String reqID) {
		// TODO Auto-generated method stub
		gameCallbacks.put(reqID, actionCallback);
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
	public Game()
	{
		this.GameID = "0";
		(this.GameLastUpdate = new GregorianCalendar()).setTimeInMillis(0);
		this.GameFirstPlayer = "";
		this.GameSecondPlayer = "";
		this.GamePlayersTurn = "";
		this.GameWantRandomPlayer = true;
		this.GameGameArrayString = "";
	}

	public Game(Cursor cursor) 
	{
		initWithCursor(cursor);
	}
	
	public Game(Cursor cursor,String header,int level) 
	{
		initWithCursor(cursor,header,level);
	}
	
	public Game(String GameID)
	{
		this.GameID = GameID;
	}

	public Game(Game item)
	{
		initWithObject(item);
	}

	/**
	* Init Object with Cursor
	* @param corsor
	* @return
	*/
	public Game initWithCursor(Cursor cursor)
	{
		return initWithCursor(cursor,"",0);
	}
	
	/**
	* Init Object with Cursor
	* @param corsor
	* @return
	*/
	public Game initWithCursor(Cursor cursor,String header,int level)
	{
		int columnIndex;
	
		columnIndex = cursor.getColumnIndex(header + LiFieldGame.GameID.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.GameID = cursor.getString(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldGame.GameLastUpdate.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
		{
			long dateStr = cursor.getLong(columnIndex);
			GregorianCalendar gc= new GregorianCalendar();
			gc.setTimeInMillis(dateStr);
			this.GameLastUpdate = gc;
		}
		
		columnIndex = cursor.getColumnIndex(header + LiFieldGame.GameFirstPlayer.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.GameFirstPlayer = cursor.getString(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldGame.GameSecondPlayer.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.GameSecondPlayer = cursor.getString(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldGame.GamePlayersTurn.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.GamePlayersTurn = cursor.getString(columnIndex);
		
		columnIndex = cursor.getColumnIndex(header + LiFieldGame.GameWantRandomPlayer.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
		{
			this.GameWantRandomPlayer = cursor.getInt(columnIndex)==1?true:false;
		}
		
		columnIndex = cursor.getColumnIndex(header + LiFieldGame.GameGameArrayString.toString());
		if (columnIndex != LiCoreDBmanager.COLUMN_NOT_EXIST)
			this.GameGameArrayString = cursor.getString(columnIndex);
		
	
		return this;
	}
	
	/**
	* Initialize values with Object
	* @param item
	* @return
	*/
	public String initWithObject(Game item)
	{
		this.GameID			= item.GameID;
		this.GameLastUpdate			= item.GameLastUpdate;
		this.GameFirstPlayer			= item.GameFirstPlayer;
		this.GameSecondPlayer			= item.GameSecondPlayer;
		this.GamePlayersTurn			= item.GamePlayersTurn;
		this.GameWantRandomPlayer			= item.GameWantRandomPlayer;
		this.GameGameArrayString			= item.GameGameArrayString;
	
		return GameID;
	}
	
	/**
	* Function to add the given object fields to the request parameters list
	* @param item
	* @param request
	* @return
	*/
/**
* Initialize Dictionary with Game item instance
* @param dictionary
* @return
*/
public LiJSONObject dictionaryRepresentation(boolean withFK) throws LiErrorHandler {

	try{
		LiJSONObject dictionary = new LiJSONObject();
		dictionary.put(LiFieldGame.GameID, GameID);
	
		dictionary.put(LiFieldGame.GameLastUpdate, LiUtility.convertDateToDictionaryRepresenataion(GameLastUpdate));
	
		dictionary.put(LiFieldGame.GameFirstPlayer, GameFirstPlayer);
	
		dictionary.put(LiFieldGame.GameSecondPlayer, GameSecondPlayer);
	
		dictionary.put(LiFieldGame.GamePlayersTurn, GamePlayersTurn);
	
		dictionary.put(LiFieldGame.GameWantRandomPlayer, GameWantRandomPlayer);
	
		dictionary.put(LiFieldGame.GameGameArrayString, GameGameArrayString);
	
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
		dbObject.put(LiFieldGame.GameID, LiCoreDBmanager.PRIMARY_KEY,-1);
		dbObject.put(LiFieldGame.GameLastUpdate, LiCoreDBmanager.DATE,0);
		dbObject.put(LiFieldGame.GameFirstPlayer, LiCoreDBmanager.TEXT,"");
		dbObject.put(LiFieldGame.GameSecondPlayer, LiCoreDBmanager.TEXT,"");
		dbObject.put(LiFieldGame.GamePlayersTurn, LiCoreDBmanager.TEXT,"");
		dbObject.put(LiFieldGame.GameWantRandomPlayer, LiCoreDBmanager.BOOL,true);
		dbObject.put(LiFieldGame.GameGameArrayString, LiCoreDBmanager.TEXT,"");
	return dbObject;
}
	public void increment(LiFieldGame liFieldGame) throws LiErrorHandler
	{
		increment(liFieldGame, 1);
	}
		 
	public void increment(LiFieldGame liFieldGame, Object value) throws LiErrorHandler
	{
		String key = liFieldGame.toString();
		float oldValueFloat = 0;
		int oldValueInt = 0;
		Object incrementedField = getGameFieldbySortType(liFieldGame);
		try {
			if (incrementedField instanceof Integer)
			{
				int incInt;
				if (value instanceof Integer)
					incInt = (Integer)value;
				else
					 throw new LiErrorHandler(ApplicasaResponse.INPUT_VALUES_ERROR, "Incremented Value isn't of the same type as the requested field");
				int total = (Integer)incrementedField+incInt;
				setGameFieldbySortType(liFieldGame, total);
				if (incrementedFields.has(liFieldGame.toString()))
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
				setGameFieldbySortType(liFieldGame, total);
					if (incrementedFields.has(liFieldGame.toString()))
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
