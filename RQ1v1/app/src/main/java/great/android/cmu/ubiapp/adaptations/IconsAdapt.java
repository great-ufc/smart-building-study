package great.android.cmu.ubiapp.adaptations;

import android.content.Context;
import android.view.View;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import task.Task2;

public class IconsAdapt extends Task2 {

    Context context;
    String elementName;


    CircleImageView iconLight;
    CircleImageView iconShake;



    public IconsAdapt(Context context, String elementName){

        this.context = context;
        this.elementName = elementName;

    }

    public IconsAdapt(CircleImageView iconLight, CircleImageView iconShake){


        this.iconLight = iconLight;
        this.iconShake = iconShake;

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

    public void executar(Map<String, String> HashString){
        evaluate(HashString.get(elementName));
    }

    public void executar(int visibility){
        iconLight.setVisibility(visibility);
        iconShake.setVisibility(visibility);
    }

    @Override
    public Object retorno() {
        return null;
    }

    @Override
    public void evaluate(Object o) {

        if (o != null) {

            System.out.println(o.toString());

            if(o.toString().equals("\""+"light"+"\"")){
                executar(View.VISIBLE);
            }

        }

    }

//    public void evaluate(String expandedListText) {
//        System.out.println (expandedListText);
//        //apresenta as imagens de controle apenas aos itens que tem light no nome
//        if (expandedListText != null) {
//
//            if (expandedListText.contains("light")) {
//                executar(0);
//
//            }
//        }
//    }


}
