package com.example.pierrdunn.criminalintent.database;

/**
 * Created by pierrdunn on 01.03.18.
 */

public class CrimeDbSchema {
    //Внутренний класс для описания таблицы
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        //Описание столбцов
        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE= "date";
            public static final String SOLVED = "solved";
        }
    }
}
