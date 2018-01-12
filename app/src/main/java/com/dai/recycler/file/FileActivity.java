package com.dai.recycler.file;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dai.recycler.BuildConfig;
import com.dai.recycler.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dai on 2017/9/15.
 */

public class FileActivity extends AppCompatActivity {

    @BindView(R.id.write_info)
    EditText writeInfo;
    @BindView(R.id.write)
    TextView write;
    @BindView(R.id.read)
    TextView read;
    @BindView(R.id.show_data)
    TextView showData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.write, R.id.read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.write:
                writeInfo();
                break;
            case R.id.read:
                readInfo();
                break;
        }
    }

    private void readInfo() {
        String fileName = "test1.txt";
        String path = Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID + File.separator + "file" + File.separator + fileName;

        File file = new File(path);
        System.out.println("file.getName() = " + file.getName());
        System.out.println("file.getPath() = " + file.getPath());
        System.out.println("file.isFile() = " + file.isFile());
        String content = null;
        try {
            //读取文件数据 方法一：
//            FileReader fr = new FileReader(file);
//            BufferedReader br = new BufferedReader(fr);

            // 读取文件数据 方法二 读取assets 文件夹下的数据
            InputStream is = getResources().getAssets().open("file/test.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            //通用步骤
            StringBuilder stringBuilder = new StringBuilder();
            while ((content = br.readLine()) != null) {
                System.out.println("br.readLine() = " + content);
                stringBuilder.append(content).append("\r\n");
            }
            System.out.println("stringBuilder.toString() = " + stringBuilder.toString());
            showData.setText(stringBuilder.toString());


            // 方法一
//            br.close();
//            fr.close();
            // 方法二  后打开的先关闭，逐层向内关闭
            is.close();
            isr.close();
            br.close();


            //方法三 打开内部文件数据
//            FileInputStream fis=openFileInput(file);
//            FileInputStream fis = new FileInputStream(file);
//            InputStreamReader inputStreamReader=new InputStreamReader(fis,"UTF-8");
//            //fis.available()文件可用长度
//            byte input[]=new byte[fis.available()];
//            is.read(input);
//            is.close();
//            fis.close();
//            String readed=new String(input);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "文件没有找到", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void writeInfo() {

        String fileName = "test1.txt";
        String path = Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID + File.separator + "file" + File.separator;
        System.out.println("path = " + path);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(path + fileName);
        if (!file1.exists()) {
            try {
                boolean isCreate = file1.createNewFile();
                System.out.println("isCreate = " + isCreate);
                System.out.println("canWrite = " + file1.canWrite());
                System.out.println("canRead = " + file1.canRead());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            File file2 = getFilesDir().getParentFile();
            FileWriter fw = new FileWriter(file1, true);//以追加的模式将字符写入
            BufferedWriter bw = new BufferedWriter(fw);//又包裹一层缓冲流 增强IO功能
            bw.write(writeInfo.getText().toString());
            bw.flush();//将内容一次性写入文件
            bw.close();
            fw.close();
            FileOutputStream outputStream = new FileOutputStream(file1, true);
//            outputStream = new File(getExternalFilesDir(null), "tours") ;
//            FileOutputStream outputStream = openFileOutput(path+fileName, MODE_APPEND);
            //把文件中的所有内容转换为byte字节数组
//            System.out.println("writeInfo.getText().toString() = " + writeInfo.getText().toString());
//            byte[] bytes = writeInfo.getText().toString().getBytes();
//            outputStream.write(bytes);
//            outputStream.close();

            //保存在手机data/data/包名/files
//            FileOutputStream fos=openFileOutput(fileName, Context.MODE_PRIVATE);
//            OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
//            osw.write(et.getText().toString());
//            //保证输出缓冲区中的所有内容
//            osw.flush();
//            fos.flush();
//            //后打开的先关闭，逐层向内关闭
//            fos.close();
//            osw.close();
            Toast.makeText(getApplicationContext(), "写入成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
