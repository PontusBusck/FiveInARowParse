package com.example.fiveinrowparse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FriendListActivity extends Activity {
    private ArrayList<String> mFriendIdsArrayList = new ArrayList<String>();
    private List<ParseUser> mAllFriendsList;
    private FriendListAdapter mFriendAdapter;
    private ListView mFriendListView;
    String OPPONENT_USER_NAME = "opponentUsername";
    String OPPONENT_ID = "opponentId";
    String START_NEW_GAME_INTENT_STRING = "StartnewGame";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend_list);

        uppdateFriendListView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uppdateFriendListView(){
        mFriendIdsArrayList.clear();
        final String friendIdsString = ParseUser.getCurrentUser().getString("friendIds");
        if(friendIdsString != null){
        mFriendIdsArrayList = friendStringToFriendArray(friendIdsString);

        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("objectId", mFriendIdsArrayList);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    Log.d("FriendObject", Integer.toString(friends.size()));
                    //mAllFriendsList.clear();
                    mAllFriendsList = friends;

                    mFriendListView = (ListView) findViewById(R.id.friend_list_view);
                    mFriendAdapter = new FriendListAdapter(FriendListActivity.this, mAllFriendsList);
                    mFriendListView.setAdapter(mFriendAdapter);
                    mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            ParseUser friendToChallange = (ParseUser) mFriendAdapter.getItem(position);
                            Intent intent = new Intent(FriendListActivity.this, OnlineGameActivity.class);
                            intent.putExtra(OPPONENT_USER_NAME, friendToChallange.getUsername());
                            intent.putExtra(OPPONENT_ID, friendToChallange.getObjectId());
                            intent.putExtra(START_NEW_GAME_INTENT_STRING, true);

                            startActivity(intent);
                        }

                    });
                } else {
                    Log.d("friendObject", "wentWrong");
                }
            }
        });

    }


    //Tar strängen med vänner och gör den till en array

    private ArrayList<String> friendStringToFriendArray(String friendString) {
        Log.d("GameArrayString", friendString);
        String currentFriendString = friendString.replace("[", "");
        currentFriendString = currentFriendString.replace("]", "");
        currentFriendString = currentFriendString.replace(" ", "");
        //String[] friendStringArray = currentFriendString.split(",");
        ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(currentFriendString.split(",")));

        /*int[] gameIntArray = new int[friendStringArray.length];
        for (int i = 0; i < friendStringArray.length; i++) {

            gameIntArray[i] = Integer.parseInt(friendStringArray[i]);

        }*/

        return stringList;

    }

    public void addAFriendButtonClicked(View view) {
        EditText friendName = (EditText) findViewById(R.id.search_friend_textfield);

        if(friendName.getText() != null){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", friendName.getText().toString());
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        mFriendIdsArrayList.add(objects.get(0).getObjectId());
                        mAllFriendsList.add(objects.get(0));
                        mFriendAdapter.notifyDataSetChanged();

                        ParseUser.getCurrentUser().put("friendIds", Arrays.toString(mFriendIdsArrayList.toArray()));
                        ParseUser.getCurrentUser().saveInBackground();


                    } else {
                        Toast.makeText(FriendListActivity.this, "found no user with that username!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



    }


    static class FriendListAdapter extends BaseAdapter {


        private Activity mActivity;
        private List<ParseUser> mFriends;

        public FriendListAdapter(Activity activity, List<ParseUser> friends) {
            mActivity = activity;

            mFriends = friends;
        }

        @Override
        public int getCount() {
            return mFriends.size();
        }

        @Override
        public Object getItem(int position) {
            return mFriends.get(position);
        }


        @Override
        public long getItemId(int position) {
            //return mGames.get(position).getId();
            return 9; //temp
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.friend_list_item, null);
            }

            ParseUser friend = mFriends.get(position);


            ((TextView) convertView.findViewById(R.id.friend_name)).setText(friend.getUsername());




            return convertView;
        }


    }
}
