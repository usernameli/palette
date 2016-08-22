package com.seewo.palette;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seewo.palette.activity.PaintActivity;
import com.seewo.palette.adapter.CommonAdapter;
import com.seewo.palette.adapter.ViewHolder;
import com.seewo.palette.util.StoreOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    GridView mGridview;
    ImageButton mImageButton;
    List<Bitmap> ImageList = new ArrayList<>();
    List<String> NameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        //从SD卡中读出图片数据
        File scanFilePath = new File(Environment.getExternalStorageDirectory() + "/palette");
        if (scanFilePath.isDirectory()) {
            for (File file : scanFilePath.listFiles()) {
                String filename = file.getAbsolutePath();
                if (file.isDirectory()) {
                    NameList.add(filename);
                }
            }
        }

    }

    /**
     * 初始化相关组件
     */
    private void initView() {

        initGridview();
        mImageButton = (ImageButton) findViewById(R.id.id_create_btn);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转绘图界面,传过去的值为null
                Intent intent = new Intent(MainActivity.this, PaintActivity.class);
                String message=null;
                intent.putExtra("openfilename",message);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化Gridview
     */
    private void initGridview() {
        //主要是完成缩略图的加载
        mGridview = (GridView) findViewById(R.id.id_gridview);
        mGridview.setAdapter(new CommonAdapter<String>(this, NameList, R.layout.gridview_item) {
            @Override
            protected void convert(ViewHolder holder, String item) {
                TextView tv=holder.getView(R.id.id_gallery_name);
                String name=item.substring(item.lastIndexOf("/")+1);
                tv.setText(name);
            }
        });

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取出当前对应文件夹名字
                String DirName=NameList.get(position);
                //跳转绘制界面之后实时加载，只是传文件名过去
                Intent intent = new Intent(MainActivity.this,PaintActivity.class);
                intent.putExtra("openfilename",DirName);
                startActivity(intent);
            }
        });
    }


}
