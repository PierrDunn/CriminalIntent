package com.example.pierrdunn.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.pierrdunn.criminalintent.database.*;

import static com.example.pierrdunn.criminalintent.database.CrimeDbSchema.*;

/**
 * Created by pierrdunn on 25.02.18.
 */

//Singleton

public class CrimeLab {
    //преписка s обозначает, что переменная статическая
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;
    //Для создания базы данных
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        //Для создания базы данных
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
    }

    //добавить объект через меню
    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    //получить все элементы
    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    //Получить один элемент
    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{ id.toString() }
        );

        try {
            if(cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    //Метод обновления строк
    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    //Метод запроса из базы
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
          CrimeTable.NAME,
                null, //сolums -- с null выбираются все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CrimeCursorWrapper(cursor);
    }

    //Изменение Crime в ContentValues(типа HashMap) для хранения в базе
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }
}
