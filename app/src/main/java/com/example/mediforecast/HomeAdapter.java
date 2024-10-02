package com.example.mediforecast;

import static android.content.ContentValues.TAG;
import static android.view.Gravity.apply;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    Context context;
    ArrayList<Home> list;


    public HomeAdapter(Context context, ArrayList<Home> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Home item = list.get(position);
        //condition for title
        String RhuOrBarangay = "";
        String[] barangays = {"Balucuc", "Calantipe", "Cansinala", "Capalangan", "Colgante",
                "Paligui", "Sampaloc", "San Juan", "San Vicente", "Sucad",
                "Sulipan", "Tabuyuc"};
        switch (item.getRhu()) {
            case "1":
                RhuOrBarangay = "Rural Health Unit I";
                break;
            case "2":
                RhuOrBarangay = "Rural Health Unit II";
                break;
            case "3":
                RhuOrBarangay = "Rural Health Unit III";
                break;
            default:
                if (Arrays.asList(barangays).contains(item.getRhu())) {
                    RhuOrBarangay = item.getRhu() + " Health Center";
                } else {
                    RhuOrBarangay = item.getRhu();
                }
                break;
        }
        holder.textViewRhu.setText(RhuOrBarangay);
        Glide.with(context)
                .load(getLogoResource(item.getRhu())) // Use the helper method to get the logo resource
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageLogo); // Load into the logo ImageView
        //format of date and time
        String timestamp = item.getCreated_by();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        try {
            Date date = inputFormat.parse(timestamp);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM. dd, yyyy hh:mm a", Locale.getDefault());
            String formattedDate = outputFormat.format(date);

            holder.textViewCreatedBy.setText(formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.textViewMessage.setText(item.getPostMessage());

        // Handle video and image display
        String mediaURL = item.getPostImg();
        if (item.getFileType().equals("video")) {
            holder.imageView.setVisibility(View.GONE);
            holder.playerView.setVisibility(View.VISIBLE);


            ExoPlayer player = new ExoPlayer.Builder(context).build();
            holder.setPlayer(player);
            holder.playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(mediaURL);
            player.setMediaItem(mediaItem);
            player.prepare();

//            // Add listener for playback events (Optional)
//            player.addListener(new Player.Listener() {
//                @Override
//                public void onPlayerError(PlaybackException error) {
//                    Log.e("HomeAdapter", "Error playing video: " + error.getMessage());
//                }
//
//                @Override
//                public void onIsPlayingChanged(boolean isPlaying) {
//                    Log.d("HomeAdapter", "Is playing: " + isPlaying);
//                }
//            });

        } else if (item.getFileType().equals("image")) {
            holder.playerView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(mediaURL).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
            holder.playerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCreatedBy, textViewMessage, textViewRhu;
        ImageView imageView, imageLogo;
        PlayerView playerView;
        ExoPlayer player;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRhu = itemView.findViewById(R.id.textView8);
            textViewCreatedBy = itemView.findViewById(R.id.textView6);
            textViewMessage = itemView.findViewById(R.id.textView9);
            imageView = itemView.findViewById(R.id.imageView7);
            imageLogo = itemView.findViewById(R.id.logo);
            playerView = itemView.findViewById(R.id.playerView);
        }

        public void setPlayer(ExoPlayer player) {
            this.player = player;
        }

        public ExoPlayer getPlayer() {
            return player;
        }
    }

    private int getLogoResource(String rhu) {
        switch (rhu) {
            case "1":
                return R.drawable.rhu1logo;
            case "2":
                return R.drawable.rhu2logo;
            case "3":
                return R.drawable.rhu3logo;
            case "Balucuc":
                return R.drawable.balucuc;
            case "Calantipe":
                return R.drawable.calantipe;
            case "Cansinala":
                return R.drawable.cansinala;
            case "Capalangan":
                return R.drawable.capalangan;
            case "Colgante":
                return R.drawable.colgante;
            case "Paligui":
                return R.drawable.paligui;
            case "Sampaloc":
                return R.drawable.sampaloc2;
            case "San Juan":
                return R.drawable.sanjuan;
            case "San Vicente":
                return R.drawable.sanvicente;
            case "Sucad":
                return R.drawable.sucad;
            case "Sulipan":
                return R.drawable.sulipan2;
            case "Tabuyuc":
                return R.drawable.tabuyuc;
            default:
                return R.drawable.backgroundlogo;
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);

        // Release player resources when view is recycled
        ExoPlayer player = holder.getPlayer();
        if (player != null) {
            player.release();
            holder.setPlayer(null);
        }
    }
}
