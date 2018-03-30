package app.jugaad.daeira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SelectArtcleToReadActivity extends AppCompatActivity {

    ListView articlesToReadListView;

    //todo Remove this later
    List<String> sampleList = new ArrayList<String>();
    String[] sampleStringArray;

    //todo REMOVE LATER
    void sampleArrayPopulation(){
        sampleList.add("Turing");
        sampleList.add("SSadasdasd");
        sampleList.add("sfsfsffasfas");
        sampleList.add("dfsdfsdfdsfdfsdfdfdsfdfdfdffsdfdsfdsff");
        sampleList.add("sfsfsffasfasdasdasdasdsadsadsadsdsadsadsadsadsdsadsad");
        sampleList.add("sfsfsffasfafdsfsfddfdsfdsfdsfdsfdsfdfdfdfdfdfds");

        sampleStringArray = new String[sampleList.size()];

        int i =0;
        for (String str: sampleList){
            sampleStringArray[i] = sampleList.get(i);
            i++;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_artcle_to_read);

        //todo remove line of code
        sampleArrayPopulation();
        articlesToReadListView = (ListView) findViewById(R.id.articles_to_read_list_view);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sampleStringArray);
        articlesToReadListView.setAdapter(adapter);

    }
}
