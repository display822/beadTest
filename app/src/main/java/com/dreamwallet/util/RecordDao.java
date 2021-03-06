package com.dreamwallet.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dreamwallet.entity.MoneyRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 *
 */

public class RecordDao {

    private Context mContext;
    private WalletDB dbHelper;

    public RecordDao(Context context) {
        mContext = context;

        dbHelper = new WalletDB(context);
    }

    /**
     *
     * 日期存储的格式是2017-09-01
     */
    public void insertRecord(MoneyRecord m){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("type", m.getType());
        cv.put("money", m.getMoney());
        cv.put("record_date", m.getRecord_date());
        cv.put("comment", m.getComment());

        db.insert(WalletDB.TABLE_NAME, null, cv);

        db.close();
    }

    public void deleteRecord(MoneyRecord m){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(WalletDB.TABLE_NAME, "comment = ? and money = ? and record_date = ?", new String[]{m.getComment(), String.valueOf(m.getMoney()), m.getRecord_date()});
        db.close();
    }

    public List<MoneyRecord> selectRecordByDate(String firstDay, String lastDay){


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //"where datetime(record_date) > ? and datetime(record_date) < ?"
        Cursor cursor = db.query(WalletDB.TABLE_NAME, new String[]{"type", "money", "record_date", "comment"},
                "record_date >= ? and record_date <= ?", new String[]{firstDay, lastDay}, null, null, "record_date asc");

        if (cursor.getCount() > 0) {
            List<MoneyRecord> orderList = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                MoneyRecord record = new MoneyRecord();
                record.setType(cursor.getInt(0));
                record.setMoney(cursor.getInt(1));
                record.setRecord_date(cursor.getString(2));
                record.setComment(cursor.getString(3));
                orderList.add(record);
            }
            cursor.close();
            return orderList;
        }else{
            cursor.close();
            return new ArrayList<MoneyRecord>();
        }


    }

}
