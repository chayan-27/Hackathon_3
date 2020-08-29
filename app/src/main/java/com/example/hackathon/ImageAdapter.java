package com.example.hackathon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    List<String> list;

    public ImageAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.assigncard, parent, false);
        return new ImageHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {


        new TestBack(holder.imgpdf).execute(new File("/document/primary:DCIM/Camera/"+list.get(position)));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder{

        ImageView imgpdf;
        CardView cardView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imgpdf = (ImageView) itemView.findViewById(R.id.imgpdf);
            cardView = (CardView) itemView.findViewById(R.id.cards);
        }
    }

    public class TestBack extends AsyncTask<File, Void, String>{
        ImageView imageView;
        Bitmap bitmap1;

        public TestBack(ImageView imageView) {
            this.imageView = imageView;


        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This will normally run on a background thread. But to better
         * support testing frameworks, it is recommended that this also tolerates
         * direct execution on the foreground thread, as part of the {@link #execute} call.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param files The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(File... files) {
            bitmap1= BitmapFactory.decodeFile(files[0].getAbsolutePath());



            return "yes";
        }


        @Override
        protected void onPostExecute(String s) {
            imageView.setImageBitmap(bitmap1);
        }
    }
}
