package com.example.pierrdunn.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by pierrdunn on 28.02.18.
 */

public class CrimePageActivity extends AppCompatActivity {

    //Новый идентификатор и интент для передачи данных CrimeFragment
    private static final String EXTRA_CRIME_ID =
            "com.example.pierrdunn.criminalintent.crime_id";

    //private static final String EXTRA_POSITION =
    //"com.example.pierrdunn.criminalintent.position";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;


    public static Intent newIntent(Context packageContext, UUID crimeID){
        //Intent intent = new Intent(packageContext, CrimeActivity.class);
        Intent intent = new Intent(packageContext, CrimePageActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        //intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_crime_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for(int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
