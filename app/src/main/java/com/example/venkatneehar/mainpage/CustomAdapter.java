package com.example.venkatneehar.mainpage;



        import android.content.Context;
        import android.graphics.drawable.Drawable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.List;

public class CustomAdapter extends ArrayAdapter<Person> {
    private final List<Person> person;

    public CustomAdapter(Context context, int resource, List<Person> person) {
        super(context, resource, person);
        this.person = person;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row, null);
        TextView textView = (TextView) row.findViewById(R.id.rowText);
        textView.setText(person.get(position).getName());

        ImageView imageView = (ImageView) row.findViewById(R.id.rowImage);
        //   InputStream inputStream = getContext().getAssets().open(String.valueOf(person.get(position).getFilename()));
        //  Drawable drawable = Drawable.createFromStream(inputStream, null);
        //  imageView.setImageDrawable(drawable);
        imageView.setImageResource(person.get(position).getFilename());

        return row;
    }
}