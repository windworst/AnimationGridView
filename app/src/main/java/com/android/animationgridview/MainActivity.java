package com.android.animationgridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        final List<String> list = new ArrayList<>();
        list.add("" + (++mIndex));
        final AnimationGridViewAdapter animationGridViewAdapter = new AnimationGridViewAdapter(gridView) {
            @Override
            protected View getConvertView(final int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (null == convertView) {
                    convertView = textView = new TextView(MainActivity.this);
                } else {
                    textView = (TextView) convertView;
                }
                textView.setText(list.get(position));
                textView.setPadding(10, 10, 10, 10);
                textView.setGravity(Gravity.CENTER);
                return convertView;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }
        };
        gridView.setAdapter(animationGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.remove(position);
                animationGridViewAdapter.removeItemAnimation(position);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                list.add(position, "" + (++mIndex));
                animationGridViewAdapter.addItemAnimation(position + 1);
                return false;
            }
        });
        findViewById(R.id.buttonPanel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("" + (++mIndex));
                animationGridViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
