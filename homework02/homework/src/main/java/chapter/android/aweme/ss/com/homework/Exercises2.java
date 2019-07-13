package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate= LayoutInflater.from(this).inflate(R.layout.activity_chatroom,null);
        setContentView(inflate);
        int viewcount=getViewCount(inflate);//导入了一个文件
        System.out.println("There are "+viewcount+" views in total");
    }

    public static int getViewCount(View view) {
        //todo 补全你的代码
        int count=0;
        if(null==view){
            return 0;
        }
        if (view instanceof ViewGroup){
            count++;
            for(int i=0;i<((ViewGroup) view).getChildCount();i++)
            {
                View tview = ((ViewGroup) view).getChildAt(i);
                if(tview instanceof ViewGroup){
                    count+=getViewCount(tview);
                }
                else{
                    count++;//如果它本身就是个view就说明来到了叶子结点
                }
            }

        }

        return count;
    }
}
