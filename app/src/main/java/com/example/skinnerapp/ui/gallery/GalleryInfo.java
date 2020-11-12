package com.example.skinnerapp.ui.gallery;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.skinnerapp.R;

import org.w3c.dom.Text;


public class GalleryInfo extends Fragment {

    private TextView tv_tituloFragment;
    private GalleryInfoModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryInfoModel.class);
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        tv_tituloFragment = (TextView) root.findViewById(R.id.textView4);
        tv_tituloFragment.setPaintFlags(tv_tituloFragment.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setupHyperlink(root);

        return root;
    }

    private void setupHyperlink(View root) {

        TextView aspavitlink =(TextView)root.findViewById(R.id.aspavitlink);
        aspavitlink.setClickable(true);
        aspavitlink.setMovementMethod(LinkMovementMethod.getInstance());
        aspavitlink.setText(Html.fromHtml("<u><b><font color='blue' ><a href='http://www.aspavit.org/' >http://www.aspavit.org/" + "</a></font></b></u>"));

        TextView aepsolink =(TextView)root.findViewById(R.id.aepsolink);
        aepsolink.setClickable(true);
        aepsolink.setMovementMethod(LinkMovementMethod.getInstance());
        aepsolink.setText(Html.fromHtml("<u><b><font color='blue' ><a href='https://www.aepso.org/' >https://www.aepso.org/" + "</a></font></b></u>"));

        TextView soarpsolink =(TextView)root.findViewById(R.id.soarpsolink);
        soarpsolink.setClickable(true);
        soarpsolink.setMovementMethod(LinkMovementMethod.getInstance());
        soarpsolink.setText(Html.fromHtml("<u><b><font color='blue' ><a href='https://www.soarpso.org/' >https://www.soarpso.org/" + "</a></font></b></u>"));
    }
}