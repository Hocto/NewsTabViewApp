package com.example.asus.ceng427hw2;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Asus on 26.04.2018.
 */

public class Tab3Fragment extends Fragment{
    private static final String TAG = "Tab3Fragment";
    String URL="http://www.ybu.edu.tr/muhendislik/bilgisayar/";
    ArrayAdapter<String> listAdapter;
    ArrayList<String> links;
    ListView listTask;
    MyTask mt;
    TextView announcement;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab3,container,false);
        announcement = (TextView) view.findViewById(R.id.announcement);
        listTask = (ListView) view.findViewById(R.id.listTask);
        ArrayList<String> taskList = new ArrayList<String>();
        loadTasks(taskList);
        mt = new MyTask();
        mt.execute(URL);

        return view;
    }


    private void loadTasks(ArrayList<String> taskList) {
        if(listAdapter==null){
            listAdapter = new ArrayAdapter<String>(getContext(), R.layout.row, R.id.taskTitle, taskList);
            listTask.setAdapter(listAdapter);
            listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = links.get(position+3);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
        else{
            listAdapter.clear();
            listAdapter.addAll(taskList);
            listAdapter.notifyDataSetChanged();
        }
    }
    class MyTask extends AsyncTask<String, Void, Elements> {
        Document doc;
        Elements words;
        String first="failed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            announcement.setText("Please wait...");
        }

        protected Elements doInBackground(String... params) {
            String url=params[0];
            links = new ArrayList<String>();

            try {
                doc = Jsoup.connect(url).get();
                words = doc.select("div.cncItem");
                for (Element link : words) {
                    String url1 = link.select("a").attr("abs:href");
                    links.add(url1);
                    System.out.println(url1);

                }
                first = words.text();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return words;
        }

        protected void onPostExecute(Elements result) {
            super.onPostExecute(result);
            ArrayList<String> taskList = new ArrayList<String>();
            ArrayList<String> taskList1 = new ArrayList<String>();
            ListIterator<Element> postIt = result.listIterator();


            for(int i = 0; i < 9; i++){
                if(postIt.hasNext()){
                    taskList.add(postIt.next().text());
                }
            }
            for(int i = 3; i < 9; i++){
                taskList1.add(taskList.get(i));
                System.out.println(taskList1.get(i-3));
            }
            loadTasks(taskList1);
            announcement.setText("");
        }
    }
}
