package com.applicasa.ApplicasaManager;

import java.util.List;
import applicasa.LiCore.LiErrorHandler;
import org.apache.http.NameValuePair;
import com.applicasa.User.User;
import com.applicasa.VirtualCurrency.VirtualCurrency;
import com.applicasa.VirtualGood.VirtualGood;
import com.applicasa.VirtualGoodCategory.VirtualGoodCategory;
import com.applicasa.ActiveGames.ActiveGames;
import com.applicasa.Game.Game;
public class LiCallbackQuery {
	// User Get By Id Callback
	public static interface LiUserGetByIDCallback {
	
		public void onGetUserComplete(User items);
		public void onGetUserFailure(LiErrorHandler error);
	}

	// User GetArray Callback
		public static interface LiUserGetArrayCallback {

		public void onGetUserComplete(List<User> items);
		public void onGetUserFailure(LiErrorHandler error);
	}
	// VirtualCurrency Get By Id Callback
	public static interface LiVirtualCurrencyGetByIDCallback {
	
		public void onGetVirtualCurrencyComplete(VirtualCurrency items);
		public void onGetVirtualCurrencyFailure(LiErrorHandler error);
	}

	// VirtualCurrency GetArray Callback
		public static interface LiVirtualCurrencyGetArrayCallback {

		public void onGetVirtualCurrencyComplete(List<VirtualCurrency> items);
		public void onGetVirtualCurrencyFailure(LiErrorHandler error);
	}
	// VirtualGood Get By Id Callback
	public static interface LiVirtualGoodGetByIDCallback {
	
		public void onGetVirtualGoodComplete(VirtualGood items);
		public void onGetVirtualGoodFailure(LiErrorHandler error);
	}

	// VirtualGood GetArray Callback
		public static interface LiVirtualGoodGetArrayCallback {

		public void onGetVirtualGoodComplete(List<VirtualGood> items);
		public void onGetVirtualGoodFailure(LiErrorHandler error);
	}
	// VirtualGoodCategory Get By Id Callback
	public static interface LiVirtualGoodCategoryGetByIDCallback {
	
		public void onGetVirtualGoodCategoryComplete(VirtualGoodCategory items);
		public void onGetVirtualGoodCategoryFailure(LiErrorHandler error);
	}

	// VirtualGoodCategory GetArray Callback
		public static interface LiVirtualGoodCategoryGetArrayCallback {

		public void onGetVirtualGoodCategoryComplete(List<VirtualGoodCategory> items);
		public void onGetVirtualGoodCategoryFailure(LiErrorHandler error);
	}
	// ActiveGames Get By Id Callback
	public static interface LiActiveGamesGetByIDCallback {
	
		public void onGetActiveGamesComplete(ActiveGames items);
		public void onGetActiveGamesFailure(LiErrorHandler error);
	}

	// ActiveGames GetArray Callback
		public static interface LiActiveGamesGetArrayCallback {

		public void onGetActiveGamesComplete(List<ActiveGames> items);
		public void onGetActiveGamesFailure(LiErrorHandler error);
	}
	// Game Get By Id Callback
	public static interface LiGameGetByIDCallback {
	
		public void onGetGameComplete(Game items);
		public void onGetGameFailure(LiErrorHandler error);
	}

	// Game GetArray Callback
		public static interface LiGameGetArrayCallback {

		public void onGetGameComplete(List<Game> items);
		public void onGetGameFailure(LiErrorHandler error);
	}
}
