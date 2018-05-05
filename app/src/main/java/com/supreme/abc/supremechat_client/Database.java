package com.supreme.abc.supremechat_client;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supreme.abc.supremechat_client.GroupChat.FriendGroup;

import java.lang.reflect.Type;
import java.util.Hashtable;

import network_data.Friend;

public class Database {

    public static Database instance = new Database();

    //private SharedPreferences Prefs;
    private SharedPreferences appSharedPrefs;
    private Gson gson;

    public Database() {

        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(MyApplication.getAppContext());
        gson = new Gson();
    }

    public Hashtable<String, Friend> LoadFriendList() {
        String json = appSharedPrefs.getString( User.mainUser.getUsername()+"FriendList" , null);

        //if no friend list is saved
        if(json == null)
            return new Hashtable<>();

        //if there's friend list saved
        Type type = new TypeToken<Hashtable<String, Friend>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void SaveFriendList(Hashtable<String, Friend> friend_list) {
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        String json = gson.toJson(friend_list);
        prefsEditor.putString(User.mainUser.getUsername()+"FriendList", json);
        prefsEditor.apply();
    }

    public Hashtable<String, FriendGroup> LoadGroupFriendList() {
        String json = appSharedPrefs.getString( User.mainUser.getUsername()+"GroupFriendList" , null);

        //if no friend list is saved
        if(json == null)
            return new Hashtable<>();

        //if there's friend list saved
        Type type = new TypeToken<Hashtable<String, FriendGroup>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void SaveGroupFriendList(Hashtable<String, FriendGroup> friend_list) {
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        String json = gson.toJson(friend_list);
        prefsEditor.putString(User.mainUser.getUsername()+"GroupFriendList", json);
        prefsEditor.apply();
    }

}