package com.example.pierrdunn.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    //Новый идентификатор и интент для передачи данных CrimeFragment
    private static final String EXTRA_CRIME_ID =
            "com.example.pierrdunn.criminalintent.crime_id";

    //private static final String EXTRA_POSITION =
            //"com.example.pierrdunn.criminalintent.position";

    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        //intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

}
