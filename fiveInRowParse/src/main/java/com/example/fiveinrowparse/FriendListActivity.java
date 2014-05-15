package com.example.fiveinrowparse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class FriendListActivity extends Activity {
    String OPPONENT_USER_NAME = "opponentUsername";
    String OPPONENT_ID = "opponentId";
    String START_NEW_GAME_INTENT_STRING = "StartnewGame";
    private ArrayList<String> mFriendIdsArrayList = new ArrayList<String>();
    private List<ParseUser> mAllFriendsList;
    private FriendListAdapter mFriendAdapter;
    private ListView mFriendListView;
    private List<ParseObject> mAllFriendRequestsList;
    private FriendRequestAdapter mFriendRequestsAdapter;
    private ListView mFriendRequestsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend_list);

        mFriendIdsArrayList = friendStringToFriendArray(ParseUser.getCurrentUser().getString("friendIds"));
        uppdateFriendRequests();
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

    private void uppdateFriendListView() {
        mFriendIdsArrayList.clear();
        final String friendIdsString = ParseUser.getCurrentUser().getString("friendIds");
        if (friendIdsString != null) {
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
        //Log.d("GameArrayString", friendString);
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

        if (friendName.getText() != null) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", friendName.getText().toString());
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {

                        Toast.makeText(FriendListActivity.this, "found user", Toast.LENGTH_SHORT).show();

                        ParseObject FriendRequest = new ParseObject("FriendRequests");
                        FriendRequest.put("fromName", ParseUser.getCurrentUser().getUsername());
                        FriendRequest.put("fromId", ParseUser.getCurrentUser().getObjectId());
                        FriendRequest.put("toName", objects.get(0).getUsername());
                        FriendRequest.put("toNameIndexing", objects.get(0).getUsername());
                        FriendRequest.put("toId", objects.get(0).getObjectId());
                        FriendRequest.put("status", "Pending");
                        FriendRequest.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    uppdateFriendRequests();
                                    Toast.makeText(FriendListActivity.this, "added user", Toast.LENGTH_SHORT).show();


                                    //TODO send new request push


                                } else {
                                    // The save failed.
                                    Log.d("tag", "Could not save resquest" + e);
                                }
                            }
                        });


                    } else {
                        Toast.makeText(FriendListActivity.this, "found no user with that username!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

    //Lägger till vännen
/*    public void addAFriendButtonClicked(View view) {
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



    }*/

    public void uppdateFriendRequests() {

        //Hämtar alla friend requests där som man antingen skickat eller fått
        ParseQuery<ParseObject> queryTo = ParseQuery.getQuery("FriendRequests");
        queryTo.whereEqualTo("toNameIndexing", ParseUser.getCurrentUser().getUsername());

        ParseQuery<ParseObject> queryFrom = ParseQuery.getQuery("FriendRequests");
        queryFrom.whereEqualTo("fromName", ParseUser.getCurrentUser().getUsername());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(queryTo);
        queries.add(queryFrom);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> friendRequests, ParseException e) {
                if (e == null) {

                    mAllFriendRequestsList = sortOutAcceptedRequestsAndAddTheFriends(friendRequests);

                    mFriendRequestsListView = (ListView) findViewById(R.id.friend_request_list_view);
                    mFriendRequestsAdapter = new FriendRequestAdapter(FriendListActivity.this, mAllFriendRequestsList);
                    mFriendRequestsListView.setAdapter(mFriendRequestsAdapter);
                    mFriendRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {


                            final ParseObject requestToAccept = (ParseObject) mFriendRequestsAdapter.getItem(position);

                            if(requestToAccept.getString("toName").equals(ParseUser.getCurrentUser().getUsername())){

                            showAcceptFriendDialog(requestToAccept);
                            }


                        }

                    });
                } else {
                    Toast.makeText(FriendListActivity.this, "Could not get new friend requests", Toast.LENGTH_SHORT).show();
                    //Log.d("error start old game", e.);
                }
            }


        });
    }

    public List<ParseObject> sortOutAcceptedRequestsAndAddTheFriends(List<ParseObject> friendRequests){

        ArrayList<String> ListOfUsersToFriend = new ArrayList<String>();

        List<ParseObject> ListOfUsersToReturn = friendRequests;
        Log.d("UsersToReturBefore", Integer.toString(ListOfUsersToReturn.size()));


        Iterator<ParseObject> iter = ListOfUsersToReturn.iterator();
        while (iter.hasNext()) {

            ParseObject next = iter.next();
            if (next.getString("status").equals("accepted")) {
                ListOfUsersToFriend.add(next.getString("toId"));
                next.deleteInBackground(); //Raderar objectet från parse
                iter.remove();
            }
        }

       //Log.d("UsersToReturafterTofriend", Integer.toString(ListOfUsersToFriend.size()) + " " + ListOfUsersToFriend.get(0));
      //  Log.d("UsersToReturafter", Integer.toString(ListOfUsersToReturn.size())+ " " + ListOfUsersToReturn.get(0).getString("status"));

        if(ListOfUsersToFriend.size() > 0){

        addAcceptedRequestsAsFriends(ListOfUsersToFriend);
        } else{
            uppdateFriendListView();
        }

        return ListOfUsersToReturn;
    }

    public void addAcceptedRequestsAsFriends(ArrayList<String> friendList){

        ArrayList<String> currentFriendList = friendStringToFriendArray(ParseUser.getCurrentUser().getString("friendIds"));
        currentFriendList.addAll(friendList);
        ParseUser.getCurrentUser().put("friendIds", Arrays.toString(currentFriendList.toArray()));
        ParseUser.getCurrentUser().saveInBackground( new SaveCallback() {
            @Override
            public void done(ParseException e) {
                uppdateFriendListView();

            }
        });


    }

    public void showAcceptFriendDialog(final ParseObject request){
        final ParseObject requestToAccept = request;

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you accept this friend invite?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",

                // lägger till vännen om man klickar ja, ändrar status och uppdaterar listviews
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        mFriendIdsArrayList.add(requestToAccept.getString("fromId"));
                        ParseUser.getCurrentUser().put("friendIds", Arrays.toString(mFriendIdsArrayList.toArray()));
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                requestToAccept.put("status", "accepted");
                                requestToAccept.put("toNameIndexing", "");
                                requestToAccept.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        uppdateFriendRequests();
                                        //uppdateFriendListView();
                                    }
                                });

                            }
                        });

                    }
                }
        );
        builder1.setNegativeButton("No",

                //Ändrar statusen på inviten och uppdaterar listviewn
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        requestToAccept.put("status", "declined");
                        requestToAccept.put("toNameIndexing", "");
                        requestToAccept.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                uppdateFriendRequests();
                            }
                        });

                    }
                });

        AlertDialog acceptGameAlert = builder1.create();
        acceptGameAlert.show();
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

    static class FriendRequestAdapter extends BaseAdapter {


        private Activity mActivity;
        private List<ParseObject> mFriends;

        public FriendRequestAdapter(Activity activity, List<ParseObject> friendRequests) {
            mActivity = activity;

            mFriends = friendRequests;
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
                convertView = View.inflate(mActivity, R.layout.friend_request_list_item, null);
            }

            ParseObject friend = mFriends.get(position);


            if(!friend.getString("fromName").equals(ParseUser.getCurrentUser().getUsername())){
                ((TextView) convertView.findViewById(R.id.from_name)).setText(friend.getString("fromName"));
            }else{
                ((TextView) convertView.findViewById(R.id.from_name)).setText(friend.getString("toName"));
            }

            if (friend.getString("status").equals("Pending")) {
                if (friend.getString("fromName").equals(ParseUser.getCurrentUser().getUsername())) {
                    ((TextView) convertView.findViewById(R.id.status)).setText("Sent");
                }else{
                    ((TextView) convertView.findViewById(R.id.status)).setText("New request");
                }
            } else{
                ((TextView) convertView.findViewById(R.id.status)).setText(friend.getString("status"));
            }


            return convertView;
        }


    }
}
