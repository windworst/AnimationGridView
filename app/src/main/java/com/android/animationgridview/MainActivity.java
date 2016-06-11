package com.android.animationgridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
        final GridView gridView = (GridView) findViewById(R.id.gridView);
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
            boolean isLock = false;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (isLock) return;
                Animation animation = getAnimation();
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isLock = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isLock = false;
                        list.remove(position);
                        animationGridViewAdapter.removeItemAnimation(position);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                list.add(position, "" + (++mIndex));
                animationGridViewAdapter.addItemAnimation(position + 1);
                gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        final View selectView = gridView.getChildAt(position - gridView.getFirstVisiblePosition());
                        if (null == selectView) return;
                        selectView.setVisibility(View.INVISIBLE);
                        selectView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                selectView.startAnimation(getAnimation());
                                selectView.setVisibility(View.VISIBLE);
                            }
                        }, 300);
                    }
                });
                return false;
            }
        });
        findViewById(R.id.buttonPanel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("" + (++mIndex));
                animationGridViewAdapter.notifyDataSetChanged();
                gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        View selectView = gridView.getChildAt(gridView.getChildCount() - 1);
                        if (null == selectView) return;
                        selectView.startAnimation(getAnimation());
                    }
                });
            }
        });
    }

    private Animation getAnimation() {
        Animation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        return animation;
    }
}
