package com.applicasa.ActiveGames;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import applicasa.LiCore.LiLocation;
import applicasa.LiCore.LiField;
import applicasa.LiJson.LiJSONObject;

public class ActiveGamesData {


	protected static Map<String, LiFieldActiveGames> stringMap = new HashMap<String, LiFieldActiveGames>();
	LiJSONObject incrementedFields = new LiJSONObject();
	public static boolean EnableOffline = true;
	public enum LiFieldActiveGames implements LiField
	{
		ActiveGames_None
	, ActiveGamesSecondPlayer
	, ActiveGamesFirstPlayer
	, ActiveGamesLastMoveMadeBy
	, ActiveGamesID
	, ActiveGamesLastUpdate
	, ActiveGamesGameArrayString
	, ActiveGamesWantRandomPlayer
	, ActiveGamesPlayersTurn

	;

		private LiFieldActiveGames() {
			stringMap.put(this.toString(), this);
		}

		public static LiFieldActiveGames getLiFieldActiveGames(String key) {
			return stringMap.get(key);
	}
	}

	protected static Map<String, Object > activeGamesCallbacks = new HashMap<String, Object>();
	//Class Name 
	public final static String kClassName                =  "ActiveGames";
	
	////
	//// Class fields name - Static Fields
	////
	////
	////
		public String ActiveGamesSecondPlayer;
	
		public String ActiveGamesFirstPlayer;
	
		public int ActiveGamesLastMoveMadeBy;
	
		public String ActiveGamesID;
	
		public GregorianCalendar ActiveGamesLastUpdate;
	
		public int ActiveGamesGameArrayString;
	
		public Boolean ActiveGamesWantRandomPlayer;
	
		public int ActiveGamesPlayersTurn;
	
	
		public String getActiveGamesSecondPlayer() {
			return ActiveGamesSecondPlayer;
		}
		
		public void setActiveGamesSecondPlayer(String ActiveGamesSecondPlayer) {
			this.ActiveGamesSecondPlayer = ActiveGamesSecondPlayer;
		}
		
		public String getActiveGamesFirstPlayer() {
			return ActiveGamesFirstPlayer;
		}
		
		public void setActiveGamesFirstPlayer(String ActiveGamesFirstPlayer) {
			this.ActiveGamesFirstPlayer = ActiveGamesFirstPlayer;
		}
		
		public int getActiveGamesLastMoveMadeBy() {
			return ActiveGamesLastMoveMadeBy;
		}
		
		public void setActiveGamesLastMoveMadeBy(int ActiveGamesLastMoveMadeBy) {
			this.ActiveGamesLastMoveMadeBy = ActiveGamesLastMoveMadeBy;
		}
		
		public String getActiveGamesID() {
			return ActiveGamesID;
		}
		
		public void setActiveGamesID(String ActiveGamesID) {
			this.ActiveGamesID = ActiveGamesID;
		}
		
		public GregorianCalendar getActiveGamesLastUpdate() {
			return ActiveGamesLastUpdate;
		}
		
		public void setActiveGamesLastUpdate(GregorianCalendar ActiveGamesLastUpdate) {
			this.ActiveGamesLastUpdate = ActiveGamesLastUpdate;
		}
		
		public int getActiveGamesGameArrayString() {
			return ActiveGamesGameArrayString;
		}
		
		public void setActiveGamesGameArrayString(int ActiveGamesGameArrayString) {
			this.ActiveGamesGameArrayString = ActiveGamesGameArrayString;
		}
		
		public Boolean getActiveGamesWantRandomPlayer() {
			return ActiveGamesWantRandomPlayer;
		}
		
		public void setActiveGamesWantRandomPlayer(Boolean ActiveGamesWantRandomPlayer) {
			this.ActiveGamesWantRandomPlayer = ActiveGamesWantRandomPlayer;
		}
		
		public int getActiveGamesPlayersTurn() {
			return ActiveGamesPlayersTurn;
		}
		
		public void setActiveGamesPlayersTurn(int ActiveGamesPlayersTurn) {
			this.ActiveGamesPlayersTurn = ActiveGamesPlayersTurn;
		}
		
		public static String getActiveGamesSortField(LiFieldActiveGames field)
		{
			return field.toString();
		}
	public Object getActiveGamesFieldbySortType(LiFieldActiveGames field)
	{
		switch (field){
			case ActiveGames_None:
				return ActiveGamesID;
				
			case ActiveGamesSecondPlayer:
				return ActiveGamesSecondPlayer;
				
			case ActiveGamesFirstPlayer:
				return ActiveGamesFirstPlayer;
				
			case ActiveGamesLastMoveMadeBy:
				return ActiveGamesLastMoveMadeBy;
				
			case ActiveGamesID:
				return ActiveGamesID;
				
			case ActiveGamesLastUpdate:
				return ActiveGamesLastUpdate;
				
			case ActiveGamesGameArrayString:
				return ActiveGamesGameArrayString;
				
			case ActiveGamesWantRandomPlayer:
					return ActiveGamesWantRandomPlayer;
					
			case ActiveGamesPlayersTurn:
				return ActiveGamesPlayersTurn;
				
			default:
				return "";
		}
		
	}
	
	protected boolean setActiveGamesFieldbySortType(LiFieldActiveGames field, Object value)
	{
		switch (field){
			case ActiveGames_None:
				break;
				
			case ActiveGamesSecondPlayer:
					ActiveGamesSecondPlayer = (String)value;
					break;
					
			case ActiveGamesFirstPlayer:
					ActiveGamesFirstPlayer = (String)value;
					break;
					
			case ActiveGamesLastMoveMadeBy:
					ActiveGamesLastMoveMadeBy = (Integer)value;
					break;
					
			case ActiveGamesID:
					ActiveGamesID = (String)value;
					break;
					
			case ActiveGamesLastUpdate:
					ActiveGamesLastUpdate = (GregorianCalendar)value;
					break;
				
			case ActiveGamesGameArrayString:
					ActiveGamesGameArrayString = (Integer)value;
					break;
					
			case ActiveGamesWantRandomPlayer:
					ActiveGamesWantRandomPlayer = (Boolean)value;
					break;
					
			case ActiveGamesPlayersTurn:
					ActiveGamesPlayersTurn = (Integer)value;
					break;
					
			default:
				break;
		}
		return true;
	}
}
