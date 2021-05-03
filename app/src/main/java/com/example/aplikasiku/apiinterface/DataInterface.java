package com.example.aplikasiku.apiinterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public interface DataInterface {
    // Init format
    final public static SimpleDateFormat myDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("id","ID"));
    final public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmm", java.util.Locale.getDefault());
    final public static SimpleDateFormat DateFormatChart = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", new Locale("id","ID"));
    // Init format
    final public static SimpleDateFormat DateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("id","ID"));
    final public static SimpleDateFormat simpleDate = new SimpleDateFormat("ddMMyyyy", java.util.Locale.getDefault());
    final public static SimpleDateFormat formatwaktu = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
    final public static SimpleDateFormat DateDataFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("id","ID"));


    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        try {
            parsed = simpleDate.parse(inputDate);
            outputDate = DateFormat.format(parsed);

        } catch (ParseException e) {
        }

        return outputDate;

    }
}
