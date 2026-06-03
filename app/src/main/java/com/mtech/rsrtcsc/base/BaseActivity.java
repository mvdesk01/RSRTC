package com.mtech.rsrtcsc.base;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<T> extends AppCompatActivity {

    protected T binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=getActivityBinding();
        if(binding instanceof ViewDataBinding){
            setContentView(((ViewDataBinding) binding).getRoot());
            init();
            initCtrl();
        }

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content),
                (v, insets) -> {

                    WindowInsetsCompat insetsCompat = insets;
                    Insets systemBars = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
        );
    }
    protected abstract T getActivityBinding();
    protected abstract void init();
    protected abstract void initCtrl();

}
