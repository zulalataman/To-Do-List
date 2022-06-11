package com.projegazi.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.projegazi.todolist.Users.loginActivity;

public class SearchActivity extends AppCompatActivity {

    ListView listView;
    String[] name= {"KAYIT OLMA\n\nEğer bir hesabınız yoksa ve kayıt olmak isterseniz uygulama giriş ekranından 'Hesabın Yok Mu?Kaydol' yazısına basarsanız sizi kayıt olma sayfasına yönlendirecektir. Bu sayfada önce doğru bir E-mail ve en az 7 haneden oluşan şifre girerek 'Kayıt Ol' butouna basınız. Daha sonra E-mailinize gönderilen doğrulama linkine tıklayarak kaydınızı tamamlamış olursunuz.\n\n",
            "GİRİŞ YAPMA \n\nGiriş yapmak için öncelikle kaydolduğunuz emailin doğrulama linkine tıkladığınıza emin olunuz. Daha sonra giriş ekranındaki istenilen bilgileri(Email-Şifre) doğru bir şekilde girdikten sonra 'Giriş Yap' butonuna tıklamaınız yeterlidir. Eğer bilgileriniz doğruysa kendinizi not uygulamasının içinde bulacaksınız. Yanlışsa size bir uyarı mesajı verilecektir.\n\n",
            "ŞİFRE YENİLEME\n\nŞifrenizi mi unuttunuz? Çözümü çok basit. Giriş ekranındaki 'Şifrenizi mi unuttunuz' yazısına bastığınızda sizi bir sayfaya yönlendirecektir. Bu sayfada sizden kullanmış olduğunuz e-maili istemektedir. Bu maili girdikten sonra eğer sistemdeki hesabınızla uyuşursa bir e-posta alacaksınız. Bu e-posta size yeni bir şifre oluşturmanız için gelen bir linktir. Bu linke tıklayarak yeni bir şifre oluşturabilir ve bu şifrenizle uygulamaya giriş yapabilirsiniz.\n\n",
            "GÖREV EKLEME \n\nYapacağınız görevleri eklemek çok basit. Giriş yaptıktan sonra açılan sayfada sağ alt köşede '+' butonunu görmektesiniz. Bu butona tıkladıktan sonra yapacağınız görevi ve tarihi seçerek 'Kaydet' butonuna basınız. Göreviniz ana ekranda görünecektir.\n\n",
            "GÖREV SİLME\n\nGörevinizi silmek istediğinizde eklediğiniz görevi sağ tarafa çekmeniz yeterli. Direkt sistemden silinecektir.\n\n",
            "GÖREV GÜNCELLEME\n\nGörevinizi düzenlemek istediğinizde ekli olan görevi sağ tafafa çekmeniz yeterli. Önünüze açılan pencerede görevinizi düzenleyip tarihinizi ayarlayıp tekrar kaydedebilirsiniz.\n\n",
            "HESAPTAN ÇIKIŞ YAPMA\n\nBaşka bir hesapla giriş yapmak için sağ üstte yer alan 'X' butonuna basmanız yeterlidir. Bu butona basınca sizi tekrar giriş ekranına atacaktır. Farklı hesapla buradan giriş yapabilirsiniz.\n\n",
            "GÖREV TAMAMLAMA\n\nEğer görevinizi yaptığınızı düşünüyorsanız eklediğiniz görevin solundaki checkbox'u işaretlemeniz yeterli \uD83D\uDE42\n\n"};

    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView =findViewById(R.id.listview);

        arrayAdapter = new ArrayAdapter<String >(this, android.R.layout.simple_list_item_1, name);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) menuItem.getActionView();


        searchView.setQueryHint("Aramak istediğinizi yazınız...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(SearchActivity.this, loginActivity.class));
        return super.onOptionsItemSelected(item);


    }
}