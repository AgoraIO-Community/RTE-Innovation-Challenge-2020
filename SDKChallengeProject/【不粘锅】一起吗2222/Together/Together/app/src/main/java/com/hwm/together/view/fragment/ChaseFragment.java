package com.hwm.together.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hwm.together.R;
import com.hwm.together.adapter.ChaseAdapter;
import com.hwm.together.chase.Chase;
import com.hwm.together.util.OnItemClickCallback;
import com.hwm.together.util.SpaceItemDecoration;
import com.hwm.together.view.activity.TetrisActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 追番
 */
public class ChaseFragment extends android.support.v4.app.Fragment {
    ImageView imgTetris;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chase, container, false);
        imgTetris = view.findViewById(R.id.fg_chase_img_tetris);
        imgTetris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TetrisActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
