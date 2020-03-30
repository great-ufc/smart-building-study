package great.android.cmu.ubiapp.adaptations;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import task.Task2;

public class HourAdapt extends Task2 {

    View viewBackground;
    ViewGroup parentLayout;


    public HourAdapt(View viewBackground, ViewGroup parentLayout){
        this.viewBackground = viewBackground;
        this.parentLayout = parentLayout;
    }


    @Override
    public void recebeToken(Object o) {

    }

    @Override
    public Object retornaToken() {
        return null;
    }

    @Override
    public void executar() {

    }

    public void executar(Calendar hour){

        evaluate(hour);

    }

    @Override
    public Object retorno() {
        return null;
    }

    @Override
    public void evaluate(Object o) {
        Calendar rightnow = (Calendar) o;
        int currentHourIn24Format = rightnow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)


        if(currentHourIn24Format > 18){

            viewBackground.setBackgroundColor(Color.BLACK);

            for (int count=0; count < parentLayout.getChildCount(); count++){
                View view = parentLayout.getChildAt(count);
                if(view instanceof TextView){
                    ((TextView)view).setTextColor(Color.WHITE);
                }
            }

        }
    }
}
