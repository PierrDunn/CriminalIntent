package com.example.pierrdunn.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.text.format.DateFormat;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by pierrdunn on 24.02.18.
 */

public class CrimeFragment extends Fragment {

    //Для пакета аргументов Bundle
    private static final String ARG_CRIME_ID = "crime_id";

    //Для диалогового окна
    private static final String DIALOG_DATE = "DialogDate";

    //Код запроса для целевого фрагмента
    private static final int REQUEST_DATE = 0;
    //Код запроса контактов у Android
    private static final int REQUEST_CONTACT = 1;

    //private static final String ARG_POSITION = "position";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    //private int mPosition;
    private Button mReportButton;
    private Button mSuspectButton;

    //Создание пакета аргументов Bundle
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Чтение данных из интента
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        //mPosition = (Integer) getArguments().getSerializable(ARG_POSITION);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DataPickerFragment dialog = DataPickerFragment
                        .newInstance(mCrime.getDate());
                //Назначение целевого фрагмента
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        //Отправка неявного интента для запроса контактов
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null)
            mSuspectButton.setEnabled(false);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DataPickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            Log.v("UPDATE_DATE", date.toString());
            updateDate();
        } else if(requestCode == REQUEST_CONTACT && data != null){
            //Получение контакта
            Uri contactUri = data.getData();
            //Определение полей, значения которых должны быть
            //возвращены запросом
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //Выполнение запроса - contactUri здесь выполняет
            //функции условия "where"
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                //проверка получения результата
                if(c.getCount() == 0)
                    return;
                //Извлечение первого столбца данных
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    //Возвращение результата активности-хосту
    public void returnResult(){
        Intent resultIntent = new Intent();
       // resultIntent.putExtra(ARG_POSITION, mPosition);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    //REPORT
    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        } else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dataFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dataFormat,
                mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        } else{
            suspect = getString(R.string.crime_report_suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }
}
