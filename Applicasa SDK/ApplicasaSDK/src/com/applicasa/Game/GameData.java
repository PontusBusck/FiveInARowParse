package com.applicasa.Game;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import applicasa.LiCore.LiLocation;
import applicasa.LiCore.LiField;
import applicasa.LiJson.LiJSONObject;

public class GameData {


	protected static Map<String, LiFieldGame> stringMap = new HashMap<String, LiFieldGame>();
	LiJSONObject incrementedFields = new LiJSONObject();
	public static boolean EnableOffline = true;
	public enum LiFieldGame implements LiField
	{
		Game_None
	, GameID
	, GameLastUpdate
	, GameFirstPlayer
	, GameSecondPlayer
	, GamePlayersTurn
	, GameWantRandomPlayer
	, GameGameArrayString

	;

		private LiFieldGame() {
			stringMap.put(this.toString(), this);
		}

		public static LiFieldGame getLiFieldGame(String key) {
			return stringMap.get(key);
	}
	}

	protected static Map<String, Object > gameCallbacks = new HashMap<String, Object>();
	//Class Name 
	public final static String kClassName                =  "Game";
	
	////
	//// Class fields name - Static Fields
	////
	////
	////
		public String GameID;
	
		public GregorianCalendar GameLastUpdate;
	
		public String GameFirstPlayer;
	
		public String GameSecondPlayer;
	
		public String GamePlayersTurn;
	
		public Boolean GameWantRandomPlayer;
	
		public String GameGameArrayString;
	
	
		public String getGameID() {
			return GameID;
		}
		
		public void setGameID(String GameID) {
			this.GameID = GameID;
		}
		
		public GregorianCalendar getGameLastUpdate() {
			return GameLastUpdate;
		}
		
		public void setGameLastUpdate(GregorianCalendar GameLastUpdate) {
			this.GameLastUpdate = GameLastUpdate;
		}
		
		public String getGameFirstPlayer() {
			return GameFirstPlayer;
		}
		
		public void setGameFirstPlayer(String GameFirstPlayer) {
			this.GameFirstPlayer = GameFirstPlayer;
		}
		
		public String getGameSecondPlayer() {
			return GameSecondPlayer;
		}
		
		public void setGameSecondPlayer(String GameSecondPlayer) {
			this.GameSecondPlayer = GameSecondPlayer;
		}
		
		public String getGamePlayersTurn() {
			return GamePlayersTurn;
		}
		
		public void setGamePlayersTurn(String GamePlayersTurn) {
			this.GamePlayersTurn = GamePlayersTurn;
		}
		
		public Boolean getGameWantRandomPlayer() {
			return GameWantRandomPlayer;
		}
		
		public void setGameWantRandomPlayer(Boolean GameWantRandomPlayer) {
			this.GameWantRandomPlayer = GameWantRandomPlayer;
		}
		
		public String getGameGameArrayString() {
			return GameGameArrayString;
		}
		
		public void setGameGameArrayString(String GameGameArrayString) {
			this.GameGameArrayString = GameGameArrayString;
		}
		
		public static String getGameSortField(LiFieldGame field)
		{
			return field.toString();
		}
	public Object getGameFieldbySortType(LiFieldGame field)
	{
		switch (field){
			case Game_None:
				return GameID;
				
			case GameID:
				return GameID;
				
			case GameLastUpdate:
				return GameLastUpdate;
				
			case GameFirstPlayer:
				return GameFirstPlayer;
				
			case GameSecondPlayer:
				return GameSecondPlayer;
				
			case GamePlayersTurn:
				return GamePlayersTurn;
				
			case GameWantRandomPlayer:
					return GameWantRandomPlayer;
					
			case GameGameArrayString:
				return GameGameArrayString;
				
			default:
				return "";
		}
		
	}
	
	protected boolean setGameFieldbySortType(LiFieldGame field, Object value)
	{
		switch (field){
			case Game_None:
				break;
				
			case GameID:
					GameID = (String)value;
					break;
					
			case GameLastUpdate:
					GameLastUpdate = (GregorianCalendar)value;
					break;
				
			case GameFirstPlayer:
					GameFirstPlayer = (String)value;
					break;
					
			case GameSecondPlayer:
					GameSecondPlayer = (String)value;
					break;
					
			case GamePlayersTurn:
					GamePlayersTurn = (String)value;
					break;
					
			case GameWantRandomPlayer:
					GameWantRandomPlayer = (Boolean)value;
					break;
					
			case GameGameArrayString:
					GameGameArrayString = (String)value;
					break;
					
			default:
				break;
		}
		return true;
	}
}
