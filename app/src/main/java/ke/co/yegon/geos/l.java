package ke.co.yegon.geos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class l extends AppCompatActivity{
    Button l,r;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab);
        l = findViewById(R.id.login);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Next time you will be logged in.",Toast.LENGTH_SHORT).show();
            }
        });
        r = findViewById(R.id.register);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Next time you will be registered.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
