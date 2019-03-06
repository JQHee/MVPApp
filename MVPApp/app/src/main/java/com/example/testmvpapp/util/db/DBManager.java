package com.example.testmvpapp.util.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.testmvpapp.R;
import com.loc.db;

/**
 * 外部导入db文件，使用代码将其写入沙盒目录下
 */


/* 导入到相应的目录下
dbHelper = new DBManager(this);
dbHelper.openDatabase();
dbHelper.closeDatabase();

// 使用数据库
SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
*/

public class DBManager {

    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "countries.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.example.testmvpapp";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置

    private SQLiteDatabase database;
    private Context context;

    DBManager(Context context) {
        this.context = context;
    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            if (!(new File(dbfile).exists())) {
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.countries); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }
    //do something else here
    public void closeDatabase() {
        this.database.close();
    }
}
