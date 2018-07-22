package deu.csc.lecture.sampleapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.view.View.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner dropdown = (Spinner)findViewById(R.id.menuDropdown);
        // Araçların random ilerlemesini sağlayacak olan button

        //Araçların ilk konumlarına geri dönebilmesi için ilk konumları listeye alındı.
        final List<Float> firstY = new ArrayList<Float>(Arrays.<Float>asList(findViewById(R.id.car1Img).getY(), findViewById(R.id.car2Img).getY(),findViewById(R.id.car3Img).getY()));
        // Araçların tanımı
        final ImageView car1Img =(ImageView) findViewById(R.id.car1Img); // 1. araba görselinin tanımlanması
        final ImageView car2Img =(ImageView) findViewById(R.id.car3Img); // 2. araba görselinin tanımlanması
        final ImageView car3Img =(ImageView) findViewById(R.id.car3Img); // 3. araba görselinin tanımlanması
        /* dikey hizalama için kullanılacak LinearLayout
        *  Araba hareket doğrultusu için kullanılacak -> dikey hizalama
        * */
        // hareket buttonu gazlamak için
        final Button gass = (Button) findViewById(R.id.button);
        gass.setEnabled(false);
        //Kazananın yazıldığı içerik kısmı ve tahmin sonucu
        final TextView win=(TextView) findViewById(R.id.win);
        // Hareketin gerçekleşeceği layout
        final LinearLayout pist=(LinearLayout)findViewById(R.id.pist);
        //dropdown seçim yapıldığında olacaklar
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                gass.setEnabled(true);
                gass.setText("Gazla");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //Hareket sağlayan butona tıklandığında yapılacaklar
        gass.setOnClickListener(new View.OnClickListener() {
            // Kazananın gösterildiği visible içerik metnin başta unvisible tutulması işlemi için gerekli değişken.
            Boolean kntrl=false;
            @Override
            public void onClick(View v) { // Buttona tıklanması halinde yapılacak işlemler
                // Araçların rasgele hareket etmeleri için oluşturulan random sınıfı
                if(gass.getText()=="Araç Seç ve Tekrar Oyna") gass.setText("Gazla");
                if(win.getVisibility()==View.VISIBLE){
                    kntrl=false;
                    // ilk konumlarına geri dönme
                    findViewById(R.id.car1Img).setY(firstY.get(0));
                    findViewById(R.id.car2Img).setY(firstY.get(1));
                    findViewById(R.id.car3Img).setY(firstY.get(2));
                    win.setVisibility(View.INVISIBLE);
                    dropdown.setEnabled(true);
                }else{
                    Random rnd=new Random();
                    if(kntrl==false) { // Kazanan belli olmadığı süreç boyunca ilerleme devam etmeli
                        /*Her bir araba resmi random sınıfından üretilen rasgele integer sayıya göre hareket etmekte*/
                        findViewById(R.id.car1Img).setY(findViewById(R.id.car1Img).getY() + rnd.nextInt(50) + 1);
                        findViewById(R.id.car2Img).setY(findViewById(R.id.car2Img).getY() + rnd.nextInt(50) + 1);
                        findViewById(R.id.car3Img).setY(findViewById(R.id.car3Img).getY() + rnd.nextInt(50) + 1);
                        dropdown.setEnabled(false);
                    }
                    /*Yarışın bitiş noktası ekranın en alt kısmı olarak ayarlandı
                    * Ekran yüksekliği aşıldığında içerik kısmı aktif edilip kazanan ilan ediliyor.*/
                    if((float)findViewById(R.id.car1Img).getY()>= pist.getHeight()-car3Img.getHeight()){win.setText("Kazanan Birinci \n Araba"); if(dropdown.getSelectedItemPosition()==0){win.setTextColor(Color.GREEN); win.setText(win.getText()+ "\nDoğru Bildin!\nTebirkler");}else{win.setTextColor(Color.RED); win.setText(win.getText()+ "\nYanlış Tahmin!\nKaybettin.");}kntrl=true;}
                    if((float)findViewById(R.id.car2Img).getY()>=pist.getHeight()-car3Img.getHeight()){win.setText("Kazanan İkinci \n Araba");if(dropdown.getSelectedItemPosition()==1){win.setTextColor(Color.GREEN);win.setText(win.getText()+ "\nDoğru Bildin!\nTebirkler");}else{win.setTextColor(Color.RED); win.setText(win.getText()+ "\nYanlış Tahmin!\nKaybettin.");}kntrl=true;}
                    if((float)findViewById(R.id.car3Img).getY()>=pist.getHeight()-car3Img.getHeight()){win.setText("Kazanan Üçüncü \n Araba");if(dropdown.getSelectedItemPosition()==2){win.setTextColor(Color.GREEN);win.setText(win.getText()+ "\nDoğru Bildin!\nTebirkler");}else{win.setTextColor(Color.RED); win.setText(win.getText()+ "\nYanlış Tahmin!\nKaybettin.");}kntrl=true;}
                    if(kntrl) {
                        win.setVisibility(View.VISIBLE);
                        gass.setEnabled(false);
                        dropdown.setEnabled(true);
                        gass.setText("Araç Seç ve Tekrar Oyna");
                    }
                }
            }
        });
    }
}
