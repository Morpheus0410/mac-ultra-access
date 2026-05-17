package org.morpheus;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class ExoPlayerBridge {
    private static Dialog dialog;
    private static ExoPlayer player;

    public static void open(final Activity activity, final String url) {
        if (activity == null || url == null || url.length() == 0) return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    closeInternal();

                    dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    FrameLayout root = new FrameLayout(activity);
                    root.setBackgroundColor(Color.BLACK);

                    player = new ExoPlayer.Builder(activity).build();

                    PlayerView playerView = new PlayerView(activity);
                    playerView.setUseController(true);
                    playerView.setPlayer(player);

                    root.addView(playerView, new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));

                    Button closeButton = new Button(activity);
                    closeButton.setText("BEENDEN");
                    closeButton.setTextColor(Color.WHITE);
                    closeButton.setBackgroundColor(Color.argb(190, 120, 0, 0));

                    FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    closeParams.gravity = Gravity.TOP | Gravity.RIGHT;
                    closeParams.setMargins(0, 35, 35, 0);
                    root.addView(closeButton, closeParams);

                    closeButton.setOnClickListener(v -> close(activity));
                    dialog.setOnDismissListener(d -> closeInternal());
                    dialog.setContentView(root);

                    Window window = dialog.getWindow();
                    if (window != null) {
                        window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        window.setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        );
                    }

                    player.setMediaItem(MediaItem.fromUri(url));
                    player.prepare();
                    player.play();

                    dialog.show();

                    Window showWindow = dialog.getWindow();
                    if (showWindow != null) {
                        showWindow.setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        );
                    }

                } catch (Throwable t) {
                    try { closeInternal(); } catch (Throwable ignored) {}
                }
            }
        });
    }

    public static void close(final Activity activity) {
        if (activity == null) {
            closeInternal();
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeInternal();
            }
        });
    }

    private static void closeInternal() {
        try {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }
        } catch (Throwable ignored) {}

        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        } catch (Throwable ignored) {}
    }
}
