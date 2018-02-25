package com.example.pierrdunn.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by pierrdunn on 25.02.18.
 */

//Singleton

public class CrimeLab {
    //преписка s обозначает, что переменная статическая
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime("Crime #" + i, (i % 2 == 0));
            mCrimes.add(crime);
        }
    }

    //получить все элементы
    public List<Crime> getCrimes(){
        return mCrimes;
    }

    //Получить один элемент
    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes) {
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
