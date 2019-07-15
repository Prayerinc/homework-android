package com.example.chapter3.homework;



import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;
public class PlaceholderFragment extends Fragment {

    private LottieAnimationView animationView;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件

        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        animationView = view.findViewById(R.id.animation_view);  //引入第一题的动画控件
        listView=view.findViewById(R.id.rv_list);
        listView.setAdapter(new ListViewAdapter(container.getContext()));
        listView.setVisibility(View.INVISIBLE);
        return view;
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {

                animationView.setVisibility(View.INVISIBLE);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(animationView, //淡入
                        "alpha", 1f, 0f);


                animator1.setDuration(2000);
                animator1.setInterpolator(new LinearInterpolator());

                listView.setVisibility(View.VISIBLE);//这个时候可以看到消息列表了

                ObjectAnimator animator2 = ObjectAnimator.ofFloat(listView,  //淡出
                        "alpha", 0f, 1f);


                animator2.setDuration(2000);
                animator2.setInterpolator(new LinearInterpolator());

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator1, animator2);
                animatorSet.start();

            }
        },5000);
    }


public static class ListViewAdapter extends BaseAdapter {



    private Context mContext;

    public ListViewAdapter(Context context) {
        mContext = context;
    }

    @Override public int getCount() {
        return 10;
    }

    @Override public Object getItem(int position) {
        return null;
    }

    @Override public long getItemId(int position) {
        return 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.im_list_item, null);

        } else {
            //!=null
            view = convertView;
        }

        return view;
    }
}


}

