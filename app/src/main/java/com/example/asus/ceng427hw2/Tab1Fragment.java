package com.example.asus.ceng427hw2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import android.os.AsyncTask;
import android.widget.TextView;
/**
 * Created by Asus on 26.04.2018.
 */

public class Tab1Fragment extends Fragment{

    private static final String TAG = "Tab1Fragment";
    MyTask mt;
    TextView menu;
    String URL="http://ybu.edu.tr/sks/";
    //String URL="http://en.wikipedia.org/";

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab1,container,false);
        menu = (TextView) view.findViewById(R.id.menu);
        mt = new MyTask();
        mt.execute(URL);

        return view;
    }
    class MyTask extends AsyncTask<String, Void, Elements> {
        Document doc;
        Elements words;
        String what1="failed";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menu.setText("Please wait...");
        }

        protected Elements doInBackground(String... params) {
            String url=params[0];
            try {
                doc = Jsoup.connect(url).get();
                words = doc.select("p");
                what1 = words.text();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return words;
        }

        protected void onPostExecute(Elements result) {
            super.onPostExecute(result);
            String menu1 = "";
            ArrayList<String> lunch = new ArrayList<String>();
            ListIterator<Element> postIt = result.listIterator();

            for(int i = 0; i < 4; i++){
                if(postIt.hasNext()){
                    lunch.add(postIt.next().text());
                    System.out.println(lunch.get(i));
                }
            }

            String[] lunch1 = new String[lunch.size()];
            lunch.toArray(lunch1);

            menu1 = "Öğle Yemeği: \n\n" + lunch1[0] + "\n\n" + lunch1[1] + "\n\n" + lunch1[2] + "\n\n" + lunch1[3] + "\n\n";
            menu.setText(menu1);
        }
    }

}

