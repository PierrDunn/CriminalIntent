package com.example.pierrdunn.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by pierrdunn on 25.02.18.
 */

//Новый контроллер

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
