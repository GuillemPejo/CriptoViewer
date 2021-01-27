package me.guillem.criptoviewer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import me.guillem.criptoviewer.R;
import me.guillem.criptoviewer.retrofit.Datum;

/**
 * * Created by Guillem on 27/01/21.
 */

public class CoinDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);

        Intent intent = getIntent();
        Datum datum = (Datum) intent.getSerializableExtra("coin");

        TextView name = findViewById(R.id.name);
        TextView price = findViewById(R.id.price);
        TextView date = findViewById(R.id.date);

        TextView launch_date = findViewById(R.id.launch_date);
        TextView cmcrank = findViewById(R.id.cmcrank);

        TextView symbol = findViewById(R.id.symbol);
        TextView volume24h = findViewById(R.id.volume24h);
        TextView circulating_supply = findViewById(R.id.circulating_supply);
        TextView max_supply = findViewById(R.id.max_supply);
        TextView market_cap = findViewById(R.id.market_cap);

        TextView change1h = findViewById(R.id.change1h);
        TextView change24h = findViewById(R.id.change24h);
        TextView change7d = findViewById(R.id.change7d);

        ImageView icon = findViewById(R.id.icon);

        name.setText(datum.getName());
        symbol.setText(datum.getSymbol());
        cmcrank.setText(String.format(datum.getCmcRank()+("")));
        launch_date.setText(parseDateToddMMyyyy(datum.getDateAdded()));

        price.setText("$" + String.format("%,f", datum.getQuote().getUSD().getPrice()));
        date.setText("Last Updated: " + parseDateToddMMyyyy(datum.getLastUpdated()));

        volume24h.setText("$" + String.format("%,d", Math.round(datum.getQuote().getUSD().getVolume24h())));

        circulating_supply.setText(String.format("%.0f", datum.getCirculatingSupply()) + " " + datum.getSymbol());
        max_supply.setText(String.format("%.0f", datum.getMaxSupply()) + " " + datum.getSymbol());

        market_cap.setText("$" + String.format("%,d", Math.round(datum.getQuote().getUSD().getMarketCap())));

        change1h.setText(String.format("%.2f", datum.getQuote().getUSD().getPercentChange1h()) + "% 1H");
        change1h.setTextColor(Color(String.format("%,f", datum.getQuote().getUSD().getPercentChange1h())));

        change24h.setText(String.format("%.2f", datum.getQuote().getUSD().getPercentChange24h()) + "% 24h");
        change24h.setTextColor(Color(String.format("%,f", datum.getQuote().getUSD().getPercentChange24h())));

        change7d.setText(String.format("%.2f", datum.getQuote().getUSD().getPercentChange7d()) + "% 7D");
        change7d.setTextColor(Color(String.format("%,f", datum.getQuote().getUSD().getPercentChange7d())));


        String logoURL = (String) intent.getSerializableExtra("icon");
        if(logoURL != null){
            logoURL = logoURL.replace("64x64", "200x200");

            try {
                Picasso.get().load(logoURL).into(icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private int Color(String price){
        if(price.contains("-")){
            return Color.RED;
        }
        return Color.rgb(139,195,74);
    }


    private String parseDateToddMMyyyy(String time) {
        //parse the server timestamp. Make sure it is in UTC timezone as per API specifications.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        //format the utc server timestamp to local timezone.
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        output.setTimeZone(TimeZone.getDefault());

        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(date);
    }

}
