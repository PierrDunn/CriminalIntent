package com.example.pierrdunn.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.format.DateFormat;

import java.util.List;

/**
 * Created by pierrdunn on 25.02.18.
 */

public class CrimeListFragment extends Fragment{

    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mAdapter;

    //Save position
    private static final String KEY_POSITION = "position";
    private int mPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,
                container, false);
        mCrimeRecycleView = (RecyclerView) view.findViewById(R.id.crime_recycle_view);
        //Без назначения LayoutManager возникает ошибка
        //LayoutManager управляет позиционированием элементов, а так же определяет
        //поведение прокрутки
        //LayoutManager размещает элементы в вертикальном списке
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null)
            mPosition = savedInstanceState.getInt(KEY_POSITION, 0);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //Переопределение метода для сохранения ключа между орентациями
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putInt(KEY_POSITION, mPosition);
    }

    //Метод, настраивающий пользовательский интерфейс CrimeListFragment
    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
            //Перезагрузка одного элемента
            //mAdapter.notifyItemChanged(mPosition);
        }
    }

    //ViewHolder заполняет макет списка
    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            //Реагировать на касание элемента списка
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateFormat.format("dd, MM, yyyy hh:mm:ss a",mCrime.getDate()));
            mSolvedImageView.setVisibility(mCrime.isSolved() ? View.VISIBLE :
            View.INVISIBLE);
        }

        //переопределение слушателя касания
        @Override
        public void onClick(View v) {
            mPosition = getAdapterPosition();
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            Intent intent = CrimePageActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
            Log.v("POSITION: ", Integer.toString(mPosition));
        }
    }

    //Возвращение результата активности-хосту
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CRIME){
            //Обработка результата
        }
    }

    //Адаптер
    //Класс RecyclerView взаимодействует с адаптером, когда требуется создать
    //новый объект ViewHolder или связать существующий объект ViewHolder с
    //объектом Crime
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

    }
}
