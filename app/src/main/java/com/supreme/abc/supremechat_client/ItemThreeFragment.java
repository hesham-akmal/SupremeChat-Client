package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.supreme.abc.supremechat_client.FriendChat.FriendListAdapter;
import com.supreme.abc.supremechat_client.FriendChat.FriendListFrag;
import com.supreme.abc.supremechat_client.Networking.Network;

import static android.content.Context.MODE_PRIVATE;

public class ItemThreeFragment extends Fragment {
    public static ItemThreeFragment newInstance() {
        ItemThreeFragment fragment = new ItemThreeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_frag, container, false);

        Button logOutBtn = rootView.findViewById(R.id.logOut);
        logOutBtn.setOnClickListener(view -> LogOut());

        return rootView;
    }

    private void LogOut()
    {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("ABC_key", MODE_PRIVATE).edit();
        editor.putBoolean("keep", false);
        editor.putString("username", null);
        editor.apply();
        FriendListFrag.tempUser.clear();//reset
        Network.instance.StopHeartbeatService();
        startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
        getActivity().finish();
    }
}